package com.aihoo.api.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "医生用户详情 VO")
public class DoctorUserVo {

    @Schema(name = "id", description = "主键ID", example = "1")
    private String id;

    @Schema(name = "mobile", description = "手机号", example = "178****7671")
    private String mobile;

    @Schema(name = "headImg", description = "头像")
    private String headImg;

    @Schema(name = "name", description = "姓名", example = "华佗")
    private String name;

    @Schema(name = "tag", description = "标签", example = "专业/负责")
    private String tag;

    @Schema(name = "memberNum", description = "工号", example = "D000010")
    private String memberNum;

    @Schema(name = "hospitalId", description = "医院id", example = "1")
    private String hospitalId;

    @Schema(name = "hospitalName", description = "就职医院", example = "复旦大学附属华山医院")
    private String hospitalName;

    @Schema(name = "departId", description = "科室id", example = "1")
    private String departId;

    @Schema(name = "departCode", description = "科室编码", example = "0402")
    private String departCode;

    @Schema(name = "departName", description = "所在科室", example = "神经外科")
    private String departName;

    @Schema(name = "officeHolderCode", description = "职称编码", example = "231")
    private String officeHolderCode;

    @Schema(name = "officeHolderName", description = "职称", example = "主任医师")
    private String officeHolderName;

    @Schema(name = "beGoodAtText", description = "擅长")
    private String beGoodAtText;

    @Schema(name = "introductionText", description = "简介")
    private String introductionText;

    @Schema(name = "isAuth", description = "是否CA认证 NONE-未认证 WAIT-认证中 PASS-已认证 REJECT-认证失败", example = "PASS")
    private String isAuth;

    @Schema(name = "token", description = "登录token")
    private String token;

    @Schema(name = "positionCode", description = "职务编码", example = "411")
    private String positionCode;

    @Schema(name = "positionName", description = "职务", example = "正高级")
    private String positionName;

    @Schema(name = "userSig", description = "腾讯IM")
    private String userSig;

    @Schema(name = "status", description = "状态(是否启用 1:启用 0:停用)", example = "1")
    private String status;

    @Schema(name = "isCancel", description = "是否注销 0 否 1是", example = "1")
    private String isCancel;

    @Schema(name = "prescriptionCount", description = "处方数", example = "1")
    private Long prescriptionCount;

    @Schema(name = "visitCount", description = "患者数", example = "1")
    private Long visitCount;

    @Schema(name = "proposalCount", description = "评价数", example = "1")
    private Long proposalCount;
}
