package com.aihoo.domain.doctor.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.aihoo.alicloud.AliCloudComponent;
import com.aihoo.common.PageResult;
import com.aihoo.constant.ImUserPrefix;
import com.aihoo.domain.doctor.dto.DoctorEnableDisableRequestDto;
import com.aihoo.domain.doctor.dto.DoctorUserAddRequestDto;
import com.aihoo.domain.doctor.dto.DoctorUserDetailsDto;
import com.aihoo.domain.doctor.dto.DoctorUserDto;
import com.aihoo.domain.doctor.dto.DoctorUserUpdateRequestDto;
import com.aihoo.domain.doctor.dto.DoctorVisitSetRequest;
import com.aihoo.domain.doctor.dto.DoctorWelcomeMessageRequest;
import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.entity.DoctorUserLog;
import com.aihoo.domain.doctor.entity.DoctorVisitSet;
import com.aihoo.domain.doctor.entity.DoctorWelcomeMessageSet;
import com.aihoo.domain.doctor.mapper.DoctorUserLogMapper;
import com.aihoo.domain.doctor.mapper.DoctorUserMapper;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.doctor.service.DoctorVisitSetService;
import com.aihoo.domain.doctor.service.DoctorWelcomeMessageSetService;
import com.aihoo.domain.drug.entity.Hospital;
import com.aihoo.domain.drug.entity.HospitalDepartment;
import com.aihoo.domain.drug.service.HospitalDepartmentService;
import com.aihoo.domain.drug.service.HospitalService;
import com.aihoo.domain.sys.service.DictService;
import com.aihoo.enums.DictTypeEnum;
import com.aihoo.exception.BizException;
import com.aihoo.properties.TencentProperties;
import com.aihoo.redis.RedisConstant;
import com.aihoo.redis.RedisService;
import com.aihoo.security.AuthUtil;
import com.aihoo.util.*;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 医生用户表 service 实现（迁自 doctor-api 的 DoctorUserServiceImpl + 合并 patient-api 的同名 service）。
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class DoctorUserServiceImpl extends ServiceImpl<DoctorUserMapper, DoctorUser> implements DoctorUserService {

    private final DoctorUserMapper doctorUserMapper;
    @Autowired(required = false)
    private RedisService redisService;
    private final DoctorUserLogMapper doctorUserLogMapper;
    private final TencentProperties tencentProperties;

    @Autowired
    private AliCloudComponent aliCloudComponent;
    @Autowired
    private DoctorVisitSetService doctorVisitSetService;
    @Autowired
    private DoctorWelcomeMessageSetService doctorWelcomeMessageSetService;

    @Autowired
    private HospitalDepartmentService hospitalDepartmentService;
    @Autowired
    private DictService dictService;
    @Autowired
    private HospitalService hospitalService;

    @Override
    public PageResult<DoctorUser> list(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        Page<DoctorUser> setPage = new Page<>(page, limit);
        //列表排序规则 index 没有值排在后面， 已经认证通过的排在前面，然后按照index排列
        IPage<DoctorUser> iPage = this.baseMapper.selectDoctorUserPage(setPage, map);
        List<DoctorUser> doctorUserList = iPage.getRecords();
        if (org.springframework.util.CollectionUtils.isEmpty(doctorUserList)) {
            return new PageResult<>(iPage.getRecords(), iPage.getTotal());
        }
        return new PageResult<>(doctorUserList, iPage.getTotal());
    }

    @Override
    public DoctorUser selectMobile(String mobile) {
        QueryWrapper<DoctorUser> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile).eq("is_cancel", "0");
        return doctorUserMapper.selectOne(wrapper);
    }

    @Override
    public boolean sendCode(String mobile) {
        String code = RandomUtil.randomNumbers(6);
        Map<String, String> template = Map.of("code", code);
        boolean result = aliCloudComponent.sendSms(mobile, JSONUtil.toJson(template));
        if (result) {
            String key = "send_code_" + mobile;
            redisService.set(key, code, 180);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DoctorUserDto phoneLogin(String mobile, String code, HttpServletRequest request) {
        String sendCodeKey = "send_code_" + mobile;
        if (!redisService.exists(sendCodeKey)) {
            throw new BizException(com.aihoo.common.BizResultCode.SMS_CODE_EXPIRED);
        }
        String checkCode = (String) redisService.get(sendCodeKey);
        if (!code.equals(checkCode)) {
            throw new BizException(com.aihoo.common.BizResultCode.SMS_CODE_ERROR);
        }
        redisService.remove(sendCodeKey);

        QueryWrapper<DoctorUser> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile).eq("is_cancel", "0");
        DoctorUser doctorUser = doctorUserMapper.selectOne(wrapper);
        if (null == doctorUser) {
            throw new BizException(com.aihoo.common.BizResultCode.DOCTOR_MOBILE_NOT_BOUND);
        }
        if ("0".equals(doctorUser.getStatus())) {
            throw new BizException(com.aihoo.common.BizResultCode.DOCTOR_ACCOUNT_DISABLED);
        }
        if (!"PASS".equals(doctorUser.getIsAuth())) {
            throw new BizException(com.aihoo.common.BizResultCode.DOCTOR_ACCOUNT_NO_AUTH);
        }

        String oldToken = doctorUser.getToken();
        String accessToken = UUID.randomUUID().toString().replace("-", "");
        UpdateWrapper<DoctorUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("mobile", mobile);
        updateWrapper.eq("status", "1");
        updateWrapper.eq("is_auth", "PASS");
        updateWrapper.eq("is_cancel", "0");
        updateWrapper.set("token", accessToken);
        if (StringUtil.isBlank(doctorUser.getUserSig())) {
            String imUserId = String.format(ImUserPrefix.USER_ID_FORMAT, ImUserPrefix.DOCTOR, doctorUser.getId());
            String userSig = ImUtils.genUserSig(imUserId, null, tencentProperties.getSdkappid(), tencentProperties.getPrivstr());
            updateWrapper.set("user_sig", userSig);
        }
        doctorUserMapper.update(updateWrapper);

        UserAgentGetter userAgentGetter = new UserAgentGetter(request);
        DoctorUserLog loginLog = new DoctorUserLog();
        loginLog.setActionType("LOGIN");
        loginLog.setDoctorUserId(doctorUser.getId());
        loginLog.setOsName(userAgentGetter.getOS());
        loginLog.setIpAddress(userAgentGetter.getIpAddr());
        loginLog.setRemark("登陆成功");
        loginLog.setCity(AdderssUtils.getCityNameByTaoBaoAPI(userAgentGetter.getIpAddr()));
        doctorUserLogMapper.insert(loginLog);

        doctorUser = doctorUserMapper.selectOne(new QueryWrapper<DoctorUser>().eq("id", doctorUser.getId()));

        String redisKey = RedisConstant.DOCKER_LOGIN_KEY + accessToken;
        redisService.set(redisKey, doctorUser, RedisConstant.TOKEN_SURVIVE_TIME);
        if (StringUtil.isNotBlank(oldToken)) {
            redisService.remove(RedisConstant.DOCKER_LOGIN_KEY + oldToken);
        }
        return convert2Dto(doctorUser);
    }

    @Override
    public DoctorVisitSet getVisitSet() {
        String loginUserId = AuthUtil.getLoginUserId();
        return doctorVisitSetService.getOne(new LambdaQueryWrapper<DoctorVisitSet>()
                .eq(DoctorVisitSet::getDoctorUserId, loginUserId));
    }

    @Override
    public DoctorVisitSet setVisit(DoctorVisitSetRequest request) {
        String loginUserId = AuthUtil.getLoginUserId();
        DoctorVisitSet doctorVisitSet = doctorVisitSetService.getOne(new LambdaQueryWrapper<DoctorVisitSet>()
                .eq(DoctorVisitSet::getDoctorUserId, loginUserId));

        if (ObjectUtils.isEmpty(doctorVisitSet)) {
            doctorVisitSet = new DoctorVisitSet();
            doctorVisitSet.setDoctorUserId(loginUserId);
        }
        BeanUtils.copyProperties(request, doctorVisitSet);
        doctorVisitSetService.saveOrUpdate(doctorVisitSet);
        return doctorVisitSet;
    }

    @Override
    public DoctorWelcomeMessageSet getWelcomeMessage() {
        String loginUserId = AuthUtil.getLoginUserId();
        return doctorWelcomeMessageSetService.getOne(new LambdaQueryWrapper<DoctorWelcomeMessageSet>()
                .eq(DoctorWelcomeMessageSet::getDoctorUserId, loginUserId));
    }

    @Override
    public DoctorWelcomeMessageSet setWelcomeMessage(DoctorWelcomeMessageRequest request) {
        String loginUserId = AuthUtil.getLoginUserId();
        DoctorWelcomeMessageSet welcomeMessageSet = doctorWelcomeMessageSetService.getOne(new LambdaQueryWrapper<DoctorWelcomeMessageSet>()
                .eq(DoctorWelcomeMessageSet::getDoctorUserId, loginUserId));

        if (ObjectUtils.isEmpty(welcomeMessageSet)) {
            welcomeMessageSet = new DoctorWelcomeMessageSet();
            welcomeMessageSet.setDoctorUserId(loginUserId);
        }
        BeanUtils.copyProperties(request, welcomeMessageSet);
        doctorWelcomeMessageSetService.saveOrUpdate(welcomeMessageSet);
        return welcomeMessageSet;
    }

    @Override
    public DoctorUserDto detail(String id) {
        String loginUserId = AuthUtil.getLoginUserId();
        DoctorUser user = (loginUserId == null) ? doctorUserMapper.selectById(id)
                : doctorUserMapper.selectById(loginUserId);
        return convert2Dto(user);
    }

    @Override
    public DoctorUserDto loginUser(String mobile, HttpServletRequest request) {
        DoctorUser doctorUser = selectActiveByMobile(mobile);
        if (doctorUser == null) {
            return null;
        }
        String accessToken = UUID.randomUUID().toString().replace("-", "");
        LambdaUpdateWrapper<DoctorUser> updateWrapper = new LambdaUpdateWrapper<DoctorUser>()
                .eq(DoctorUser::getMobile, mobile)
                .eq(DoctorUser::getStatus, "1")
                .eq(DoctorUser::getIsAuth, "PASS")
                .eq(DoctorUser::getIsCancel, "0")
                .set(DoctorUser::getToken, accessToken);
        doctorUserMapper.update(null, updateWrapper);
        doctorUser.setToken(accessToken);
        return convert2Dto(doctorUser);
    }

    @Override
    public List<DoctorUser> doctorQuery(String name) {
        LambdaQueryWrapper<DoctorUser> queryWrapper = new LambdaQueryWrapper<DoctorUser>()
                .eq(DoctorUser::getIsCancel, "0")
                .eq(DoctorUser::getStatus, "1")
                .eq(DoctorUser::getIsAuth, "PASS")
                .like(StringUtil.isNotBlank(name), DoctorUser::getName, name);
        List<DoctorUser> doctorUserList = doctorUserMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(doctorUserList)) {
            return List.of();
        }
        return doctorUserList;
    }

    @Override
    public DoctorUserDetailsDto doctorDetails(String doctorId) {
        DoctorUser doctorUser = doctorUserMapper.selectById(doctorId);
        DoctorVisitSet doctorVisitSet = doctorVisitSetService.getOne(
                new LambdaQueryWrapper<DoctorVisitSet>().eq(DoctorVisitSet::getDoctorUserId, doctorUser.getId()));
        return DoctorUserDetailsDto.of(doctorUser, doctorVisitSet);
    }

    @Override
    public JSONArray hospitalDepartmentAll(String hospitalId) {
        JSONArray jsonArray = new JSONArray();
        QueryWrapper<HospitalDepartment> wrapper = new QueryWrapper<>();
        wrapper.eq("hospital_id", hospitalId);
        List<HospitalDepartment> list = hospitalDepartmentService.list(wrapper);
        if (org.springframework.util.CollectionUtils.isEmpty(list)) {
            return jsonArray;
        }
        list.forEach(s -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", s.getId());
            jsonObject.put("departCode", s.getDepartCode());
            jsonObject.put("departName", s.getDepartName());
            jsonArray.add(jsonObject);
        });
        return jsonArray;
    }

    @Override
    public String getNowWelcomeMessage(Long doctorUserId) {
        DoctorWelcomeMessageSet welcomeMessageSet = doctorWelcomeMessageSetService.getOne(
                new LambdaQueryWrapper<DoctorWelcomeMessageSet>()
                        .eq(DoctorWelcomeMessageSet::getDoctorUserId, doctorUserId));

        if (welcomeMessageSet == null) {
            return "欢迎找我在线复诊，认真填写主诉和问诊单有助于我了解您的病情，我会尽快阅读您的资料，请稍等。";
        }

        if (welcomeMessageSet.getIsAuto() == null || welcomeMessageSet.getIsAuto() != 1) {
            return "";
        }

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(welcomeMessageSet.getWelcomeMessage())) {
            return welcomeMessageSet.getWelcomeMessage();
        }

        return "欢迎找我在线复诊，认真填写主诉和问诊单有助于我了解您的病情，我会尽快阅读您的资料，请稍等。";
    }

    private DoctorUser selectActiveByMobile(String mobile) {
        return doctorUserMapper.selectOne(new LambdaQueryWrapper<DoctorUser>()
                .eq(DoctorUser::getMobile, mobile)
                .eq(DoctorUser::getIsCancel, "0")
                .eq(DoctorUser::getStatus, "1")
                .eq(DoctorUser::getIsAuth, "PASS"));
    }

    private DoctorUserDto convert2Dto(DoctorUser doctorUser) {
        if (doctorUser == null) {
            return null;
        }
        DoctorUserDto dto = new DoctorUserDto();
        BeanUtils.copyProperties(doctorUser, dto);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doctorUserAdd(DoctorUserAddRequestDto req, HttpServletRequest httpRequest) throws Exception {
        // 1. 校验身份证格式
        if (!IdentityCardUtils.isIdCard(req.getPapersNumbers().trim())) {
            throw new BizException("身份证格式不正确");
        }
        // 2. 校验手机号格式 + 唯一
        if (req.getMobile() == null || req.getMobile().length() != 11) {
            throw new BizException("请填写11位正确的手机号码");
        }
        Long mobileExists = doctorUserMapper.selectCount(new QueryWrapper<DoctorUser>()
                .eq("mobile", req.getMobile().trim())
                .eq("is_cancel", "0"));
        if (mobileExists != null && mobileExists > 0) {
            throw new BizException("该手机号已经注册");
        }
        // 3. 校验证件号唯一
        Long papersExists = doctorUserMapper.selectCount(new QueryWrapper<DoctorUser>()
                .eq("papers_numbers", req.getPapersNumbers().trim())
                .eq("is_cancel", "0"));
        if (papersExists != null && papersExists > 0) {
            throw new BizException("证件号码重复");
        }
        // 4. 校验医院存在
        Hospital hospital = hospitalService.getById(req.getHospitalId().trim());
        if (hospital == null) {
            throw new BizException("未查询到id对应医院名称 id :" + req.getHospitalId());
        }
        // 5. 校验科室存在
        String departName = hospitalDepartmentService.findDepartmentNameByCode(req.getDepartCode().trim());
        if (StringUtils.isEmpty(departName)) {
            throw new BizException("未查询到departCode对应科室名称");
        }
        // 6. 校验职称/人员类别/职务/证件 类型都能查到
        String officeHolderName = dictService.getDoctorNameByTypeAndCode(
                DictTypeEnum.DOCT_TITLE.getType(), req.getOfficeHolderCode().trim());
        if (StringUtils.isEmpty(officeHolderName)) {
            throw new BizException("未查询到officeHolderCode对应的职称");
        }
        String personTypeName = dictService.getDoctorNameByTypeAndCode(
                DictTypeEnum.RYLB.getType(), req.getPersonTypeCode().trim());
        if (StringUtils.isEmpty(personTypeName)) {
            throw new BizException("未找到编码对应的人员类别");
        }
        String positionName = dictService.getDoctorNameByTypeAndCode(
                DictTypeEnum.POSITION.getType(), req.getPositionCode().trim());
        if (StringUtils.isEmpty(positionName)) {
            throw new BizException("未找到对应的职务类型");
        }
        String papersName = dictService.getDoctorNameByTypeAndCode(
                DictTypeEnum.PAPERS.getType(), req.getPapersCode().trim());
        if (StringUtils.isEmpty(papersName)) {
            throw new BizException("未找到对应的证件类型");
        }

        // 7. 计算性别/年龄/生日
        Map<String, String> idInfo = IdentityCardUtils.getCarMessage(req.getPapersNumbers().trim());

        // 8. 组装 DoctorUser
        DoctorUser doctorUser = new DoctorUser();
        doctorUser.setCreateUserId(SecurityUtils.getLoginUserId());
        doctorUser.setMobile(req.getMobile().trim());
        doctorUser.setName(req.getName().trim());
        if (StringUtils.hasText(req.getHeadImg())) {
            doctorUser.setHeadImg(req.getHeadImg().trim());
        }
        if (StringUtils.hasText(req.getTag())) {
            doctorUser.setTag(req.getTag().trim());
        }
        if (StringUtils.hasText(req.getIndex())) {
            doctorUser.setIndex(req.getIndex());
        }
        if (StringUtils.hasText(req.getAchievement())) {
            doctorUser.setAchievement(req.getAchievement());
        }
        if (StringUtils.hasText(req.getDoctorType())) {
            doctorUser.setDoctorType(req.getDoctorType().trim());
        }
        doctorUser.setHospitalId(hospital.getId());
        doctorUser.setHospitalName(hospital.getHosName());
        doctorUser.setDepartId(req.getDepartId().trim());
        doctorUser.setDepartCode(req.getDepartCode().trim());
        doctorUser.setDepartName(departName);
        doctorUser.setOfficeHolderCode(req.getOfficeHolderCode().trim());
        doctorUser.setOfficeHolderName(officeHolderName);
        doctorUser.setBeGoodAtText(req.getBeGoodAtText().trim());
        doctorUser.setIntroductionText(req.getIntroductionText().trim());
        doctorUser.setPersonTypeCode(req.getPersonTypeCode().trim());
        doctorUser.setPersonTypeName(personTypeName);
        doctorUser.setPositionCode(req.getPositionCode().trim());
        doctorUser.setPositionName(positionName);
        doctorUser.setPapersCode(req.getPapersCode().trim());
        doctorUser.setPapersName(papersName);
        doctorUser.setPapersNumbers(req.getPapersNumbers().trim());
        doctorUser.setSex(idInfo.get("sex"));
        doctorUser.setAge(idInfo.get("age"));
        doctorUser.setBirthday(idInfo.get("birthday"));
        // 证书类字段
        doctorUser.setMedicalLicensePageOne(StringUtils.hasText(req.getMedicalLicensePageOne()) ? req.getMedicalLicensePageOne().trim() : null);
        doctorUser.setMedicalLicensePageTwo(StringUtils.hasText(req.getMedicalLicensePageTwo()) ? req.getMedicalLicensePageTwo().trim() : null);
        doctorUser.setMedicalLicenseNo(StringUtils.hasText(req.getMedicalLicenseNo()) ? req.getMedicalLicenseNo().trim() : null);
        doctorUser.setMedicalLicenseIssueDate(StringUtils.hasText(req.getMedicalLicenseIssueDate()) ? req.getMedicalLicenseIssueDate().trim() : null);
        doctorUser.setPracticeCertificatePageOne(StringUtils.hasText(req.getPracticeCertificatePageOne()) ? req.getPracticeCertificatePageOne().trim() : null);
        doctorUser.setPracticeCertificatePageTwo(StringUtils.hasText(req.getPracticeCertificatePageTwo()) ? req.getPracticeCertificatePageTwo().trim() : null);
        doctorUser.setPracticeCertificateNo(StringUtils.hasText(req.getPracticeCertificateNo()) ? req.getPracticeCertificateNo().trim() : null);
        doctorUser.setPracticeCertificateIssueDate(StringUtils.hasText(req.getPracticeCertificateIssueDate()) ? req.getPracticeCertificateIssueDate().trim() : null);
        if (StringUtils.hasText(req.getArea())) {
            doctorUser.setArea(req.getArea().trim());
        }

        // 9. 插入 DoctorUser
        doctorUserMapper.insert(doctorUser);

        // 10. 插入问诊设置（空记录）
        DoctorVisitSet visitSet = new DoctorVisitSet();
        visitSet.setDoctorUserId(doctorUser.getId());
        doctorVisitSetService.save(visitSet);

        // 11. 插入欢迎语设置（空记录）
        DoctorWelcomeMessageSet welcome = new DoctorWelcomeMessageSet();
        welcome.setDoctorUserId(doctorUser.getId());
        doctorWelcomeMessageSetService.save(welcome);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doctorUpdate(DoctorUserUpdateRequestDto req, HttpServletRequest httpRequest) throws Exception {
        if (StringUtils.isEmpty(req.getId())) {
            throw new BizException("请携带医生id");
        }
        DoctorUser doctorUser = doctorUserMapper.selectById(req.getId());
        if (doctorUser == null) {
            throw new BizException("医生不存在 id=" + req.getId());
        }

        // 手机号校验
        if (StringUtils.hasText(req.getMobile()) && !req.getMobile().equals(doctorUser.getMobile())) {
            if (req.getMobile().length() != 11) {
                throw new BizException("请填写11位正确的手机号码");
            }
            Long count = doctorUserMapper.selectCount(new QueryWrapper<DoctorUser>()
                    .eq("mobile", req.getMobile().trim())
                    .eq("is_cancel", "0")
                    .ne("id", req.getId()));
            if (count != null && count > 0) {
                throw new BizException("该手机号已经注册");
            }
            doctorUser.setMobile(req.getMobile().trim());
        }
        if (StringUtils.hasText(req.getName())) {
            doctorUser.setName(req.getName().trim());
        }
        if (StringUtils.hasText(req.getDepartCode())) {
            String departName = hospitalDepartmentService.findDepartmentNameByCode(req.getDepartCode().trim());
            doctorUser.setDepartCode(req.getDepartCode().trim());
            doctorUser.setDepartName(departName);
        }
        if (StringUtils.hasText(req.getDepartId())) {
            doctorUser.setDepartId(req.getDepartId());
        }
        if (StringUtils.hasText(req.getBeGoodAtText())) {
            doctorUser.setBeGoodAtText(req.getBeGoodAtText());
        }
        // tag 允许置空
        if (!StringUtils.hasText(req.getTag())) {
            doctorUser.setTag(null);
        } else {
            doctorUser.setTag(req.getTag());
        }
        if (StringUtils.hasText(req.getIndex())) {
            doctorUser.setIndex(req.getIndex());
        }
        if (StringUtils.hasText(req.getIntroductionText())) {
            doctorUser.setIntroductionText(req.getIntroductionText());
        }
        if (StringUtils.hasText(req.getHeadImg())) {
            doctorUser.setHeadImg(req.getHeadImg());
        }
        if (StringUtils.hasText(req.getHospitalId())) {
            Hospital hospital = hospitalService.getById(req.getHospitalId().trim());
            if (hospital == null) {
                throw new BizException("未查询到id对应医院名称 id :" + req.getHospitalId());
            }
            doctorUser.setHospitalId(hospital.getId());
            doctorUser.setHospitalName(hospital.getHosName());
        }
        if (StringUtils.hasText(req.getOfficeHolderCode())) {
            String officeHolderName = dictService.getDoctorNameByTypeAndCode(
                    DictTypeEnum.DOCT_TITLE.getType(), req.getOfficeHolderCode().trim());
            if (StringUtils.isEmpty(officeHolderName)) {
                throw new BizException("未查询到officeHolderCode对应的职称");
            }
            doctorUser.setOfficeHolderName(officeHolderName);
            doctorUser.setOfficeHolderCode(req.getOfficeHolderCode().trim());
        }
        if (StringUtils.hasText(req.getPersonTypeCode())) {
            String personTypeName = dictService.getDoctorNameByTypeAndCode(
                    DictTypeEnum.RYLB.getType(), req.getPersonTypeCode().trim());
            if (StringUtils.isEmpty(personTypeName)) {
                throw new BizException("未找到编码对应的人员类别");
            }
            doctorUser.setPersonTypeName(personTypeName);
            doctorUser.setPersonTypeCode(req.getPersonTypeCode().trim());
        }
        if (StringUtils.hasText(req.getPositionCode())) {
            String positionName = dictService.getDoctorNameByTypeAndCode(
                    DictTypeEnum.POSITION.getType(), req.getPositionCode().trim());
            if (StringUtils.isEmpty(positionName)) {
                throw new BizException("未找到对应的职务类型");
            }
            doctorUser.setPositionName(positionName);
            doctorUser.setPositionCode(req.getPositionCode().trim());
        }
        if (StringUtils.hasText(req.getPapersCode())) {
            String papersName = dictService.getDoctorNameByTypeAndCode(
                    DictTypeEnum.PAPERS.getType(), req.getPapersCode().trim());
            if (StringUtils.isEmpty(papersName)) {
                throw new BizException("未找到证件对应的名称");
            }
            doctorUser.setPapersName(papersName);
            doctorUser.setPapersCode(req.getPapersCode().trim());
            // 证件号码也校验
            if (StringUtils.hasText(req.getPapersNumbers())) {
                if (!IdentityCardUtils.isIdCard(req.getPapersNumbers())) {
                    throw new BizException("身份证格式不正确");
                }
                if (!req.getPapersNumbers().equals(doctorUser.getPapersNumbers())) {
                    Long exists = doctorUserMapper.selectCount(new QueryWrapper<DoctorUser>()
                            .eq("papers_numbers", req.getPapersNumbers().trim())
                            .eq("is_cancel", "0")
                            .ne("id", req.getId()));
                    if (exists != null && exists > 0) {
                        throw new BizException("证件号码重复");
                    }
                }
                Map<String, String> idInfo = IdentityCardUtils.getCarMessage(req.getPapersNumbers());
                doctorUser.setSex(idInfo.get("sex"));
                doctorUser.setAge(idInfo.get("age"));
                doctorUser.setBirthday(idInfo.get("birthday"));
                doctorUser.setPapersNumbers(req.getPapersNumbers().trim());
            }
        }
        if (StringUtils.hasText(req.getAchievement())) {
            doctorUser.setAchievement(req.getAchievement());
        }
        if (StringUtils.hasText(req.getDoctorType())) {
            doctorUser.setDoctorType(req.getDoctorType().trim());
        }
        // 证书
        if (StringUtils.hasText(req.getMedicalLicensePageOne())) doctorUser.setMedicalLicensePageOne(req.getMedicalLicensePageOne().trim());
        if (StringUtils.hasText(req.getMedicalLicensePageTwo())) doctorUser.setMedicalLicensePageTwo(req.getMedicalLicensePageTwo().trim());
        if (StringUtils.hasText(req.getMedicalLicenseNo())) doctorUser.setMedicalLicenseNo(req.getMedicalLicenseNo().trim());
        if (StringUtils.hasText(req.getMedicalLicenseIssueDate())) doctorUser.setMedicalLicenseIssueDate(req.getMedicalLicenseIssueDate().trim());
        if (StringUtils.hasText(req.getPracticeCertificatePageOne())) doctorUser.setPracticeCertificatePageOne(req.getPracticeCertificatePageOne().trim());
        if (StringUtils.hasText(req.getPracticeCertificatePageTwo())) doctorUser.setPracticeCertificatePageTwo(req.getPracticeCertificatePageTwo().trim());
        if (StringUtils.hasText(req.getPracticeCertificateNo())) doctorUser.setPracticeCertificateNo(req.getPracticeCertificateNo().trim());
        if (StringUtils.hasText(req.getPracticeCertificateIssueDate())) doctorUser.setPracticeCertificateIssueDate(req.getPracticeCertificateIssueDate().trim());
        if (StringUtils.hasText(req.getArea())) doctorUser.setArea(req.getArea().trim());

        doctorUserMapper.updateById(doctorUser);
    }

    @Override
    public boolean enableDisable(DoctorEnableDisableRequestDto req) {
        DoctorUser update = new DoctorUser();
        update.setId(req.getId());
        update.setStatus(req.getStatus());
        int rows = doctorUserMapper.updateById(update);
        if ("0".equals(req.getStatus())) {
            DoctorUser current = doctorUserMapper.selectById(req.getId());
            if (current != null && StringUtil.isNotBlank(current.getToken())) {
                redisService.remove(RedisConstant.DOCKER_LOGIN_KEY + current.getToken());
            }
        }
        return rows > 0;
    }
}
