package com.aihoo.domain.patient.service;

import com.aihoo.domain.patient.dto.PatientUserVo;
import com.aihoo.domain.patient.entity.PatientUser;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PatientUserService extends IService<PatientUser> {
    PatientUserVo weChatLogin(String code);

    void updatePhone(String mobile);

    void updatePhone(String mobile, String code);

    PatientUserVo queryPatientUserById();

    boolean sendCode(String mobile);

    boolean checkPhone(String mobile);

    void bindWeChatPhone(String code);

    boolean checkWeChatPhone(String code);

    PatientUserVo allowPrivacyPolicy();
}
