package com.aihoo.domain.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "患者用户详情 VO")
public class PatientUserVo {
    private String id;
    private String mobile;
    private String wechatOpenId;
    private String nickName;
    private String headImg;
    private String name;
    private String idCard;
    private String sex;
    private String birthDay;
    private String imUserId;
    private String imUserSig;
    private Long orderCount;
    private Long hosSickCount;
    private Long visitCount;
    private String token;
    private Boolean allowPrivacyPolicy;
}
