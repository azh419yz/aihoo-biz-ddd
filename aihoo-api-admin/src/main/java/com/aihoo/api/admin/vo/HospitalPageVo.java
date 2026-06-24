package com.aihoo.api.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "医院管理分页响应")
public class HospitalPageVo {

    private String id;
    private String hospitalNo;
    private String createUserId;
    private String hosName;
    private String hosGradeCode;
    private String hosGradeName;
    private String hosLevelCode;
    private String hosLevelName;
    private String hosCateCode;
    private String hosCateName;
    private String hosAttCode;
    private String hosAttName;
    private String status;
}
