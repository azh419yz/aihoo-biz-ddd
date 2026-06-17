package com.aihoo.api.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "处方笺VO（内部 API 用）")
public class HosPrescriptionInnerVo {
    @Schema(description = "处方ID")
    private String id;
    @Schema(description = "创建时间")
    private String createTime;
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "性别")
    private String sex;
    @Schema(description = "年龄")
    private String age;
    @Schema(description = "医嘱")
    private String advice;
    @Schema(description = "辨病辨证")
    private String diseaseSyndrome;
    @Schema(description = "科室")
    private String departName;
    @Schema(description = "药房名称")
    private String drugstoreName;
    @Schema(description = "药态")
    private String medicineStatusCode;
    @Schema(description = "药品")
    private List<PrescriptionDrugVo> drugVoList;
    @Schema(description = "用法用量")
    private String method;
    @Schema(description = "收货信息")
    private String receiveMsg;
    @Schema(description = "患者备注")
    private String hosSickRemark;
    @Schema(description = "医生姓名")
    private String doctorName;
    @Schema(description = "审核医生姓名")
    private String checkDoctorName;
    @Schema(description = "调配医生姓名")
    private String allocateDoctorName;
}
