package com.aihoo.domain.doctor.service.impl;

import com.aihoo.domain.doctor.dto.DoctorUserDetailsDto;
import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.entity.DoctorVisitSet;
import com.aihoo.domain.doctor.entity.DoctorWelcomeMessageSet;
import com.aihoo.domain.doctor.mapper.DoctorUserMapper;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.doctor.service.DoctorVisitSetService;
import com.aihoo.domain.doctor.service.DoctorWelcomeMessageSetService;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 医生用户表 服务实现（迁移自 patient-api 的 DoctorUserServiceImpl）。
 *
 * @author mcp
 * @since 2020-09-15
 */
@Service
@RequiredArgsConstructor
public class DoctorUserServiceImpl extends ServiceImpl<DoctorUserMapper, DoctorUser> implements DoctorUserService {
    private final DoctorUserMapper doctorUserMapper;
    private final DoctorVisitSetService doctorVisitSetService;
    private final DoctorWelcomeMessageSetService doctorWelcomeMessageSetService;

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
}
