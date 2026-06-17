package com.aihoo.api.doctor.controller;

import com.aihoo.common.BizResult;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.dto.SearchDrugstoreRequest;
import com.aihoo.domain.hospital.model.entity.Drugstore;
import com.aihoo.domain.hospital.service.DrugstoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Drugstore", description = "医生端-药店相关接口")
@RestController
@RequestMapping("/api/v2/drugstore")
@RequiredArgsConstructor
public class DrugstoreController {

    private final DrugstoreService drugstoreService;

    /**
     * 带条件分页查询药房列表
     *
     * @param pageParam 分页参数
     * @return 分页结果
     */
    @GetMapping("/list")
    @Operation(summary = "药房列表")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, PageResult.class},
                            description = "药房列表"
                    )
            )
    )
    public BizResult<Object> list(@Parameter PageParam<Drugstore> pageParam,
                                  @Parameter SearchDrugstoreRequest request) {
        return BizResult.success(drugstoreService.getPage(pageParam, request));
    }

}
