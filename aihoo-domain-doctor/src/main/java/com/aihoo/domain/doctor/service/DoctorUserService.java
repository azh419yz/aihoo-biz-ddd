package com.aihoo.domain.doctor.service;

import com.aihoo.domain.doctor.dto.DoctorUserDetailsDto;
import com.aihoo.domain.doctor.entity.DoctorUser;
import com.baomidou.mybatisplus.extension.service.IService;

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
 * @author mcp
 * @since 2020-09-15
 */
public interface DoctorUserService extends IService<DoctorUser> {

    /** 医生查询（patient-api: DoctorUserV2Controller.list） */
    List<DoctorUser> doctorQuery(String name);

    /** 医生详情（patient-api: DoctorUserV2Controller.doctorDetails） */
    DoctorUserDetailsDto doctorDetails(String id);

    /** 查询当前时间段医生的欢迎消息（patient-api: DoctorUserV2Controller.getNowWelcomeMessageSet） */
    String getNowWelcomeMessage(Long doctorUserId);
}
