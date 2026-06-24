package com.aihoo.domain.logistics.dto.sl;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class OrderPriceRespWrapperDto {
    @JSONField(name = "deliverTmDto")
    private List<OrderPriceRespDto> deliverTmDto;
}