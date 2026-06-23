package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.request.SaveDoctorDirectoryRequest;
import com.aihoo.api.doctor.vo.DoctorDirectoryVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.doctor.dto.DoctorDirectoryDto;
import com.aihoo.domain.doctor.entity.DoctorDirectory;
import com.aihoo.domain.doctor.service.DoctorDirectoryService;
import com.aihoo.domain.patient.entity.HosSick;
import com.aihoo.domain.patient.service.HosSickService;
import com.aihoo.domain.sys.oss.OssComponent;
import com.aihoo.util.AvatarUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 医生通讯录 controller（迁自 doctor-api: DoctorDirectoryV2Controller）。
 * 2026-06-18 拆解循环依赖：患者信息合并逻辑从 DoctorDirectoryServiceImpl 上移到本 controller。
 */
@Tag(name = "doctorDirectory", description = "医生端-通讯录")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/doctor-directory")
public class DoctorDirectoryController {

    private final DoctorDirectoryService doctorDirectoryService;
    private final HosSickService hosSickService;
    private final OssComponent ossComponent;

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

        List<Long> sickIds = dtoList.stream().map(DoctorDirectoryDto::getSickId).toList();
        Map<Long, HosSick> sickMap = hosSickService.listBySickIds(sickIds).stream()
                .collect(Collectors.toMap(sick -> Long.valueOf(sick.getId()), Function.identity(), (a, b) -> a));

        return BizResult.success(dtoList.stream().map(dto -> merge(dto, sickMap)).collect(Collectors.toList()));
    }

    private DoctorDirectoryVo merge(DoctorDirectoryDto dto, Map<Long, HosSick> sickMap) {
        DoctorDirectoryVo vo = new DoctorDirectoryVo();
        BeanUtils.copyProperties(dto, vo);

        HosSick sick = dto.getSickId() == null ? null : sickMap.get(dto.getSickId());
        if (sick == null) {
            vo.setSickName("患者" + new Random().nextInt(9000));
            return vo;
        }

        vo.setSickAge(sick.getAge());
        vo.setMobile(sick.getMobile());
        vo.setSickSex(sick.getSex());
        vo.setSickName(sick.getName());
        vo.setSaveTime(sick.getCreateTime());
        vo.setAvatar(ossComponent.getUrl(AvatarUtil.getAvatarPath(sick.getSex(), sick.getAge())));
        return vo;
    }
}