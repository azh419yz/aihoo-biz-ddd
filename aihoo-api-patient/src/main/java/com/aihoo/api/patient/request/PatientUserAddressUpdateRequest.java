package com.aihoo.api.patient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "更新收货地址请求对象")
public class PatientUserAddressUpdateRequest {
    @NotNull(message = "id不能为空")
    @Schema(name = "id", description = "地址id", example = "1")
    private Long id;

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

    @Schema(name = "isDefault", description = "是否是默认地址 1:是 0:不是", example = "1")
    private Integer isDefault;
}
