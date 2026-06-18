package com.aihoo.api.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 医生 VO（运营端 DoctorUserController 出参）。
 */
@Data
@Schema(description = "医生 VO")
public class DoctorUserVo {

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "头像")
    private String headImg;

    @Schema(description = "标签 以|分隔")
    private String tag;

    @Schema(description = "工号")
    private String memberNum;

    @Schema(description = "医院id")
    private String hospitalId;

    @Schema(description = "就职医院")
    private String hospitalName;

    @Schema(description = "科室id")
    private String departId;

    @Schema(description = "科室编码")
    private String departCode;

    @Schema(description = "所在科室")
    private String departName;

    @Schema(description = "职称编码")
    private String officeHolderCode;

    @Schema(description = "职称")
    private String officeHolderName;

    @Schema(description = "擅长")
    private String beGoodAtText;

    @Schema(description = "简介")
    private String introductionText;

    @Schema(description = "成就")
    private String achievement;

    @Schema(description = "人员类别编码")
    private String personTypeCode;

    @Schema(description = "人员类别")
    private String personTypeName;

    @Schema(description = "职务编码")
    private String positionCode;

    @Schema(description = "职务")
    private String positionName;

    @Schema(description = "证件类型编码")
    private String papersCode;

    @Schema(description = "证件")
    private String papersName;

    @Schema(description = "证件号码")
    private String papersNumbers;

    @Schema(description = "性别 0-女 1-男")
    private String sex;

    @Schema(description = "年龄")
    private String age;

    @Schema(description = "生日 MM-DD")
    private String birthday;

    @Schema(description = "状态 0-停用 1-启用")
    private String status;

    @Schema(description = "排序")
    private String index;

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

    @Schema(description = "是否开启图文问诊 0-未开 1-开启")
    private Integer isImg;

    @Schema(description = "图文问诊价格")
    private Integer imgPrice;

    @Schema(description = "接单上限")
    private Integer upperLimit;

    @Schema(description = "是否开启免打扰时段")
    private Integer isDisturb;

    @Schema(description = "免打扰时段")
    private String noDisturbTime;
}