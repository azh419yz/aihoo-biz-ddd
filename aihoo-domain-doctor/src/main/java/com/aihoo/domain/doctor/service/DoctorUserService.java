package com.aihoo.domain.doctor.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.doctor.dto.DoctorEnableDisableRequestDto;
import com.aihoo.domain.doctor.dto.DoctorUserAddRequestDto;
import com.aihoo.domain.doctor.dto.DoctorUserDetailsDto;
import com.aihoo.domain.doctor.dto.DoctorUserDto;
import com.aihoo.domain.doctor.dto.DoctorUserUpdateRequestDto;
import com.aihoo.domain.doctor.dto.DoctorVisitSetDto;
import com.aihoo.domain.doctor.dto.DoctorWelcomeMessageDto;
import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.entity.DoctorVisitSet;
import com.aihoo.domain.doctor.entity.DoctorWelcomeMessageSet;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

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
 *   <li>{@link #setVisit(DoctorVisitSetDto)}</li>
 *   <li>{@link #getWelcomeMessage()}</li>
 *   <li>{@link #setWelcomeMessage(DoctorWelcomeMessageDto)}</li>
 *   <li>{@link #detail(String)}</li>
 * </ul>
 *
 * <p>新增自 admin-api 的 DoctorUserController（2026-06-18）：
 * <ul>
 *   <li>{@link #doctorUserAdd(DoctorUserAddRequestDto, HttpServletRequest)} — 仅基础数据 + DoctorVisitSet + DoctorWelcomeMessageSet</li>
 *   <li>{@link #doctorUpdate(DoctorUserUpdateRequestDto, HttpServletRequest)} — 仅基础数据</li>
 * </ul>
 */
public interface DoctorUserService extends IService<DoctorUser> {

    PageResult<DoctorUser> list(Map<String, Object> map);

    /**
     * 医生查询（patient-api: DoctorUserV2Controller.list）
     */
    List<DoctorUser> doctorQuery(String name);

    /**
     * 医生详情（patient-api: DoctorUserV2Controller.doctorDetails）
     */
    DoctorUserDetailsDto doctorDetails(String id);

    JSONArray hospitalDepartmentAll(String hospitalId);

    /**
     * 查询当前时间段医生的欢迎消息（patient-api: DoctorUserV2Controller.getNowWelcomeMessageSet）
     */
    String getNowWelcomeMessage(Long doctorUserId);

    /**
     * 创蓝登录（doctor-api: ChuangLanController.login）
     */
    DoctorUserDto loginUser(String mobile, HttpServletRequest request);

    /**
     * 根据手机号查询（doctor-api: DoctorUserV2Controller.sendCode）
     */
    DoctorUser selectMobile(String mobile);

    /**
     * 发送短信验证码（doctor-api: DoctorUserV2Controller.sendCode）
     */
    boolean sendCode(String mobile);

    /**
     * 手机验证码登录（doctor-api: DoctorUserV2Controller.login）
     */
    DoctorUserDto phoneLogin(String mobile, String code, HttpServletRequest request);

    /**
     * 查询问诊设置（doctor-api: DoctorUserV2Controller.getVisitSet）
     */
    DoctorVisitSet getVisitSet();

    /**
     * 设置问诊设置（doctor-api: DoctorUserV2Controller.setVisit）
     */
    DoctorVisitSet setVisit(DoctorVisitSetDto request);

    /**
     * 查询欢迎语设置记录（doctor-api: DoctorUserV2Controller.getWelcomeMessage）
     */
    DoctorWelcomeMessageSet getWelcomeMessage();

    /**
     * 设置欢迎语（doctor-api: DoctorUserV2Controller.setWelcomeMessage）
     */
    DoctorWelcomeMessageSet setWelcomeMessage(DoctorWelcomeMessageDto request);

    /**
     * 医生详情（doctor-api: DoctorUserV2Controller.detail）
     */
    DoctorUserDto detail(String id);

    /**
     * 医生新增（admin-api: DoctorUserController.doctorUserAdd）
     * 简化版本：仅插入 t_doctor_user 基础数据 + t_doctor_visit_set 空记录 + t_doctor_welcome_message 空记录。
     */
    void doctorUserAdd(DoctorUserAddRequestDto request, HttpServletRequest httpRequest) throws Exception;

    /**
     * 医生更新（admin-api: DoctorUserController.doctorUpdate）
     * 简化版本：仅更新 t_doctor_user 基础数据。
     */
    void doctorUpdate(DoctorUserUpdateRequestDto request, HttpServletRequest httpRequest) throws Exception;

    /**
     * 医生启用/禁用（admin-api: DoctorUserController.enableDisable）
     * 更新 doctorUser.status；禁用时清理 redis 中的登录 token。
     */
    boolean enableDisable(DoctorEnableDisableRequestDto request);

}
