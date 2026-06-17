package com.aihoo.domain.patient.service.impl;

import cn.hutool.core.date.DateUtil;
import com.aihoo.alicloud.AliCloudComponent;
import com.aihoo.constant.ImUserPrefix;
import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.patient.dto.HosSickDto;
import com.aihoo.domain.patient.dto.HosVisitDto;
import com.aihoo.domain.patient.dto.SaveUpdateHosSickDto;
import com.aihoo.domain.patient.entity.HosSick;
import com.aihoo.domain.patient.entity.HosSickHealthRecords;
import com.aihoo.domain.patient.mapper.HosSickHealthRecordsMapper;
import com.aihoo.domain.patient.mapper.HosSickMapper;
import com.aihoo.domain.patient.service.HosSickService;
import com.aihoo.domain.sys.oss.OssComponent;
import com.aihoo.domain.visit.entity.HosVisit;
import com.aihoo.domain.visit.service.HosPrescriptionService;
import com.aihoo.domain.visit.service.HosVisitService;
import com.aihoo.properties.TencentProperties;
import com.aihoo.security.AuthUtil;
import com.aihoo.util.ImUtils;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 就诊人 service 实现（迁自 patient-api 的 HosSickServiceImpl）。
 */
@Service
public class HosSickServiceImpl extends ServiceImpl<HosSickMapper, HosSick> implements HosSickService {

    private final AliCloudComponent aliCloudComponent;
    private final HosVisitService hosVisitService;
    private final OssComponent ossComponent;
    private final TencentProperties tencentProperties;
    private final HosSickHealthRecordsMapper hosSickHealthRecordsMapper;
    private final HosPrescriptionService hosPrescriptionService;
    private final DoctorUserService doctorUserService;

    public HosSickServiceImpl(AliCloudComponent aliCloudComponent,
                              HosVisitService hosVisitService,
                              OssComponent ossComponent,
                              TencentProperties tencentProperties,
                              HosSickHealthRecordsMapper hosSickHealthRecordsMapper,
                              HosPrescriptionService hosPrescriptionService,
                              DoctorUserService doctorUserService) {
        this.aliCloudComponent = aliCloudComponent;
        this.hosVisitService = hosVisitService;
        this.ossComponent = ossComponent;
        this.tencentProperties = tencentProperties;
        this.hosSickHealthRecordsMapper = hosSickHealthRecordsMapper;
        this.hosPrescriptionService = hosPrescriptionService;
        this.doctorUserService = doctorUserService;
    }

    public List<HosSickDto> queryHosSickByPatientUserId() {
        String loginUserId = AuthUtil.getLoginUserId();
        List<HosSick> hosSicks = baseMapper.selectList(new LambdaQueryWrapper<HosSick>()
                .eq(HosSick::getPatientUserId, loginUserId));
        if (CollectionUtils.isEmpty(hosSicks)) {
            return List.of();
        }
        return hosSicks.stream().map(hosSick -> {
            HosSickDto dto = HosSickDto.fromEntity(hosSick);
            dto.setAvatar(ossComponent.getUrl(getAvatarPath(hosSick.getSex(), hosSick.getAge())));

            if (StringUtil.isBlank(dto.getImUserSig())) {
                String imUserId = String.format(ImUserPrefix.HOS_SICK_USER_ID_FORMAT, ImUserPrefix.PATIENT, hosSick.getPatientUserId(), hosSick.getId());
                String userSig = ImUtils.genUserSig(imUserId, null, tencentProperties.getSdkappid(), tencentProperties.getPrivstr());
                setImUserSig(hosSick.getId(), imUserId, userSig);

                dto.setImUserId(imUserId);
                dto.setImUserSig(userSig);
            }
            return dto;
        }).toList();
    }

    @Override
    public long countHosSickByPatientUserId(String patientUserId) {
        return count(new LambdaQueryWrapper<HosSick>().eq(HosSick::getPatientUserId, patientUserId));
    }

    @Override
    public List<HosSickDto> queryHosSickByDoctorId(String doctorId) {
        List<HosSickDto> dtos = queryHosSickByPatientUserId();
        if (StringUtil.isNotBlank(doctorId)) {
            dtos.forEach(dto -> {
                HosVisit latestHosVisit = hosVisitService.latestHosVisit(dto.getId(), doctorId);
                if (latestHosVisit == null) return;
                String status = latestHosVisit.getStatus();
                if ("UNSUBMITTED".equals(status) || "SUBMITTED".equals(status) || "STARTED".equals(status)) {
                    dto.setStatus(latestHosVisit.getPatientUserId().equals(AuthUtil.getLoginUserId()) ?
                            "问诊中" : "其他家庭成员账号问诊中");
                    dto.setImGroupId(latestHosVisit.getImGroupId());
                }
            });
        }
        return dtos;
    }

