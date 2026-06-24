package com.aihoo.domain.logistics.dto.sf;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class OrderPriceDto {
    
    @JSONField(name = "businessType")
    private String businessType;

    
    @JSONField(name = "weight")
    private Double weight;

    
    @JSONField(name = "volume")
    private Double volume;

    
    @JSONField(name = "consignedTime")
    private String consignedTime;

    
    @JSONField(name = "searchPrice")
    private String searchPrice;

    
    @JSONField(name = "destAddress")
    private AddressDto destAddress;

    
    @JSONField(name = "srcAddress")
    private AddressDto srcAddress;

    
    @JSONField(name = "monthlyCard")
    private String monthlyCard;
}