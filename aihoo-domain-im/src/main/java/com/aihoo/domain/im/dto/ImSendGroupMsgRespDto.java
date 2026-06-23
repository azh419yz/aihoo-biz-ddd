package com.aihoo.domain.im.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "im群发普通消息")
public class ImSendGroupMsgRespDto extends ImRespDto {
    @Schema(description = "消息时间戳")
    @JSONField(name = "MsgTime")
    private Long msgTime;
    @Schema(description = "32位序列号")
    @JSONField(name = "MsgSeq")
    private Long msgSeq;
}
