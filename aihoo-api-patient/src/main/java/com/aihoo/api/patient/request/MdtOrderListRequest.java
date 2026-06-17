package com.aihoo.api.patient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单")
public class MdtOrderListRequest {
    @Schema(description = "分页参数，页数")
    private Integer page;
    @Schema(description = "分页参数，数据量")
    private Integer limit;
}
