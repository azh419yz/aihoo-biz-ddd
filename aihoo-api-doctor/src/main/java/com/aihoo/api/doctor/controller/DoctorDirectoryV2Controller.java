package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.request.SaveDoctorDirectoryRequest;
import com.aihoo.api.doctor.vo.DoctorDirectoryVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.doctor.dto.DoctorDirectoryDto;
import com.aihoo.domain.doctor.entity.DoctorDirectory;
import com.aihoo.domain.doctor.service.DoctorDirectoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 医生通讯录 controller（迁自 doctor-api: DoctorDirectoryV2Controller）。
 */
@Tag(name = "doctorDirectory", description = "医生端-通讯录")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/doctor-directory")
public class DoctorDirectoryV2Controller {

    private final DoctorDirectoryService doctorDirectoryService;

    @PostMapping
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Boolean.class},
                            description = "保存成功或者失败, TRUE OR FALSE"
                    )
            )
    )
    @Operation(summary = "扫码保存医生患者关系")
    public BizResult<Boolean> saveDoctorDirectory(@RequestBody SaveDoctorDirectoryRequest directoryRequest) {
        DoctorDirectory doctorDirectory = new DoctorDirectory();
        doctorDirectory.setDoctorId(directoryRequest.getDoctorId());
        doctorDirectory.setSource(1);
        doctorDirectory.setSickId(directoryRequest.getSickId());
        doctorDirectory.setPatientUserId(directoryRequest.getPatientUserId());
        return BizResult.success(doctorDirectoryService.save(doctorDirectory));
    }

    @GetMapping
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, DoctorDirectoryVo.class},
                            description = "患者列表信息"
                    )
            )
    )
    @Operation(summary = "查询医生通讯录")
    public BizResult<List<DoctorDirectoryVo>> list(String sickName) {
        List<DoctorDirectoryDto> dtoList = doctorDirectoryService.findDoctorDirectoryList(sickName);
        return BizResult.success(dtoList.stream().map(this::toVo).collect(Collectors.toList()));
    }

    private DoctorDirectoryVo toVo(DoctorDirectoryDto dto) {
        DoctorDirectoryVo vo = new DoctorDirectoryVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }
}
