package com.aihoo.api.patient.controller;

import com.aihoo.api.patient.request.SaveDoctorDirectoryRequest;
import com.aihoo.common.BizResult;
import com.aihoo.domain.doctor.entity.DoctorDirectory;
import com.aihoo.domain.doctor.service.DoctorDirectoryService;
import com.aihoo.domain.patient.entity.HosSick;
import com.aihoo.domain.patient.service.HosSickService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/5 16:01
 */
@Tag(name = "doctorDirectory", description = "患者端-通讯录")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/doctor-directory")
public class DoctorDirectoryController {

    private final DoctorDirectoryService doctorDirectoryService;
    private final HosSickService hosSickService;

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
        doctorDirectory.setDoctorUserId(directoryRequest.getDoctorId());
        doctorDirectory.setPatientUserId(directoryRequest.getPatientUserId());
        doctorDirectory.setSource(1);
        if (directoryRequest.getSickId() == null || directoryRequest.getSickId() == 0) {
            List<HosSick> list = hosSickService.list(new LambdaQueryWrapper<HosSick>()
                    .eq(HosSick::getPatientUserId, directoryRequest.getPatientUserId()));
            if (CollectionUtils.isNotEmpty(list)) {
                doctorDirectory.setSickId(Long.valueOf(list.get(0).getId()));
                doctorDirectory.setSickName(list.get(0).getName());
            }
        }

        //判断是否已经存在该通讯录信息
        List<DoctorDirectory> dirs = doctorDirectoryService.list(new LambdaQueryWrapper<DoctorDirectory>()
                .eq(doctorDirectory.getSickId() != null && doctorDirectory.getSickId() > 0, DoctorDirectory::getSickId, doctorDirectory.getSickId())
                .eq(DoctorDirectory::getDoctorUserId, doctorDirectory.getDoctorUserId())
                .eq(DoctorDirectory::getPatientUserId, doctorDirectory.getPatientUserId()));

        if (CollectionUtils.isNotEmpty(dirs)) {
            return BizResult.fail(500, "重复添加");
        }

        return BizResult.success(doctorDirectoryService.save(doctorDirectory));
    }
}
