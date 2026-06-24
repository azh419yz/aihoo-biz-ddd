package com.aihoo.api.patient.controller;

import com.aihoo.api.patient.vo.DoctorUserVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.doctor.dto.DoctorUserDetailsDto;
import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.service.DoctorUserService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "PatientUserV2", description = "患者端-患者相关接口")
@RestController
@RequestMapping("/api/v2/doctorUser")
@RequiredArgsConstructor
public class DoctorUserController {
    private final DoctorUserService doctorUserService;

    @GetMapping("/list")
    @Operation(summary = "医生列表")
    public BizResult<List<DoctorUserVo>> list(@RequestParam(required = false) String name) {
        return BizResult.success(toVoList(doctorUserService.doctorQuery(name)));
    }

    @GetMapping("/doctorDetails")
    @Operation(summary = "医生详情")
    public BizResult<DoctorUserVo> doctorDetails(@RequestParam String id) {
        return BizResult.success(toVo(doctorUserService.doctorDetails(id)));
    }

    @GetMapping("/now/welcomeMessage")
    @Operation(summary = "查询当前时间段医生的欢迎消息")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, String.class},
                            description = "查询欢迎语设置"
                    )
            )
    )
    public BizResult<String> getNowWelcomeMessageSet(
            @Parameter(name = "doctorUserId", description = "医生ID", example = "12345")
            Long doctorUserId) {
        return BizResult.success(doctorUserService.getNowWelcomeMessage(doctorUserId));
    }

    private DoctorUserVo toVo(DoctorUserDetailsDto dto) {
        if (dto == null) {
            return null;
        }
        DoctorUserVo vo = new DoctorUserVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    private DoctorUserVo toVo(DoctorUser doctorUser) {
        if (doctorUser == null) {
            return null;
        }
        DoctorUserVo vo = new DoctorUserVo();
        BeanUtils.copyProperties(doctorUser, vo);
        return vo;
    }

    private List<DoctorUserVo> toVoList(List<DoctorUser> doctorUsers) {
        if (doctorUsers == null || doctorUsers.isEmpty()) {
            return List.of();
        }
        List<DoctorUserVo> result = new ArrayList<>(doctorUsers.size());
        for (DoctorUser doctorUser : doctorUsers) {
            result.add(toVo(doctorUser));
        }
        return result;
    }
}
