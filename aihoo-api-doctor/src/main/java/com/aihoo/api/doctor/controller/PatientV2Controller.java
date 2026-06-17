package com.aihoo.api.doctor.controller;

import com.aihoo.common.BizResult;
import com.aihoo.domain.patient.dto.HosSickVo;
import com.aihoo.domain.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: 患者
 * @author: Mr.Li
 * @create: 2020-09-29 13:54
 **/
@Tag(name = "PatientV2", description = "医生端-患者相关接口")
@RestController
@RequestMapping("/api/v2/patient")
@RequiredArgsConstructor
public class PatientV2Controller {
    private final PatientService patientService;

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
        return BizResult.success(patientService.patientList(sickName));

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
        return BizResult.success(patientService.patientMsg(id));

    }
}
