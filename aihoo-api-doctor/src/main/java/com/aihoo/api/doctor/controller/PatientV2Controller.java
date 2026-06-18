package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.vo.HosSickVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.patient.dto.HosSickDto;
import com.aihoo.domain.patient.service.HosSickService;
import com.aihoo.domain.visit.service.HosRevisitService;
import com.aihoo.domain.visit.service.HosVisitService;
import com.aihoo.security.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: 患者
 * @author: Mr.Li
 * @create: 2020-09-29 13:54
 *
 * <p>2026-06-18 拆解循环依赖：sickIds 由 controller 调 HosVisitService + HosRevisitService 取并集后传入 patient service。
 */
@Tag(name = "PatientV2", description = "医生端-患者相关接口")
@RestController
@RequestMapping("/api/v2/patient")
@RequiredArgsConstructor
public class PatientV2Controller {
    private final HosSickService hosSickService;
    private final HosVisitService hosVisitService;
    private final HosRevisitService hosRevisitService;

    @GetMapping("/patientList")
    @Operation(summary = "就诊人列表")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosSickVo.class},
                            description = "就诊人列表"
                    )
            )
    )
    public BizResult<List<HosSickVo>> patientList(@Parameter(required = false) String sickName) {
        String doctorId = AuthUtil.getLoginUserId();
        List<String> visitSickIds = hosVisitService.listSickIdsByDoctorUserId(doctorId);
        List<String> revisitSickIds = hosRevisitService.listSickIdsByDoctorUserId(doctorId);
        Set<String> sickIdSet = new LinkedHashSet<>();
        sickIdSet.addAll(visitSickIds);
        sickIdSet.addAll(revisitSickIds);

        List<HosSickDto> dtos = hosSickService.patientListBySickIds(List.copyOf(sickIdSet), sickName);
        return BizResult.success(dtos.stream().map(this::convert2Vo).toList());
    }

    @GetMapping("/patientMsg")
    @Operation(summary = "患者详情")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosSickVo.class},
                            description = "患者详情"
                    )
            )
    )
    public BizResult<HosSickVo> patientMsg(@Parameter String id) {
        HosSickDto dto = hosSickService.patientMsg(id);
        return BizResult.success(convert2Vo(dto));
    }

    private HosSickVo convert2Vo(HosSickDto dto) {
        if (dto == null) {
            return null;
        }
        HosSickVo vo = new HosSickVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }
}