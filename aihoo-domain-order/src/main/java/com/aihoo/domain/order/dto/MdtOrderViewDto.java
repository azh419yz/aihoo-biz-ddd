package com.aihoo.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "订单详情vo")
public class MdtOrderViewDto {
    private String id;
    private String orderNum;
    private BigDecimal totalPrice;
    private String patientUserId;
    private String hosSickId;
    private String status;
    private String doctorUserId;
    private String doctorUserName;
    private String payTime;
    private String medicineStatusCode;
    private String drugstoreId;
    private String drugstoreName;
    private String pic;
    private MdtOrderViewPrescriptionDto prescription;
}
