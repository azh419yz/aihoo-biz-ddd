package com.aihoo.domain.visit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RecentPreDto {

    @Schema(name = "id", description = "主键ID", example = "1")
    private String id;

    @Schema(name = "patientUserId", description = "患者用户ID", example = "1")
    private String patientUserId;

    @Schema(name = "hosSickId", description = "就诊人ID", example = "1")
    private String hosSickId;

    @Schema(name = "doctorUserId", description = "医生ID", example = "1")
    private String doctorUserId;

    @Schema(name = "doctorUserName", description = "医生名称", example = "1")
    private String doctorUserName;

    @Schema(name = "visitMdtNum", description = "订单编号", example = "V20260228161121005")
    private String visitMdtNum;

    @Schema(name = "name", description = "姓名", example = "张三")
    private String name;

    @Schema(name = "sex", description = "性别 0-女 1-男", example = "1")
    private String sex;

    @Schema(name = "age", description = "年龄", example = "18")
    private String age;

    @Schema(name = "disease", description = "辨病", example = "失眠")
    private String disease;

    @Schema(name = "syndrome", description = "辩证", example = "风热,气虚")
    private String syndrome;

    @Schema(name = "drugstoreProvincesCode", description = "药店所在省", example = "110000")
    private String drugstoreProvincesCode;

    @Schema(name = "status", description = "处方状态", example = "")
    private String status;

    @Schema(name = "drugstoreCityCode", description = "药店所在市", example = "110100")
    private String drugstoreCityCode;

    @Schema(name = "drugstoreId", description = "药店ID", example = "1")
    private String drugstoreId;

    @Schema(name = "medicineStatusCode", description = "药态 1:中药饮片-自煎 2:中药饮片-代煎 3:颗粒", example = "2")
    private String medicineStatusCode;

    @Schema(name = "checkTime", description = "药剂师审核时间", example = "2")
    private String checkTime;

    @Schema(name = "checkPharmaceutist", description = "药剂师名字", example = "2")
    private String checkPharmaceutist;

    @Schema(name = "confirmedStatus", description = "患者确认状态 0 没有 1确认")
    private Integer confirmedStatus;

    @Schema(name = "expressPrice", description = "物流费用")
    private BigDecimal expressPrice;

    @Schema(name = "drugList", description = "药品", example = "[{'id': '1', 'name': '葛根', 'price': '2.15', 'count': '10'}]")
    private List<PrescriptionDrugDto> drugList;

    @Schema(name = "instruction", description = "用法", example = "")
    private PrescriptionInstructionDto instruction;

    @Schema(name = "consultationFee", description = "诊金", example = "")
    private PrescriptionConsultationFeeDto consultationFee;

    @Schema(name = "img", description = "电子处方图片URL")
    private String img;

    private String createTime;
    private String updateTime;
    private String type;
    private String otherId;
    private String orderNum;
    private String prescriptionNum;
    private String feeType;
    private String idCard;
    private String mobile;
    private String departCode;
    private String departName;
    private String medicalCertificate;
    private String seal;
    private String doctorSignet;
    private String checkStatus;
    private String checkPharmaceutistId;
    private String checkContent;
    private String checkReturn;
    private String totalPrice;
    private String payType;
    private String payTime;
    private String isPay;
    private String endTime;
    private String isCancel;
    private String kidneyStatus;
    private String liverStatus;
    private String womanStatus;
    private String allegeName;
    private String isDisable;
    private String isPush;
    private String check_timeout;
    private String manualCheckContent;
    private String manualCheckTime;
    private String manualCheckPharmaceutist;
    private String manualCheckPharmaceutistId;
    private String isCanForce;
    private String manualCheckReturn;
}