package com.aihoo.api.doctor.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "im消息撤回")
public class ImWithdrawMsgRequest {

    @NotNull(message = "群组ID不能为空")
    @Schema(name = "groupId", description = "群组ID")
    private String groupId;

    @NotNull(message = "消息唯一值不能为空")
    @Schema(name = "msgReq", description = "唯一值")
    private String msgReq;
}
