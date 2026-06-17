package com.aihoo.domain.doctor.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.aihoo.alicloud.AliCloudComponent;
import com.aihoo.constant.ImUserPrefix;
import com.aihoo.domain.doctor.dto.DoctorUserDetailsDto;
import com.aihoo.domain.doctor.dto.DoctorUserDto;
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
import com.aihoo.exception.BizException;
import com.aihoo.properties.TencentProperties;
import com.aihoo.redis.RedisConstant;
import com.aihoo.redis.RedisService;
import com.aihoo.security.AuthUtil;
import com.aihoo.util.AdderssUtils;
import com.aihoo.util.ImUtils;
import com.aihoo.util.JSONUtil;
import com.aihoo.util.StringUtil;
import com.aihoo.util.UserAgentGetter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
