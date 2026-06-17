package com.aihoo.api.patient.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "订单详情vo")
public class MdtOrderViewVo {
    @Schema(description = "主键ID")
    private String id;
    @Schema(description = "订单号")
    private String orderNum;
    @Schema(description = "支付金额")
    private BigDecimal totalPrice;
    @Schema(description = "患者用户id")
    private String patientUserId;
    @Schema(description = "就诊人id")
    private String hosSickId;
    @Schema(description = "订单状态")
    private String status;
    @Schema(description = "医生ID")
    private String doctorUserId;
    @Schema(description = "医生名字")
    private String doctorUserName;
    @Schema(description = "支付时间")
    private String payTime;
    @Schema(description = "药态")
    private String medicineStatusCode;
    @Schema(description = "药房ID")
    private String drugstoreId;
    @Schema(description = "药房名称")
    private String drugstoreName;
    @Schema(description = "药品照片")
    private String pic;
    @Schema(description = "处方详情")
    private MdtOrderViewPrescriptionVo prescription;
}
