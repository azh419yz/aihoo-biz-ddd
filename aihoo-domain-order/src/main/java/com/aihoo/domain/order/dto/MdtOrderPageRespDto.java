package com.aihoo.domain.order.dto;

import lombok.Data;

/**
 * 订单分页响应 DTO（domain 内）。
 */
@Data
public class MdtOrderPageRespDto {
    private String id;
    private String orderName;
    private String orderNum;
    private String consultationDoctorName;
    private String totalPrice;
    private String payTime;
    private String status;
    private String mdtHospital;
}
