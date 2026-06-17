package com.aihoo.api.patient.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DoctorUserVo {

    @Schema(name = "id", description = "主键ID", example = "1")
    private String id;

    @Schema(name = "headImg", description = "头像", example = "https://oss-aliyun.com/xxx")
    private String headImg;

    @Schema(name = "name", description = "姓名", example = "华佗")
    private String name;

    @Schema(name = "tag", description = "标签 以|分隔", example = "1/2/3")
    private String tag;

    @Schema(name = "memberNum", description = "工号", example = "12345")
    private String memberNum;

    @Schema(name = "hospitalId", description = "医院id", example = "1")
    private String hospitalId;

    @Schema(name = "hospitalName", description = "就职医院", example = "复旦大学附属华山医院")
    private String hospitalName;

    @Schema(name = "departId", description = "科室id", example = "41")
    private String departId;

    @Schema(name = "departCode", description = "科室编码", example = "0303")
    private String departCode;

    @Schema(name = "departName", description = "所在科室", example = "神经内科")
    private String departName;

    @Schema(name = "officeHolderCode", description = "职称编码", example = "231")
    private String officeHolderCode;

    @Schema(name = "officeHolderName", description = "职称", example = "主任医师")
    private String officeHolderName;

    @Schema(name = "beGoodAtText", description = "擅长")
    private String beGoodAtText;

    @Schema(name = "achievement", description = "成就")
    private String achievement;

    @Schema(name = "introductionText", description = "简介")
    private String introductionText;

    @Schema(name = "personTypeCode", description = "人员类别编码")
    private String personTypeCode;

    @Schema(name = "personTypeName", description = "人员类别")
    private String personTypeName;

    @Schema(name = "positionCode", description = "职务编码")
    private String positionCode;

    @Schema(name = "positionName", description = "职务")
    private String positionName;

    @Schema(name = "papersCode", description = "证件类型编码")
    private String papersCode;

    @Schema(name = "papersName", description = "证件")
    private String papersName;

    @Schema(name = "papersNumbers", description = "证件号码")
    private String papersNumbers;

    @Schema(name = "sex", description = "医生性别")
    private String sex;

    @Schema(name = "status", description = "状态(是否启用 1:启用 0:停用)")
    private String status;

    @Schema(name = "isImg", description = "是否开启图文问诊 0-未开 1-开启")
    private Integer isImg;

    @Schema(name = "imgPrice", description = "图文问诊价格")
    private Integer imgPrice;

    @Schema(name = "upperLimit", description = "接单上限")
    private Integer upperLimit;

    @Schema(name = "isDisturb", description = "是否开启免打扰时段")
    private Integer isDisturb;

    @Schema(name = "noDisturbTime", description = "免打扰时段")
    private String noDisturbTime;
}