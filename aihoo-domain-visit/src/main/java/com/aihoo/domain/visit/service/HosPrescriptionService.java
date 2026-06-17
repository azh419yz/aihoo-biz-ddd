package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.dto.ConfirmedPrescriptionDto;
import com.aihoo.domain.visit.dto.HosPrescriptionInnerDto;
import com.aihoo.domain.visit.dto.PrescriptionSelectDto;
import com.aihoo.domain.visit.dto.RecentPreDto;
import com.aihoo.domain.visit.entity.HosPrescription;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 处方 service（迁自 patient-api 的 HosPrescriptionService）。
 */
public interface HosPrescriptionService extends IService<HosPrescription> {

    /**
     * 根据 visitMdtNum 查询处方列表（patient-api: HosSickServiceImpl 中调用）。
     */
    List<HosPrescription> listByVisitMdtNum(String visitMdtNum);

    /**
     * 我的开方数（patient-api: HosPrescriptionV2Controller.list）。
     */
    IPage<HosPrescription> getHosPrescriptionList(PrescriptionSelectDto request);

    /**
     * 查询处方详情（patient-api: HosPrescriptionV2Controller.getRecentPreById）。
     */
    RecentPreDto getRecentPreById(Long id, String toProvince);

    /**
     * 确认处方（patient-api: HosPrescriptionV2Controller.confirmed）。
     */
    Boolean confirmed(ConfirmedPrescriptionDto req);

    /**
     * 内部 API：处方笺聚合查询（admin InnerController.prescription）。
     * 跨域：doctor 域 DoctorUserService + order 域 MdtOrderService（Rule H 授权）。
     */
    HosPrescriptionInnerDto getPrescriptionInnerVo(String id);
}