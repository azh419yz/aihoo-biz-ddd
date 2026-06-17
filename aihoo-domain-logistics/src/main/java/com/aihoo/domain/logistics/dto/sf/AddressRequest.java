package com.aihoo.domain.logistics.dto.sf;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 顺丰地区查询请求。
 */
@Data
public class AddressRequest {
    @JSONField(name = "province")
    private String province;

    @JSONField(name = "city")
    private String city;

    @JSONField(name = "district")
    private String district;

    @JSONField(name = "address")
    private String address;

    @JSONField(name = "code")
    private String code;
}