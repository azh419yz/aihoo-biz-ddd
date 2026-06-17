package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.controller.request.TcmSyndromeListReq;
import com.aihoo.common.BaseController;
import com.aihoo.common.BizResult;
import com.aihoo.domain.sys.dto.TcmSyndromeVo;
import com.aihoo.domain.sys.service.TcmSyndromeService;
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
 * 中医证候管理
 */
@Tag(name = "Syndrome", description = "医生端-中医证候接口")
@RestController
@RequestMapping("/api/v2/syndrome")
@RequiredArgsConstructor
public class TcmSyndromeController extends BaseController {

    private final TcmSyndromeService tcmSyndromeService;

    @Operation(summary = "获取中医证候列表")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, TcmSyndromeVo.class},
                            description = "获取中医证候列表"
                    )
            )
    )
    @GetMapping("/list")
    public BizResult<Object> list(@Parameter TcmSyndromeListReq req) {
        return BizResult.success(tcmSyndromeService.getSyndromeList(req));
    }
}
