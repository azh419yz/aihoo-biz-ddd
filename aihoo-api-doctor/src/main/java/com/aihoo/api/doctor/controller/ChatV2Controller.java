package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.controller.request.VisitChatRequest;
import com.aihoo.common.BizResult;
import com.aihoo.domain.visit.dto.VisitChatVo;
import com.aihoo.domain.visit.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "ChatV2", description = "医生端-问诊复诊相关接口")
@RestController
@RequestMapping("/api/v2/doctorChat")
@RequiredArgsConstructor
public class ChatV2Controller {
    private final ChatService chatService;

    @PostMapping("/startVistChat")
    @Operation(summary = "开始问诊")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, VisitChatVo.class},
                            description = "开始问诊"
                    )
            )
    )
    public BizResult<VisitChatVo> startVisitChat(@Validated @RequestBody VisitChatRequest request) {
        return BizResult.success(chatService.startVisitChatV2(request.getId()));
    }

    @PostMapping("/stopVistChat")
    @Operation(summary = "结束问诊")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, VisitChatVo.class},
                            description = "结束问诊"
                    )
            )
    )
    public BizResult<VisitChatVo> stopVisitChat(@Validated @RequestBody VisitChatRequest request) {
        return BizResult.success(chatService.stopVisitChatV2(request.getId()));
    }

    @PostMapping("/startRevisitChat")
    @Operation(summary = "开始复诊")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, VisitChatVo.class},
                            description = "开始复诊"
                    )
            )
    )
    public BizResult<VisitChatVo> startRevisitChat(@Validated @RequestBody VisitChatRequest request) {
        return BizResult.success(chatService.startRevisitChatV2(request.getId()));
    }

    @PostMapping("/stopRevisitChat")
    @Operation(summary = "结束复诊")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, VisitChatVo.class},
                            description = "结束复诊"
                    )
            )
    )
    public BizResult<VisitChatVo> stopRevisitChat(@Validated @RequestBody VisitChatRequest request) {
        return BizResult.success(chatService.stopRevisitChatV2(request.getId()));
    }

}
