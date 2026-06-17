package com.aihoo.api.patient.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "购药订单 VO")
public class MdtOrderPageVo {
    @Schema(name = "id", description = "主键ID")
    private String id;
    @Schema(name = "orderName", description = "订单名称")
    private String orderName;
    @Schema(name = "orderNum", description = "订单号")
    private String orderNum;
    @Schema(name = "consultationDoctorName", description = "会诊医生")
    private String consultationDoctorName;
    @Schema(name = "totalPrice", description = "支付金额")
    private String totalPrice;
    @Schema(name = "payTime", description = "付款时间")
    private String payTime;
    @Schema(name = "status", description = "状态")
    private String status;
    @Schema(name = "mdtHospital", description = "所属医院")
    private String mdtHospital;
}
