package com.aihoo.api.patient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "保存收货地址请求对象")
public class PatientUserAddressSaveRequest {
    @Schema(name = "receiverName", description = "收货人", example = "张三")
    private String receiverName;

    @Schema(name = "receiverPhone", description = "收货人号码", example = "13800000000")
    private String receiverPhone;

    @Schema(name = "areaCode", description = "地区编号  逗号隔开", example = "100000,100100")
    private String areaCode;

    @Schema(name = "areaName", description = "地区名称 由编号拼接而来", example = "北京市,北京市")
    private String areaName;

    @Schema(name = "detailAddress", description = "详细地址", example = "xxx路xxx号")
    private String detailAddress;
}
