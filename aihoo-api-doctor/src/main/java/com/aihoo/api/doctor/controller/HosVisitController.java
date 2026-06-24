package com.aihoo.api.doctor.controller;

import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import com.aihoo.common.JsonResult;
import com.aihoo.domain.visit.dto.HosOrderDto;
import com.aihoo.domain.visit.dto.HosVisitBaseInfoRespDto;
import com.aihoo.domain.visit.dto.HosVisitHealthInfoDto;
import com.aihoo.domain.visit.dto.HosVisitOrderDto;
import com.aihoo.domain.visit.service.HosVisitService;
import com.alibaba.fastjson2.JSONArray;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "HosVisitV2", description = "医生端-问诊相关接口")
@RestController
@RequestMapping("/api/v2/hosVisit")
@RequiredArgsConstructor
@Log4j2
public class HosVisitController {

    private final HosVisitService hosVisitService;

    @GetMapping("/healthInfo")
    @Operation(summary = "获取问诊资料-健康状况")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosVisitHealthInfoDto.class},
                            description = "获取问诊资料-健康状况"
                    )
            )
    )
    public BizResult<HosVisitHealthInfoDto> getHealthInfo(@RequestParam String hosVisitId) {
        return BizResult.success(hosVisitService.getHealthInfo(hosVisitId));
    }

    @GetMapping("/baseInfo")
    @Operation(summary = "获取问诊资料-基本状况")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosVisitBaseInfoRespDto.class},
                            description = "获取问诊资料-基本状况"
                    )
            )
    )
    public BizResult<HosVisitBaseInfoRespDto> getBaseInfo(@RequestParam String hosVisitId) {
        return BizResult.success(hosVisitService.getBaseInfo(hosVisitId));
    }

    @ResponseBody
    @GetMapping("/visitData")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {HosOrderDto.class},
                            description = "获取详情"
                    )
            )
    )
    public JsonResult visitData(
            @Parameter(name = "id", description = "问诊卡单id", example = "1234") String id) {
        if (StringUtils.isEmpty(id)) {
            return JsonResult.error("未传入参数id");
        }
        try {
            HosOrderDto status = hosVisitService.visitData(id);
            return JsonResult.ok().put("data", status);
        } catch (Exception e) {
            log.error("visitData 异常", e);
            return JsonResult.error();
        }
    }

    @GetMapping("/visitList")
    @Operation(summary = "查看患者相关问诊订单")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosVisitOrderDto.class},
                            description = "返回信息发送相关结果"
                    )
            )
    )
    public BizResult<List<HosVisitOrderDto>> patientVisitList() {
        try {
            JSONArray hosVisitAndDoctorList = hosVisitService.patientList(Maps.newHashMap());
            List<HosVisitOrderDto> hosVisitOrderList = hosVisitAndDoctorList.toList(HosVisitOrderDto.class);
            return BizResult.success(hosVisitOrderList);
        } catch (Exception e) {
            log.info("异常:", e);
            return BizResult.fail(BizResultCode.INTERNAL_ERROR);
        }
    }
}
