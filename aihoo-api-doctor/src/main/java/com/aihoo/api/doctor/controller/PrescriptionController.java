package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.request.PrescriptionQueryRequest;
import com.aihoo.api.doctor.request.SavePrescriptionRequest;
import com.aihoo.api.doctor.request.SearchRecentPreRequest;
import com.aihoo.api.doctor.request.WithdrawPrescriptionRequest;
import com.aihoo.api.doctor.vo.RecentPreVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.visit.dto.PrescriptionQueryDto;
import com.aihoo.domain.visit.dto.RecentPreDto;
import com.aihoo.domain.visit.dto.SavePrescriptionDto;
import com.aihoo.domain.visit.dto.SearchRecentPreDto;
import com.aihoo.domain.visit.dto.WithdrawPrescriptionDto;
import com.aihoo.domain.visit.entity.HosPrescription;
import com.aihoo.domain.visit.service.PrescriptionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 医生端-处方相关接口（迁自 doctor-api: PrescriptionV2Controller）。
 */
@Tag(name = "PrescriptionV2", description = "医生端-处方相关接口")
@RestController
@RequestMapping("/api/v2/pre")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @GetMapping("/recentPre")
    @Operation(summary = "最近处方")
    public BizResult<RecentPreVo> getRecentPre(@Valid SearchRecentPreRequest request) {
        SearchRecentPreDto dto = new SearchRecentPreDto();
        BeanUtils.copyProperties(request, dto);
        return BizResult.success(toVo(prescriptionService.getRecentPre(dto)));
    }

    @PostMapping("/save")
    @Operation(summary = "辩证开方")
    public BizResult<RecentPreVo> save(@Valid @RequestBody SavePrescriptionRequest request) {
        SavePrescriptionDto dto = new SavePrescriptionDto();
        BeanUtils.copyProperties(request, dto);
        return BizResult.success(toVo(prescriptionService.savePrescription(dto)));
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
        PrescriptionQueryDto dto = new PrescriptionQueryDto();
        BeanUtils.copyProperties(request, dto);
        return BizResult.success(prescriptionService.getHosPrescriptionList(dto));
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
        return BizResult.success(toVo(prescriptionService.getRecentPreById(id)));
    }

    @PutMapping("/withdraw")
    @Operation(summary = "撤回处方")
    public BizResult<Boolean> withdrawPrescription(@RequestBody WithdrawPrescriptionRequest req) {
        WithdrawPrescriptionDto dto = new WithdrawPrescriptionDto();
        BeanUtils.copyProperties(req, dto);
        return BizResult.success(prescriptionService.withdrawPrescription(dto));
    }

    private RecentPreVo toVo(RecentPreDto dto) {
        if (dto == null) {
            return null;
        }
        RecentPreVo vo = new RecentPreVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }
}
