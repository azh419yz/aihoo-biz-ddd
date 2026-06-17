package com.aihoo.api.patient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建订单对象")
public class MdtOrderSaveRequest {
    @Schema(description = "患者用户id")
    private String patientUserId;
    @Schema(description = "就诊人id")
    private String hosSickId;
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "性别 0-女 1-男")
    private String sex;
    @Schema(description = "处方ID")
    private String preId;
    @Schema(description = "年龄")
    private String age;
    @Schema(description = "手机号")
    private String mobile;
    @Schema(description = "医生id")
    private String doctorUserId;
    @Schema(description = "药态")
    private String medicineStatusCode;
    @Schema(description = "支付金额")
    private String totalPrice;
    @Schema(description = "药房ID")
    private String drugstoreId;
    @Schema(description = "患者备注")
    private String hosSickRemark;
    @Schema(description = "收件人姓名")
    private String receiveName;
    @Schema(description = "收件人电话")
    private String receivePhone;
    @Schema(description = "收件人区域")
    private String receiveArea;
    @Schema(description = "收件人地址")
    private String receiveAddress;
}
