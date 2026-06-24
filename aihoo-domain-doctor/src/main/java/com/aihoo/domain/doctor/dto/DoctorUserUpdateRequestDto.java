package com.aihoo.domain.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "医生更新请求")
public class DoctorUserUpdateRequestDto {

    @Schema(description = "医生id")
    @NotBlank(message = "请携带医生id")
    private String id;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "医院id")
    private String hospitalId;

    @Schema(description = "科室id")
    private String departId;

    @Schema(description = "科室编码")
    private String departCode;

    @Schema(description = "职称编码")
    private String officeHolderCode;

    @Schema(description = "擅长")
    private String beGoodAtText;

    @Schema(description = "简介")
    private String introductionText;

    @Schema(description = "人员类别编码")
    private String personTypeCode;

    @Schema(description = "职务编码")
    private String positionCode;

    @Schema(description = "证件类型编码")
    private String papersCode;

    @Schema(description = "证件号码")
    private String papersNumbers;

    @Schema(description = "头像")
    private String headImg;

    @Schema(description = "标签 以|分隔")
    private String tag;

    @Schema(description = "排序")
    private String index;

    @Schema(description = "成就")
    private String achievement;

    @Schema(description = "会诊医生类型 ASSISTANT/CONSULTANT")
    private String doctorType;

    @Schema(description = "医师资格证-页一")
    private String medicalLicensePageOne;
    @Schema(description = "医师资格证-页二")
    private String medicalLicensePageTwo;
    @Schema(description = "医师资格证-编号")
    private String medicalLicenseNo;
    @Schema(description = "医师资格证-发证日期")
    private String medicalLicenseIssueDate;

    @Schema(description = "执业证-页一")
    private String practiceCertificatePageOne;
    @Schema(description = "执业证-页二")
    private String practiceCertificatePageTwo;
    @Schema(description = "执业证-编号")
    private String practiceCertificateNo;
    @Schema(description = "执业证-发证日期")
    private String practiceCertificateIssueDate;

    @Schema(description = "地区")
    private String area;
}