package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.controller.request.SaveDoctorDirectoryRequest;
import com.aihoo.common.BizResult;
import com.aihoo.domain.doctor.dto.DoctorDirectoryVo;
import com.aihoo.domain.doctor.model.entity.DoctorDirectory;
import com.aihoo.domain.doctor.service.DoctorDirectoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/5 16:01
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

        return BizResult.success(doctorDirectoryService.findDoctorDirectoryList(sickName));
    }
}
