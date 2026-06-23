package com.aihoo.domain.im.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "im信息发送回参")
public class ImSendMsgRespDto extends ImRespDto {
    @Schema(description = "消息时间戳")
    private Long MsgTime;
    @Schema(description = "消息唯一标识")
    private String MsgKey;
    @Schema(description = "该条消息在客户端唯一标识")
    private String MsgId;
}
