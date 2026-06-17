package com.aihoo.api.patient.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "患者用户详情 VO")
public class PatientUserApiVo {
    @Schema(name = "id", description = "主键ID", example = "1")
    private String id;
    @Schema(name = "mobile", description = "手机号", example = "13800000001")
    private String mobile;
    @Schema(name = "wechatOpenId", description = "微信openId", example = "1")
    private String wechatOpenId;
    @Schema(name = "nickName", description = "昵称", example = "哈吉米")
    private String nickName;
    @Schema(name = "headImg", description = "头像", example = "https://oss.aliyun.com/xxsd")
    private String headImg;
    @Schema(name = "name", description = "姓名", example = "张三")
    private String name;
    @Schema(name = "idCard", description = "身份证", example = "310765199001015432")
    private String idCard;
    @Schema(name = "sex", description = "性别 0-女 1-男", example = "1")
    private String sex;
    @Schema(name = "birthDay", description = "生日", example = "1990-01-01")
    private String birthDay;
    @Schema(name = "imUserId", description = "通信 IM 的用户ID", example = "PATIENT_1")
    private String imUserId;
    @Schema(name = "imUserSig", description = "通信 IM 的密码", example = "123456")
    private String imUserSig;
    @Schema(name = "orderCount", description = "订单数量", example = "1")
    private Long orderCount;
    @Schema(name = "hosSickCount", description = "患者数量", example = "1")
    private Long hosSickCount;
    @Schema(name = "visitCount", description = "问诊数量", example = "1")
    private Long visitCount;
    @Schema(name = "token", description = "认证token", example = "77a1c2165a5c4f0093bc8bd354232f56")
    private String token;
    @Schema(name = "allowPrivacyPolicy", description = "是否已授权隐私协议", example = "true")
    private Boolean allowPrivacyPolicy;
}
