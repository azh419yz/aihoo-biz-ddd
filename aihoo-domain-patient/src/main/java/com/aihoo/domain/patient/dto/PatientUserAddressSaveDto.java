package com.aihoo.domain.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "保存收货地址")
public class PatientUserAddressSaveDto {
    private String receiverName;
    private String receiverPhone;
    private String areaCode;
    private String areaName;
    private String detailAddress;
}
