package com.aihoo.domain.im.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "IM消息内容视图")
public class ImMsgContentDto {
    private String msgType;
    private String msgTypeName;
    private String msgContent;
    private String imMsgId;
}
