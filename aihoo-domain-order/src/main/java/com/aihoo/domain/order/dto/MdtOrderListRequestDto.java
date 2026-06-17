package com.aihoo.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单列表请求")
public class MdtOrderListRequestDto {
    private Integer page;
    private Integer limit;
}
