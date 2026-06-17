package com.aihoo.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "购药订单 VO")
public class MdtOrderPageVo {
    private String id;
    private String orderName;
    private String orderNum;
    private String consultationDoctorName;
    private String totalPrice;
    private String payTime;
    private String status;
    private String mdtHospital;
}
