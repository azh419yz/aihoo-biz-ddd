package com.aihoo.domain.logistics.dto.sl;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 顺丰价格查询响应。
 */
@Data
public class OrderPriceRespDto {
    @JSONField(name = "businessType")
    private String businessType;

    @JSONField(name = "businessTypeDesc")
    private String businessTypeDesc;

    @JSONField(name = "deliverTime")
    private String deliverTime;

    @JSONField(name = "fee")
    private BigDecimal fee;

    @JSONField(name = "searchPrice")
    private String searchPrice;

    @JSONField(name = "closeTime")
    private String closeTime;
}