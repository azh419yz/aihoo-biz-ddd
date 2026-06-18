package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.vo.HosSickVo;
import com.aihoo.api.doctor.vo.HosVisitVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.patient.dto.HosSickDto;
import com.aihoo.domain.patient.service.HosSickService;
import com.aihoo.domain.visit.entity.HosVisit;
import com.aihoo.domain.visit.service.HosPrescriptionService;
import com.aihoo.domain.visit.service.HosVisitService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 医生端-获取患者详情（迁自 doctor-api: HosSickV2Controller）。
 *
 * <p>2026-06-18 拆解循环依赖：patient service 不再聚合 visit/prescription/doctor，
 * controller 调 HosVisitService + HosPrescriptionService + DoctorUserService 自行组装。
 */
@Tag(name = "HosSick", description = "医生端-获取患者详情")
@RestController
@RequestMapping("/api/v2/sick")
@RequiredArgsConstructor
public class HosSickV2Controller {

    private final HosSickService hosSickService;
    private final HosVisitService hosVisitService;
    private final HosPrescriptionService hosPrescriptionService;
    private final DoctorUserService doctorUserService;

    @GetMapping("/{id}")
    public BizResult<HosSickVo> findSickById(@PathVariable String id) {
        if (id == null || "0".equals(id)) {
            return BizResult.fail(500, "参数错误");
        }
        HosSickDto dto = hosSickService.queryHosSickByHosSickId(id);
        return BizResult.success(convert2Vo(dto));
    }

    private HosSickVo convert2Vo(HosSickDto dto) {
        if (dto == null) {
            return null;
        }
        HosSickVo vo = new HosSickVo();
        BeanUtils.copyProperties(dto, vo);

        List<HosVisit> visits = hosVisitService.listVisitsByHosSickId(dto.getId());
        if (visits != null && !visits.isEmpty()) {
            List<HosVisitVo> visitVos = new ArrayList<>();
            for (HosVisit visit : visits) {
                HosVisitVo visitVo = new HosVisitVo();
                BeanUtils.copyProperties(visit, visitVo);
                visitVo.setVisitId(visit.getId() == null ? null : Long.valueOf(visit.getId()));
                visitVo.setVisitNo(visit.getOrderNum());
                visitVo.setBaseInfo(visit.getBaseInfo());
                visitVo.setHealthInfo(visit.getHealthInfo());
                visitVo.setHosPrescriptions(hosPrescriptionService.listByVisitMdtNum(visit.getOrderNum()));
                if (visit.getDoctorUserId() != null && !visit.getDoctorUserId().isEmpty()) {
                    DoctorUser doctor = doctorUserService.getById(visit.getDoctorUserId());
                    if (doctor != null) {
                        visitVo.setDoctorName(doctor.getName());
                        visitVo.setDoctorHeadImg(doctor.getHeadImg());
                    }
                }
                visitVos.add(visitVo);
            }
            vo.setHosVisits(visitVos);
        }
        return vo;
    }
}