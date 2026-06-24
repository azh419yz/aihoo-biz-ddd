package com.aihoo.api.patient.controller;

import com.aihoo.api.patient.request.SaveUpdateHosSickRequest;
import com.aihoo.api.patient.vo.HosSickVo;
import com.aihoo.api.patient.vo.HosVisitVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.patient.dto.HosSickDto;
import com.aihoo.domain.patient.dto.SaveUpdateHosSickDto;
import com.aihoo.domain.patient.entity.HosSick;
import com.aihoo.domain.patient.service.HosSickService;
import com.aihoo.domain.visit.entity.HosPrescription;
import com.aihoo.domain.visit.entity.HosVisit;
import com.aihoo.domain.visit.service.HosPrescriptionService;
import com.aihoo.domain.visit.service.HosVisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "HosSickV2", description = "患者端-就诊人相关接口")
@RestController
@RequestMapping("/api/v2/hosSick")
@RequiredArgsConstructor
public class HosSickController {

    private final HosSickService hosSickService;
    private final HosVisitService hosVisitService;
    private final HosPrescriptionService hosPrescriptionService;
    private final DoctorUserService doctorUserService;

    
    @GetMapping("/queryByPatientUserId")
    @Operation(summary = "查询所有就诊人信息")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosSickVo.class},
                            description = "查询所有就诊人信息"
                    )
            )
    )
    public BizResult<List<HosSickVo>> queryHosSickByPatientUserId(@RequestParam(required = false) String doctorId) {
        List<HosSickDto> dtos = hosSickService.queryHosSickByDoctorId(doctorId);

        java.util.Map<String, HosVisit> latestVisitMap = new java.util.HashMap<>();
        if (doctorId != null && !doctorId.isBlank()) {
            for (HosSickDto dto : dtos) {
                HosVisit latestHosVisit = hosVisitService.latestHosVisit(dto.getId(), doctorId);
                if (latestHosVisit != null) {
                    latestVisitMap.put(dto.getId(), latestHosVisit);
                }
            }
        }
        return BizResult.success(dtos.stream().map(dto -> toVo(dto, latestVisitMap.get(dto.getId()))).toList());
    }

    
    @GetMapping("/queryByHosSickId")
    @Operation(summary = "查询单个就诊人信息")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosSickVo.class},
                            description = "返回患者信息"
                    )
            )
    )
    public BizResult<HosSickVo> queryHosSickByHosSickId(@ParameterObject String hosSickId) {
        HosSickDto dto = hosSickService.queryHosSickByHosSickId(hosSickId);
        return BizResult.success(toVo(dto, null));
    }

    
    @PostMapping("/check")
    @Operation(summary = "认证就诊人")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, SaveUpdateHosSickRequest.class},
                            description = "认证就诊人"
                    )
            )
    )
    public BizResult<SaveUpdateHosSickRequest> validateRequest(
            @Validated(SaveUpdateHosSickRequest.Check.class) @RequestBody SaveUpdateHosSickRequest request) {
        SaveUpdateHosSickDto dto = new SaveUpdateHosSickDto();
        BeanUtils.copyProperties(request, dto);
        SaveUpdateHosSickDto result = hosSickService.validateRequest(dto);
        SaveUpdateHosSickRequest resp = new SaveUpdateHosSickRequest();
        BeanUtils.copyProperties(result, resp);
        return BizResult.success(resp);
    }

    
    @DeleteMapping("/delete/{hosSickId}")
    @Operation(summary = "删除就诊人")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Integer.class},
                            description = "删除就诊人"
                    )
            )
    )
    public BizResult<Integer> deleteHosSickById(@PathVariable String hosSickId) {
        return BizResult.success(hosSickService.removeHosSick(hosSickId));
    }

    
    @PostMapping("/save")
    @Operation(summary = "增加就诊人")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosSickVo.class},
                            description = "添加就诊人成功"
                    )
            )
    )
    public BizResult<HosSickVo> saveHosSick(
            @Validated(SaveUpdateHosSickRequest.Save.class) @RequestBody SaveUpdateHosSickRequest request
    ) {
        SaveUpdateHosSickDto dto = new SaveUpdateHosSickDto();
        BeanUtils.copyProperties(request, dto);
        return BizResult.success("添加就诊人成功", toVo(hosSickService.saveHosSick(dto), null));
    }

    
    @PutMapping("/update")
    @Operation(summary = "修改就诊人")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosSickVo.class},
                            description = "修改就诊人成功"
                    )
            )
    )
    public BizResult<HosSickVo> updateHosSickById(
            @Validated(SaveUpdateHosSickRequest.Update.class) @RequestBody SaveUpdateHosSickRequest request
    ) {
        SaveUpdateHosSickDto dto = new SaveUpdateHosSickDto();
        BeanUtils.copyProperties(request, dto);
        return BizResult.success(toVo(hosSickService.updateHosSick(dto), null));
    }

    private HosSickVo toVo(HosSickDto dto, HosVisit latestVisit) {
        if (dto == null) return null;
        HosSickVo vo = new HosSickVo();
        BeanUtils.copyProperties(dto, vo);

        if (latestVisit != null) {
            String status = latestVisit.getStatus();
            if ("UNSUBMITTED".equals(status) || "SUBMITTED".equals(status) || "STARTED".equals(status)) {
                vo.setStatus(latestVisit.getPatientUserId().equals(com.aihoo.security.AuthUtil.getLoginUserId()) ?
                        "问诊中" : "其他家庭成员账号问诊中");
                vo.setImGroupId(latestVisit.getImGroupId());
            }
        }

        List<HosVisit> visits = hosVisitService.listVisitsByHosSickId(dto.getId());
        if (visits != null && !visits.isEmpty()) {
            List<HosVisitVo> visitVos = visits.stream().map(this::toVisitVo).toList();
            vo.setVisits(visitVos);
        }
        return vo;
    }

    private HosVisitVo toVisitVo(HosVisit visit) {
        HosVisitVo vo = new HosVisitVo();
        BeanUtils.copyProperties(visit, vo);
        vo.setCreateTime(visit.getCreateTime());
        vo.setContent(visit.getContent());
        vo.setImGroupId("GROUP_" + visit.getOrderNum());
        vo.setHosPrescriptions(hosPrescriptionService.listByVisitMdtNum(visit.getOrderNum()));
        if (visit.getDoctorUserId() != null && !visit.getDoctorUserId().isEmpty()) {
            DoctorUser doctor = doctorUserService.getById(visit.getDoctorUserId());
            if (doctor != null) {
                vo.setDoctorName(doctor.getName());
                vo.setDoctorHeadImg(doctor.getHeadImg());
            }
        }
        return vo;
    }
}