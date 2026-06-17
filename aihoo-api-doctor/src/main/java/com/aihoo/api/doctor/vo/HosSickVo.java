package com.aihoo.api.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "医生端-患者信息 VO")
public class HosSickVo {

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号码")
    private String mobile;

    @Schema(description = "实名制认证 1:通过 0:不通过")
    private Integer idCardVerify;

    @Schema(description = "证件信息")
    private String idCard;

    @Schema(description = "性别 0-女 1-男")
    private String sex;

    @Schema(description = "年龄")
    private String age;

    @Schema(description = "身高")
    private String height;

    @Schema(description = "体重")
    private String weight;

    @Schema(description = "地区")
    private String area;

    @Schema(description = "地区名称")
    private String areaName;

    @Schema(description = "既往史")
    private String pastHistory;

    @Schema(description = "过敏史")
    private String allergyHistory;

    @Schema(description = "病情描述")
    private String desc;

    @Schema(description = "问诊卡列表")
    private List<HosVisitVo> hosVisits;

    @Schema(description = "舌照")
    private List<String> tongueImages;

    @Schema(description = "面照")
    private List<String> faceImages;

    @Schema(description = "病例")
    private List<String> medicalRecordImages;
}