    @Override
    public HosSickDto queryHosSickByHosSickId(String hosSickId) {
        HosSick hosSick = baseMapper.selectById(hosSickId);
        HosSickDto dto = HosSickDto.fromEntity(hosSick);
        dto.setAvatar(ossComponent.getUrl(getAvatarPath(hosSick.getSex(), hosSick.getAge())));

        if (StringUtil.isBlank(dto.getImUserSig())) {
            String imUserId = String.format(ImUserPrefix.HOS_SICK_USER_ID_FORMAT, ImUserPrefix.PATIENT, hosSick.getPatientUserId(), hosSick.getId());
            String userSig = ImUtils.genUserSig(imUserId, null, tencentProperties.getSdkappid(), tencentProperties.getPrivstr());
            setImUserSig(hosSick.getId(), imUserId, userSig);

            dto.setImUserId(imUserId);
            dto.setImUserSig(userSig);
        }

        QueryWrapper<HosSickHealthRecords> healthRecordWrapper = new QueryWrapper<>();
        healthRecordWrapper.eq("hos_sick_id", hosSickId);
        List<HosSickHealthRecords> healthRecords = hosSickHealthRecordsMapper.selectList(healthRecordWrapper);
        HosSickHealthRecords healthRecord = healthRecords.isEmpty() ? null : healthRecords.get(0);

        dto.setId(hosSick.getId());
        dto.setName(hosSick.getName());
        dto.setIdCard(hosSick.getIdCard());
        dto.setSex(hosSick.getSex());
        dto.setAge(hosSick.getAge());
        dto.setMobile(hosSick.getMobile());
        if (healthRecord != null) {
            dto.setArea(healthRecord.getArea());
            dto.setAreaName(healthRecord.getAreaName());
            dto.setHeight(healthRecord.getHeight());
            dto.setWeight(healthRecord.getWeight());
            dto.setPastHistory(healthRecord.getPastHistory());
            dto.setAllergyHistory(healthRecord.getAllergyHistory());
            dto.setIdCardVerify(healthRecord.getIdCardVerify());
            if (StringUtils.isNotEmpty(healthRecord.getFaceImages()))
                dto.setFaceImages(Lists.newArrayList(healthRecord.getFaceImages().split(",")));
            if (StringUtils.isNotEmpty(healthRecord.getTongueImages()))
                dto.setTongueImages(Lists.newArrayList(healthRecord.getTongueImages().split(",")));
            if (StringUtils.isNotEmpty(healthRecord.getMedicalRecordImages()))
                dto.setMedicalRecordImages(Lists.newArrayList(healthRecord.getMedicalRecordImages().split(",")));
        }

        QueryWrapper<HosVisit> visitWrapper = new QueryWrapper<>();
        visitWrapper.eq("hos_sick_id", hosSick.getId());
        visitWrapper.orderByDesc("create_time");
        List<HosVisit> visitList = hosVisitService.list(visitWrapper);

        List<HosVisitDto> visitDtos = visitList.stream().map(visit -> {
            HosVisitDto visitDto = new HosVisitDto();
            BeanUtils.copyProperties(visit, visitDto);
            visitDto.setCreateTime(visit.getCreateTime());
            visitDto.setContent(visit.getContent());
            visitDto.setImGroupId("GROUP_" + visit.getOrderNum());
            visitDto.setHosPrescriptions(hosPrescriptionService.listByVisitMdtNum(visit.getOrderNum()));
            if (StringUtils.isNotEmpty(visit.getDoctorUserId())) {
                DoctorUser doctor = doctorUserService.getById(visit.getDoctorUserId());
                if (doctor != null) {
                    visitDto.setDoctorName(doctor.getName());
                    visitDto.setDoctorHeadImg(doctor.getHeadImg());
                }
            }
            return visitDto;
        }).toList();
        dto.setVisits(visitDtos);
        dto.setAvatar(ossComponent.getUrl(getAvatarPath(hosSick.getSex(), hosSick.getAge())));

        return dto;
    }

    @Override
    public SaveUpdateHosSickDto validateRequest(SaveUpdateHosSickDto request) {
        String idCard = request.getIdCard();
        if (!aliCloudComponent.verifyIdentity(request.getName(), idCard)) {
            throw new RuntimeException("身份证不合法");
        }
        request.setSex((idCard.charAt(16) % 2 == 0) ? "0" : "1");
        request.setBirthday(DateUtil.parse(idCard.substring(6, 14), "yyyyMMdd").toJdkDate());
        return request;
    }

