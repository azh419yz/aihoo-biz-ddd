package com.aihoo.api.doctor.controller;


import com.aihoo.common.BaseController;
import com.aihoo.common.BizResult;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.dto.SearchDrugRequest;
import com.aihoo.domain.hospital.model.entity.Drug;
import com.aihoo.domain.hospital.service.DrugService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处方药品管理
 */
@Tag(name = "Drug", description = "医生端-药品相关接口")
@RestController
@RequestMapping("/api/v2/drug")
@RequiredArgsConstructor
public class DrugController extends BaseController {

    private final DrugService drugService;

    /**
     * 药品管理多条件分页查询
     *
     * @param pageParam 分页参数
     * @param request   入参
     * @return {}
     */
    @Operation(summary = "药品列表")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, PageResult.class},
                            description = "药品列表"
                    )
            )
    )
    @GetMapping("/list")
    public BizResult<Object> list(@ParameterObject PageParam<Drug> pageParam,
                                  @ParameterObject SearchDrugRequest request) {
        return BizResult.success(drugService.getPage(pageParam, request));
    }

}
