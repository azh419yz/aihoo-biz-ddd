package com.aihoo.domain.logistics.dto.sf;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 顺丰价格查询请求。
 */
@Data
public class OrderPriceRequest {
    /** 时效代码 (1:特快, 2:标快, 5:次晨, 6:即日) */
    @JSONField(name = "businessType")
    private String businessType;

    /** 货物总重量 (kg) */
    @JSONField(name = "weight")
    private Double weight;

    /** 货物体积 */
    @JSONField(name = "volume")
    private Double volume;

    /** 寄件时间 (YYYY-MM-DD HH24:MM:SS) */
    @JSONField(name = "consignedTime")
    private String consignedTime;

    /** 是否查询价格 (1:是, 0:否) */
    @JSONField(name = "searchPrice")
    private String searchPrice;

    /** 目的地信息 */
    @JSONField(name = "destAddress")
    private AddressRequest destAddress;

    /** 原寄地信息 */
    @JSONField(name = "srcAddress")
    private AddressRequest srcAddress;

    /** 月结卡号 */
    @JSONField(name = "monthlyCard")
    private String monthlyCard;
}