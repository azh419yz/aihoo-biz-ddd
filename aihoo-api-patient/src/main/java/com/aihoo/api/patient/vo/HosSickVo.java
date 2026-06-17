package com.aihoo.api.patient.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 就诊人 VO（迁自 patient-api 的 HosSickVo）。
 */
@Data
@Schema(description = "就诊人VO")
public class HosSickVo {

    @Schema(name = "id", description = "主键ID", example = "1")
    private String id;

    @Schema(name = "name", description = "姓名", example = "张三")
    private String name;

    @Schema(name = "idCard", description = "身份证", example = "310765199001015432")
    private String idCard;

    @Schema(name = "sex", description = "性别 0-女 1-男", example = "1")
    private String sex;

    @Schema(description = "出生日期(yyyy-MM-dd)", example = "2000-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Schema(name = "status", description = "最新问诊状态 ", example = "问诊中")
    private String status;

    @Schema(name = "avatar", description = "头像", example = "https://oss.aliyun.com/xxx.jpg")
    private String avatar;

    @Schema(name = "imGroupId", description = "问诊中时的群聊ID", example = "GROUP_1")
    private String imGroupId;

    @Schema(name = "imUserId", description = "IM用户ID", example = "PATIENT_1_1")
    private String imUserId;

    @Schema(name = "imUserSig", description = "IM用户签名", example = "abcdefg")
    private String imUserSig;

    @Schema(name = "visitCount", description = "问诊卡数量", example = "1")
    private Integer visitCount;

    @Schema(name = "age", description = "年龄", example = "18")
    private String age;

    @Schema(name = "height", description = "身高", example = "1")
    private String height;

    @Schema(name = "mobile", description = "手机号码", example = "1")
    private String mobile;

    @Schema(name = "idCardVerify", description = "实名制认证 1:通过 0:不通过", example = "1")
    private Integer idCardVerify;

    @Schema(name = "pastHistory", description = "既往史", example = "1")
    private String pastHistory;

    @Schema(name = "area", description = "地区code", example = "1,2,3,4")
    private String area;

    @Schema(name = "areaName", description = "地区名称", example = "1")
    private String areaName;

    @Schema(name = "allergyHistory", description = "过敏史", example = "1")
    private String allergyHistory;

    @Schema(name = "weight", description = "体重", example = "1")
    private String weight;

    @Schema(name = "tongueImages", description = "舌照", example = "")
    private List<String> tongueImages;

    @Schema(name = "faceImages", description = "面照", example = "")
    private List<String> faceImages;

    @Schema(name = "medicalRecordImages", description = "病例", example = "")
    private List<String> medicalRecordImages;

    @Schema(name = "visits", description = "问诊单", example = "")
    private List<HosVisitVo> visits;
}