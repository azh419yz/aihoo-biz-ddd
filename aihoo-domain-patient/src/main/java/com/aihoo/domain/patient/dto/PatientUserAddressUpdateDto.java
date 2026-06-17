package com.aihoo.domain.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新收货地址")
public class PatientUserAddressUpdateDto {
    private Long id;
    private String receiverName;
    private String receiverPhone;
    private String areaCode;
    private String areaName;
    private String detailAddress;
    private Integer isDefault;
}
