package com.aihoo.domain.logistics.dto.sl;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 顺丰价格响应包装。
 */
@Data
public class OrderPriceRespWrapperDto {
    @JSONField(name = "deliverTmDto")
    private List<OrderPriceRespDto> deliverTmDto;
}