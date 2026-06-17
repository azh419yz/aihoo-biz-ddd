package com.aihoo.domain.doctor.service;

import com.aihoo.domain.doctor.dto.DoctorUserDetailsDto;
import com.aihoo.domain.doctor.dto.DoctorUserDto;
import com.aihoo.domain.doctor.dto.DoctorVisitSetRequest;
import com.aihoo.domain.doctor.dto.DoctorWelcomeMessageRequest;
import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.entity.DoctorVisitSet;
import com.aihoo.domain.doctor.entity.DoctorWelcomeMessageSet;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 医生用户表 服务接口。
 *
 * <p>实现自 patient-api 的 DoctorUserV2Controller（已完整迁移）：
 * <ul>
 *   <li>{@link #doctorQuery(String)}</li>
 *   <li>{@link #doctorDetails(String)}</li>
 *   <li>{@link #getNowWelcomeMessage(Long)}</li>
 * </ul>
 *
 * <p>新增自 doctor-api 的 ChuangLanController：
 * <ul>
 *   <li>{@link #loginUser(String, HttpServletRequest)}</li>
 * </ul>
 *
 * <p>新增自 doctor-api 的 DoctorUserV2Controller（2026-06-17 阶段 2）：
 * <ul>
 *   <li>{@link #selectMobile(String)}</li>
 *   <li>{@link #sendCode(String)}</li>
 *   <li>{@link #phoneLogin(String, String, HttpServletRequest)}</li>
 *   <li>{@link #getVisitSet()}</li>
 *   <li>{@link #setVisit(DoctorVisitSetRequest)}</li>
 *   <li>{@link #getWelcomeMessage()}</li>
 *   <li>{@link #setWelcomeMessage(DoctorWelcomeMessageRequest)}</li>
 *   <li>{@link #detail(String)}</li>
 * </ul>
 */
public interface DoctorUserService extends IService<DoctorUser> {

    /** 医生查询（patient-api: DoctorUserV2Controller.list） */
    List<DoctorUser> doctorQuery(String name);

    /** 医生详情（patient-api: DoctorUserV2Controller.doctorDetails） */
    DoctorUserDetailsDto doctorDetails(String id);

    /** 查询当前时间段医生的欢迎消息（patient-api: DoctorUserV2Controller.getNowWelcomeMessageSet） */
    String getNowWelcomeMessage(Long doctorUserId);

    /** 创蓝登录（doctor-api: ChuangLanController.login） */
    DoctorUserDto loginUser(String mobile, HttpServletRequest request);

    /** 根据手机号查询（doctor-api: DoctorUserV2Controller.sendCode） */
    DoctorUser selectMobile(String mobile);

    /** 发送短信验证码（doctor-api: DoctorUserV2Controller.sendCode） */
    boolean sendCode(String mobile);

    /** 手机验证码登录（doctor-api: DoctorUserV2Controller.login） */
    DoctorUserDto phoneLogin(String mobile, String code, HttpServletRequest request);

    /** 查询问诊设置（doctor-api: DoctorUserV2Controller.getVisitSet） */
    DoctorVisitSet getVisitSet();

    /** 设置问诊设置（doctor-api: DoctorUserV2Controller.setVisit） */
    DoctorVisitSet setVisit(DoctorVisitSetRequest request);

    /** 查询欢迎语设置记录（doctor-api: DoctorUserV2Controller.getWelcomeMessage） */
    DoctorWelcomeMessageSet getWelcomeMessage();

    /** 设置欢迎语（doctor-api: DoctorUserV2Controller.setWelcomeMessage） */
    DoctorWelcomeMessageSet setWelcomeMessage(DoctorWelcomeMessageRequest request);

    /** 医生详情（doctor-api: DoctorUserV2Controller.detail） */
    DoctorUserDto detail(String id);
}
