package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.dto.PrescriptionQueryDto;
import com.aihoo.domain.visit.dto.RecentPreDto;
import com.aihoo.domain.visit.dto.SavePrescriptionDto;
import com.aihoo.domain.visit.dto.SearchRecentPreDto;
import com.aihoo.domain.visit.dto.WithdrawPrescriptionDto;
import com.aihoo.domain.visit.entity.HosPrescription;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 处方服务（迁自 doctor-api 的 PrescriptionService）。
 *
 * <p>承载 doctor-api 视角的开方/审方业务；patient-api 视角由 {@link HosPrescriptionService} 提供。
 */
public interface PrescriptionService extends IService<HosPrescription> {

    long countByDoctorUserId(String doctorUserId);

    /**
     * 开处方保存（doctor-api: PrescriptionV2Controller.save）。
     */
    RecentPreDto savePrescription(SavePrescriptionDto request);

    /**
     * 最近处方查询（doctor-api: PrescriptionV2Controller.getRecentPre）。
     */
    RecentPreDto getRecentPre(SearchRecentPreDto request);

    /**
     * 处方列表分页（doctor-api: PrescriptionV2Controller.list）。
     */
    IPage<HosPrescription> getHosPrescriptionList(PrescriptionQueryDto request);

    /**
     * 处方详情（doctor-api: PrescriptionV2Controller.getRecentPreById）。
     */
    RecentPreDto getRecentPreById(Long id);

    /**
     * 撤回处方（doctor-api: PrescriptionV2Controller.withdrawPrescription）。
     */
    Boolean withdrawPrescription(WithdrawPrescriptionDto req);

    /**
     * 生成电子处方（doctor-api: PrescriptionV2Controller.generateEprescription）。
     */
    Boolean generateEprescription(Long prescriptionId);
}
