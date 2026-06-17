package com.aihoo.api.doctor.controller;

import com.aihoo.common.BizResult;
import com.aihoo.domain.prescription.dto.*;
import com.aihoo.domain.prescription.model.entity.HosPrescription;
import com.aihoo.domain.prescription.service.PrescriptionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * 开处方接口
 */
@Tag(name = "PrescriptionV2", description = "医生端-处方相关接口")
@RestController
@RequestMapping("/api/v2/pre")
@RequiredArgsConstructor
public class PrescriptionV2Controller {
    private final PrescriptionService prescriptionService;

    @GetMapping("/recentPre")
    @Operation(summary = "最近处方")
    public BizResult<RecentPreVo> getRecentPre(@Valid SearchRecentPreRequest request) {
        return BizResult.success(prescriptionService.getRecentPre(request));
    }


    @PostMapping("/save")
    @Operation(summary = "辩证开方")
    public BizResult<RecentPreVo> save(@Valid @RequestBody SavePrescriptionRequest request) {
        return BizResult.success(prescriptionService.savePrescription(request));

    }

    @GetMapping
    @Operation(summary = "我的开方数")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosPrescription.class},
                            description = "处方列表"
                    )
            )
    )
    public BizResult<IPage<HosPrescription>> list(PrescriptionQueryRequest request) {
        return BizResult.success(prescriptionService.getHosPrescriptionList(request));
    }

    @GetMapping("/view")
    @Operation(summary = "查询处方内容")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, RecentPreVo.class},
                            description = "处方详情"
                    )
            )
    )
    public BizResult<RecentPreVo> getRecentPreById(@ParameterObject Long id) {
        return BizResult.success(prescriptionService.getRecentPreById(id));
    }

    @PutMapping("/withdraw")
    @Operation(summary = "撤回处方")
    public BizResult<Boolean> withdrawPrescription(@RequestBody WithdrawPrescriptionRequest req) {
        return BizResult.success(prescriptionService.withdrawPrescription(req));
    }

}
