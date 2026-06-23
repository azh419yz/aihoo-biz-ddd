package com.aihoo.domain.order.dto;

import com.aihoo.domain.visit.entity.HosPrescriptionInstruction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class MdtOrderViewPrescriptionDto {

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "患者用户id")
    private String patientUserId;

    @Schema(description = "就诊人id")
    private String hosSickId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "性别 0-女 1-男")
    private String sex;

    @Schema(description = "年龄")
    private String age;

    @Schema(description = "药态")
    private String medicineStatusCode;

    @Schema(description = "临床诊断")
    private String medicalCertificate;

    @Schema(description = "开方人ID")
    private String doctorUserId;

    @Schema(description = "开方人名称")
    private String doctorUserName;

    @Schema(description = "审核药师")
    private String checkPharmaceutist;

    @Schema(description = "开方时间")
    private String createTime;

    @Schema(description = "处方编号")
    private String orderNum;

    @Schema(description = "药物")
    private List<MdtOrderViewPrescriptionDrugDto> drugs;

    @Schema(description = "用药详情")
    private HosPrescriptionInstruction instruction;
}