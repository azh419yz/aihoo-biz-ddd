package com.aihoo.domain.patient.service.impl;

import cn.hutool.core.date.DateUtil;
import com.aihoo.alicloud.AliCloudComponent;
import com.aihoo.constant.ImUserPrefix;
import com.aihoo.domain.patient.dto.HosSickDto;
import com.aihoo.domain.patient.dto.SaveUpdateHosSickDto;
import com.aihoo.domain.patient.entity.HosSick;
import com.aihoo.domain.patient.entity.HosSickHealthRecords;
import com.aihoo.domain.patient.mapper.HosSickMapper;
import com.aihoo.domain.patient.service.HosSickHealthRecordsService;
import com.aihoo.domain.patient.service.HosSickService;
import com.aihoo.domain.sys.oss.OssComponent;
import com.aihoo.properties.TencentProperties;
import com.aihoo.security.AuthUtil;
import com.aihoo.util.AvatarUtil;
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

@Service
public class HosSickServiceImpl extends ServiceImpl<HosSickMapper, HosSick> implements HosSickService {

    private final AliCloudComponent aliCloudComponent;
    private final OssComponent ossComponent;
    private final TencentProperties tencentProperties;
    private final HosSickHealthRecordsService healthRecordsService;

    public HosSickServiceImpl(AliCloudComponent aliCloudComponent,
                              OssComponent ossComponent,
                              TencentProperties tencentProperties,
                              HosSickHealthRecordsService healthRecordsService) {
        this.aliCloudComponent = aliCloudComponent;
        this.ossComponent = ossComponent;
        this.tencentProperties = tencentProperties;
        this.healthRecordsService = healthRecordsService;
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
            dto.setAvatar(ossComponent.getUrl(AvatarUtil.getAvatarPath(hosSick.getSex(), hosSick.getAge())));

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

        return queryHosSickByPatientUserId();
    }

    @Override
    public List<HosSick> listBySickIds(List<Long> sickIds) {
        if (sickIds == null || sickIds.isEmpty()) {
            return List.of();
        }
        return baseMapper.selectBatchIds(sickIds);
    }

    @Override
    public List<HosSickDto> patientListBySickIds(List<String> sickIds, String sickName) {
        if (sickIds == null || sickIds.isEmpty()) {
            return List.of();
        }
        String likeName = StringUtil.isBlank(sickName) ? "%%" : "%" + sickName + "%";

        List<HosSick> hosSicks = baseMapper.selectList(new LambdaQueryWrapper<HosSick>()
                .in(HosSick::getId, sickIds)
                .like(HosSick::getName, likeName)
                .eq(HosSick::getIsDelete, "0"));

        return hosSicks.stream().map(HosSickDto::fromEntity).toList();
    }

    @Override
    public HosSickDto patientMsg(String id) {
        HosSick hosSick = baseMapper.selectById(id);
        if (hosSick == null) {
            return null;
        }
        return HosSickDto.fromEntity(hosSick);
    }

    @Override
    public HosSickDto queryHosSickByHosSickId(String hosSickId) {
        HosSick hosSick = baseMapper.selectById(hosSickId);
        HosSickDto dto = HosSickDto.fromEntity(hosSick);
        dto.setAvatar(ossComponent.getUrl(AvatarUtil.getAvatarPath(hosSick.getSex(), hosSick.getAge())));

        if (StringUtil.isBlank(dto.getImUserSig())) {
            String imUserId = String.format(ImUserPrefix.HOS_SICK_USER_ID_FORMAT, ImUserPrefix.PATIENT, hosSick.getPatientUserId(), hosSick.getId());
            String userSig = ImUtils.genUserSig(imUserId, null, tencentProperties.getSdkappid(), tencentProperties.getPrivstr());
            setImUserSig(hosSick.getId(), imUserId, userSig);

            dto.setImUserId(imUserId);
            dto.setImUserSig(userSig);
        }

        QueryWrapper<HosSickHealthRecords> healthRecordWrapper = new QueryWrapper<>();
        healthRecordWrapper.eq("hos_sick_id", hosSickId);
        List<HosSickHealthRecords> healthRecords = healthRecordsService.list(healthRecordWrapper);
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

        dto.setAvatar(ossComponent.getUrl(AvatarUtil.getAvatarPath(hosSick.getSex(), hosSick.getAge())));

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
        dto.setAvatar(ossComponent.getUrl(AvatarUtil.getAvatarPath(hosSick.getSex(), hosSick.getAge())));
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
        dto.setAvatar(ossComponent.getUrl(AvatarUtil.getAvatarPath(hosSick.getSex(), hosSick.getAge())));

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

}