package com.aihoo.domain.visit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "问诊复诊 DTO")
public class VisitChatDto {

    @Schema(name = "msg", description = "返回信息", example = "开始在线复诊")
    private String msg;

    @Schema(name = "orderStatus", description = "订单状态", example = "HAVE")
    private String orderStatus;

    @Schema(name = "cutDown", description = "问诊时间", example = "1")
    private String cutDown;

    @Schema(name = "isCanChat", description = "是否可以咨询 1:是 0:否", example = "1")
    private String isCanChat;
}
