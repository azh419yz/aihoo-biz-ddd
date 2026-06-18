package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.entity.HosRevisit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 复诊 service（迁自 patient-api: HosSickServiceImpl.patientListByDoctorId 中对 t_hos_revisit 表的查询）。
 * 2026-06-18 拆解循环依赖：原 patient 域直接使用 visit 域的 HosRevisitMapper，现封装为 service 暴露给 api 层。
 */
public interface HosRevisitService extends IService<HosRevisit> {

    /**
     * 按医生 ID 查询其复诊过的所有就诊人 ID（revisit 表去重）。
     * 配合 HosVisitService.listSickIdsByDoctorUserId 取并集，用于 patient-api: HosSickController.patientList。
     */
    List<String> listSickIdsByDoctorUserId(String doctorId);
}