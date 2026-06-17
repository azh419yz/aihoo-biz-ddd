package com.aihoo.api.patient.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单选择页面药店对象")
public class VisitSelectDrugstoreVo {
    @Schema(description = "药店id")
    private Long drugstoreId;
    @Schema(description = "药店名称")
    private String drugstoreName;
    @Schema(description = "药材费用")
    private Double drugPrice;
}
