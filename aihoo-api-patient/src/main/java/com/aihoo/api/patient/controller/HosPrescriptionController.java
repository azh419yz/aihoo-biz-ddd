package com.aihoo.api.patient.controller;

import com.aihoo.api.patient.request.ConfirmedPrescriptionRequest;
import com.aihoo.api.patient.request.PrescriptionSelectRequest;
import com.aihoo.api.patient.vo.RecentPreVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.visit.dto.ConfirmedPrescriptionDto;
import com.aihoo.domain.visit.dto.PrescriptionSelectDto;
import com.aihoo.domain.visit.dto.RecentPreDto;
import com.aihoo.domain.visit.entity.HosPrescription;
import com.aihoo.domain.visit.service.HosPrescriptionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PrescriptionV2", description = "患者端-处方相关接口")
@RestController
@RequestMapping("/api/v2/pre")
@RequiredArgsConstructor
public class HosPrescriptionController {

    private final HosPrescriptionService hosPrescriptionService;

    @GetMapping
    @Operation(summary = "我的开方数")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, HosPrescription.class},
                            description = "返回处方信息"
                    )
            )
    )
    public BizResult<IPage<HosPrescription>> list(@RequestBody PrescriptionSelectRequest request) {
        PrescriptionSelectDto dto = new PrescriptionSelectDto();
        BeanUtils.copyProperties(request, dto);
        return BizResult.success(hosPrescriptionService.getHosPrescriptionList(dto));
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
                            description = "返回处方信息"
                    )
            )
    )
    public BizResult<RecentPreVo> getRecentPreById(
            @Parameter(name = "id", description = "处方ID", example = "123") Long id,
            @Parameter(name = "toProvince", description = "收货地址", example = "北京市") String toProvince) {
        return BizResult.success(toVo(hosPrescriptionService.getRecentPreById(id, toProvince)));
    }

    @PutMapping("/confirmed")
    @Operation(summary = "确认处方")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class},
                            description = "返回是否确认成功"
                    )
            )
    )
    public BizResult<Boolean> confirmed(@RequestBody ConfirmedPrescriptionRequest req) {
        ConfirmedPrescriptionDto dto = new ConfirmedPrescriptionDto();
        BeanUtils.copyProperties(req, dto);
        return BizResult.success(hosPrescriptionService.confirmed(dto));
    }

    @GetMapping("/confirmed")
    @Operation(summary = "查询处方确认状态")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class},
                            description = "返回是否确认成功 TRUE为确认。FALSE为没有确认"
                    )
            )
    )
    public BizResult<Boolean> selectConfirmedStatus(ConfirmedPrescriptionRequest req) {
        HosPrescription prescription = hosPrescriptionService.getById(req.getPrescriptionId());
        if (prescription == null) {
            return BizResult.fail(500, "没有查询到该处方");
        }
        return BizResult.success(prescription.getConfirmedStatus().equals(1));
    }

    private RecentPreVo toVo(RecentPreDto dto) {
        if (dto == null) return null;
        RecentPreVo vo = new RecentPreVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }
}