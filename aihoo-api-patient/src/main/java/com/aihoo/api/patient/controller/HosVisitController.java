package com.aihoo.api.patient.controller;

import com.aihoo.api.patient.request.HosVisitCreateRequest;
import com.aihoo.api.patient.request.HosVisitIdRequest;
import com.aihoo.api.patient.request.HosVisitInfoRequest;
import com.aihoo.api.patient.vo.HosVisitBaseInfoVo;
import com.aihoo.api.patient.vo.HosVisitHealthInfoVo;
import com.aihoo.api.patient.vo.HosVisitOrderVo;
import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import com.aihoo.domain.visit.entity.HosVisit;
import com.aihoo.domain.visit.service.HosVisitService;
import com.alibaba.fastjson2.JSONArray;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "HosVisitV2", description = "患者端-问诊相关接口")
@RestController
@RequestMapping("/api/v2/hosVisit")
@RequiredArgsConstructor
public class HosVisitController {

    private final HosVisitService hosVisitService;

    
    @PostMapping("/createOrder")
    @Operation(summary = "创建问诊单")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosVisit.class},
                            description = "创建问诊单"
                    )
            )
    )
    public BizResult<HosVisit> createOrder(@Validated @RequestBody HosVisitCreateRequest request) {
        com.aihoo.domain.visit.dto.HosVisitCreateDto dto = new com.aihoo.domain.visit.dto.HosVisitCreateDto();
        BeanUtils.copyProperties(request, dto);
        return BizResult.success(hosVisitService.createOrder(dto));
    }

    @PostMapping("/pay")
    @Operation(summary = "支付问诊订单")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "支付问诊订单"
                    )
            )
    )
    public BizResult<Void> hosVisitPay(@Validated @RequestBody HosVisitIdRequest request) {
        hosVisitService.hosVisitPay(request.getHosVisitId());
        return BizResult.success("支付成功!");
    }

    @PostMapping("/addHosSick")
    @Operation(summary = "添加问诊订单就诊人")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "添加问诊订单就诊人"
                    )
            )
    )
    public BizResult<Void> addHosSick(@Validated @RequestBody HosVisitIdRequest request) {
        hosVisitService.addHosSick(request.getHosVisitId(), request.getHosSickId());
        return BizResult.success("就诊人添加成功!");
    }

    @PostMapping("/addHealthInfo")
    @Operation(summary = "添加问诊资料-健康状况")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "添加问诊资料-健康状况"
                    )
            )
    )
    public BizResult<Void> addHealthInfo(@Validated @RequestBody HosVisitInfoRequest request) {
        hosVisitService.addHealthInfo(toVisitDto(request));
        return BizResult.success("问诊资料-健康状况添加成功!");
    }

    @PostMapping("/addBaseInfo")
    @Operation(summary = "添加问诊资料-基本状况")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "添加问诊资料-基本状况"
                    )
            )
    )
    public BizResult<Void> addBaseInfo(@Validated @RequestBody HosVisitInfoRequest request) {
        hosVisitService.addBaseInfo(toVisitDto(request));
        return BizResult.success("问诊资料基本状况添加成功!");
    }

    @PutMapping("/updateBaseInfo")
    @Operation(summary = "更新问诊资料-基本状况")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "更新问诊资料-基本状况"
                    )
            )
    )
    public BizResult<Void> updateBaseInfo(@Validated @RequestBody HosVisitInfoRequest request) {
        hosVisitService.updateBaseInfo(toVisitDto(request));
        return BizResult.success("问诊资料基本状况更新成功!");
    }

    @PostMapping("/submitInfo")
    @Operation(summary = "提交问诊资料(健康状况、基本状况资料添加完使用)")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Void.class},
                            description = "提交问诊资料"
                    )
            )
    )
    public BizResult<Void> submitInfo(@Validated @RequestBody HosVisitIdRequest request) {
        hosVisitService.submitInfo(request.getHosVisitId());
        return BizResult.success("问诊资料添加成功!");
    }

    @GetMapping("/healthInfo")
    @Operation(summary = "获取问诊资料-健康状况")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosVisitHealthInfoVo.class},
                            description = "获取问诊资料-健康状况"
                    )
            )
    )
    public BizResult<HosVisitHealthInfoVo> getHealthInfo(@RequestParam String hosVisitId) {
        return BizResult.success(toHealthInfoVo(hosVisitService.getHealthInfo(hosVisitId)));
    }

    @GetMapping("/baseInfo")
    @Operation(summary = "获取问诊资料-基本状况")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosVisitBaseInfoVo.class},
                            description = "获取问诊资料-基本状况"
                    )
            )
    )
    public BizResult<HosVisitBaseInfoVo> getBaseInfo(@RequestParam String hosVisitId) {
        return BizResult.success(toBaseInfoVo(hosVisitService.getBaseInfo(hosVisitId)));
    }

    
    @PostMapping("/patient/visitList")
    @Operation(summary = "查看患者相关问诊订单")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosVisitOrderVo.class},
                            description = "返回信息发送相关结果"
                    )
            )
    )
    public BizResult<List<HosVisitOrderVo>> patientVisitList() {
        try {
            JSONArray hosVisitAndDoctorList = hosVisitService.patientList(Maps.newHashMap());

            List<HosVisitOrderVo> hosVisitOrderList = hosVisitAndDoctorList.toList(HosVisitOrderVo.class);

            return BizResult.success(hosVisitOrderList);
        } catch (Exception e) {
            log.info("异常:", e);
            return BizResult.fail(BizResultCode.INTERNAL_ERROR);
        }
    }

    private com.aihoo.domain.visit.dto.HosVisitCreateDto toVisitDto(HosVisitCreateRequest request) {
        com.aihoo.domain.visit.dto.HosVisitCreateDto dto = new com.aihoo.domain.visit.dto.HosVisitCreateDto();
        BeanUtils.copyProperties(request, dto);
        return dto;
    }

    private com.aihoo.domain.visit.dto.HosVisitInfoDto toVisitDto(HosVisitInfoRequest request) {
        com.aihoo.domain.visit.dto.HosVisitInfoDto dto = new com.aihoo.domain.visit.dto.HosVisitInfoDto();
        dto.setHosVisitId(request.getHosVisitId());
        dto.setHealthInfo(request.getHealthInfo());
        if (request.getBaseInfo() != null) {
            com.aihoo.domain.visit.dto.HosVisitBaseInfoDTO baseInfoDto = new com.aihoo.domain.visit.dto.HosVisitBaseInfoDTO();
            BeanUtils.copyProperties(request.getBaseInfo(), baseInfoDto);
            dto.setBaseInfo(baseInfoDto);
        }
        return dto;
    }

    private HosVisitHealthInfoVo toHealthInfoVo(com.aihoo.domain.visit.dto.HosVisitHealthInfoDto dto) {
        if (dto == null) return null;
        HosVisitHealthInfoVo vo = new HosVisitHealthInfoVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    private HosVisitBaseInfoVo toBaseInfoVo(com.aihoo.domain.visit.dto.HosVisitBaseInfoRespDto dto) {
        if (dto == null) return null;
        HosVisitBaseInfoVo vo = new HosVisitBaseInfoVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }
}
