package com.aihoo.api.patient.controller;

import com.aihoo.api.patient.vo.VisitSelectDrugstoreVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.drug.service.DrugstoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 药房 controller（患者端，迁自 patient-api 的 DrugstoreV2Controller）。
 *
 * @author wyz
 * @since 2026/3/15
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v2/drugstore")
public class DrugstoreController {

    private final DrugstoreService drugstoreService;

    @GetMapping
    @Operation(summary = "根据药材查询药房")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, VisitSelectDrugstoreVo.class},
                            description = "返回处方信息"
                    )
            )
    )
    public BizResult<List<VisitSelectDrugstoreVo>> selectDrugstoreByDrug() {
        drugstoreService.selectDrugstoreByDrug();
        return BizResult.success();
    }
}
