package com.aihoo.api.patient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MdtOrderPayRequest {
    @Schema(name = "orderNum", description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNum;
}