    @Override
    public int removeHosSick(String hosSickId) {
        return baseMapper.deleteById(hosSickId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HosSickDto saveHosSick(SaveUpdateHosSickDto request) {
        String loginUserId = AuthUtil.getLoginUserId();
        long count = baseMapper.selectCount(new LambdaQueryWrapper<HosSick>().eq(HosSick::getPatientUserId, loginUserId));
        if (count >= 5) {
            throw new RuntimeException("已经创建就诊人超过5个");
        }

        String idCard = request.getIdCard();
        if (StringUtil.isNotBlank(idCard)) {
            HosSick existingSick = baseMapper.selectOne(new LambdaQueryWrapper<HosSick>()
                    .eq(HosSick::getPatientUserId, loginUserId)
                    .eq(HosSick::getIdCard, idCard));
            if (existingSick != null) {
                throw new RuntimeException("该身份证号已存在，请勿重复添加");
            }
        }

        HosSick hosSick = new HosSick();
        BeanUtils.copyProperties(request, hosSick);
        hosSick.setPatientUserId(loginUserId);
        if (null != hosSick.getBirthday()) {
            hosSick.setAge(String.valueOf(DateUtil.ageOfNow(hosSick.getBirthday())));
        }

        baseMapper.insert(hosSick);

        String imUserId = String.format(ImUserPrefix.HOS_SICK_USER_ID_FORMAT, ImUserPrefix.PATIENT, hosSick.getPatientUserId(), hosSick.getId());
        String userSig = ImUtils.genUserSig(imUserId, null, tencentProperties.getSdkappid(), tencentProperties.getPrivstr());
        setImUserSig(hosSick.getId(), imUserId, userSig);

        HosSickDto dto = HosSickDto.fromEntity(hosSick);
        dto.setAvatar(ossComponent.getUrl(getAvatarPath(hosSick.getSex(), hosSick.getAge())));
        dto.setImUserId(imUserId);
        dto.setImUserSig(userSig);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HosSickDto updateHosSick(SaveUpdateHosSickDto request) {
        HosSick hosSick = baseMapper.selectById(request.getId());
        if (Objects.isNull(hosSick)) {
            throw new RuntimeException("就诊人不存在");
        }

        String newIdCard = request.getIdCard();
        if (StringUtil.isNotBlank(newIdCard) && !newIdCard.equals(hosSick.getIdCard())) {
            String loginUserId = AuthUtil.getLoginUserId();
            HosSick existingSick = baseMapper.selectOne(new LambdaQueryWrapper<HosSick>()
                    .eq(HosSick::getPatientUserId, loginUserId)
                    .eq(HosSick::getIdCard, newIdCard)
                    .ne(HosSick::getId, request.getId()));
            if (existingSick != null) {
                throw new RuntimeException("该身份证号已存在，请勿重复添加");
            }
        }

        LambdaUpdateWrapper<HosSick> updateWrapper = new LambdaUpdateWrapper<HosSick>()
                .eq(HosSick::getId, hosSick.getId())
                .set(StringUtil.isNotBlank(request.getName()), HosSick::getName, request.getName())
                .set(StringUtil.isNotBlank(request.getIdCard()), HosSick::getIdCard, request.getIdCard())
                .set(StringUtil.isNotBlank(request.getSex()), HosSick::getSex, request.getSex())
                .set(!Objects.isNull(request.getBirthday()), HosSick::getBirthday, request.getBirthday());
        baseMapper.update(updateWrapper);

        hosSick = baseMapper.selectById(request.getId());
        HosSickDto dto = HosSickDto.fromEntity(hosSick);
        dto.setAvatar(ossComponent.getUrl(getAvatarPath(hosSick.getSex(), hosSick.getAge())));

        return dto;
    }

    @Override
    public void setImUserSig(String hosSickId, String imUserId, String userSig) {
        LambdaUpdateWrapper<HosSick> updateWrapper = new LambdaUpdateWrapper<HosSick>()
                .eq(HosSick::getId, hosSickId)
                .set(HosSick::getImUserId, imUserId)
                .set(HosSick::getImUserSig, userSig);
        baseMapper.update(updateWrapper);
    }

    private String getAvatarPath(String sex, String ageStr) {
        String genderPrefix = "1".equals(sex) ? "M" : "W";
        int age = 0;
        try {
            if (StringUtil.isNotBlank(ageStr)) {
                String numericAge = ageStr.replaceAll("\\D+", "");
                if (StringUtil.isNotBlank(numericAge)) {
                    age = Integer.parseInt(numericAge);
                }
            }
        } catch (Exception ignore) {
        }

        String ageSuffix;
        if (age <= 6) {
            ageSuffix = "1";
        } else if (age <= 20) {
            ageSuffix = "2";
        } else if (age <= 60) {
            ageSuffix = "3";
        } else {
            ageSuffix = "4";
        }

        return "patient_aihoo/avatar/" + genderPrefix + ageSuffix + ".jpg";
    }
}