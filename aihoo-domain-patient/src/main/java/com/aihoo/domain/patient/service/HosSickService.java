package com.aihoo.domain.patient.service;

import com.aihoo.domain.patient.dto.HosSickDto;
import com.aihoo.domain.patient.dto.SaveUpdateHosSickDto;
import com.aihoo.domain.patient.entity.HosSick;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 就诊人 service（迁自 patient-api 的 HosSickService）。
 */
public interface HosSickService extends IService<HosSick> {

    /**
     * 统计当前患者下的就诊人数量（patient-api: PatientUserServiceImpl.getPatientUserVo 用）。
     */
    long countHosSickByPatientUserId(String patientUserId);

    /**
     * 根据医生 ID 查询当前登录患者的就诊人列表。
     */
    List<HosSickDto> queryHosSickByDoctorId(String doctorId);

    /**
     * 根据就诊人 ID 查询就诊人详情。
     */
    HosSickDto queryHosSickByHosSickId(String hosSickId);

    /**
     * 实名认证就诊人（按身份证回填性别、生日）。
     */
    SaveUpdateHosSickDto validateRequest(SaveUpdateHosSickDto request);

    /**
     * 删除就诊人。
     */
    int removeHosSick(String hosSickId);

    /**
     * 新增就诊人。
     */
    HosSickDto saveHosSick(SaveUpdateHosSickDto request);

    /**
     * 修改就诊人。
     */
    HosSickDto updateHosSick(SaveUpdateHosSickDto request);

    /**
     * 写入 IM 用户签名。
     */
    void setImUserSig(String hosSickId, String imUserId, String userSig);
}