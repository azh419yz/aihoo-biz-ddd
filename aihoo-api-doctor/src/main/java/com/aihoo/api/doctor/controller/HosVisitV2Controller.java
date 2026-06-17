package com.aihoo.api.doctor.controller;


import com.aihoo.api.doctor.controller.vo.HosVisitOrderVo;
import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import com.aihoo.common.JsonResult;
import com.aihoo.domain.visit.dto.HosOrder;
import com.aihoo.domain.visit.dto.HosVisitBaseInfoVo;
import com.aihoo.domain.visit.dto.HosVisitHealthInfoVo;
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

/**
 * <p>
 * 在线问诊信息表 前端控制器
 * </p>
 *
 * @author updateStatusHavemcp
 * @since 2020-09-18
 */
@Tag(name = "HosVisitV2", description = "医生端-问诊相关接口")
@RestController
@RequestMapping("/api/v2/hosVisit")
@RequiredArgsConstructor
@Log4j2
public class HosVisitV2Controller {

    private final HosVisitService hosVisitService;


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
                            oneOf = {BizResult.class, HosVisitBaseInfoVo.class},
                            description = "获取问诊资料-基本状况"
                    )
            )
    )
    public BizResult<HosVisitBaseInfoVo> getBaseInfo(@RequestParam String hosVisitId) {
        return BizResult.success(hosVisitService.getBaseInfo(hosVisitId));
    }

    /**
     * 医生-》问诊-》详情
     *
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/visitData")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {HosOrder.class},
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
            HosOrder status = hosVisitService.visitData(id);
            return JsonResult.ok().put("data", status);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 查看患者问诊订单
     */
    @GetMapping("/visitList")
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

}
