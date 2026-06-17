package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.controller.request.TcmDiseaseListReq;
import com.aihoo.common.BaseController;
import com.aihoo.common.BizResult;
import com.aihoo.domain.sys.dto.TcmDiseaseVo;
import com.aihoo.domain.sys.service.TcmDiseaseService;
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

/**
 * 中医疾病管理
 */
@Tag(name = "Disease", description = "医生端-中医疾病接口")
@RestController
@RequestMapping("/api/v2/disease")
@RequiredArgsConstructor
public class TcmDiseaseController extends BaseController {

    private final TcmDiseaseService tcmDiseaseService;

    @Operation(summary = "获取中医疾病列表")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, TcmDiseaseVo.class},
                            description = "获取中医疾病列表"
                    )
            )
    )
    @GetMapping("/list")
    public BizResult<Object> list(@Parameter TcmDiseaseListReq req) {
        return BizResult.success(tcmDiseaseService.getDiseaseList(req));
    }
}
