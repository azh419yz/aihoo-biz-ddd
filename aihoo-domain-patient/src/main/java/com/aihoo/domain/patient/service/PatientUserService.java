package com.aihoo.domain.patient.service;

import com.aihoo.domain.patient.entity.PatientUser;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PatientUserService extends IService<PatientUser> {
    /**
     * 微信登录，返回当前患者 entity（不聚合 orderCount/hosSickCount/visitCount，由 api-patient controller 聚合）。
     * 2026-06-18 拆解循环依赖：service 不再返回跨域 Vo。
     */
    PatientUser weChatLogin(String code);

    void updatePhone(String mobile);

    void updatePhone(String mobile, String code);

    /**
     * 查询当前登录用户 entity（不聚合 orderCount/hosSickCount/visitCount，由 api-patient controller 聚合）。
     */
    PatientUser queryPatientUserById();

    boolean sendCode(String mobile);

    boolean checkPhone(String mobile);

    void bindWeChatPhone(String code);

    boolean checkWeChatPhone(String code);

    /**
     * 授权隐私协议（设置 isAuth=PASS），返回更新后 entity（allowPrivacyPolicy 字段由 controller 计算）。
     */
    PatientUser allowPrivacyPolicy();
}