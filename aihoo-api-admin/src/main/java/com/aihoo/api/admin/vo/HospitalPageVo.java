package com.aihoo.api.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 医院分页列表 VO（admin: HospitalController.list 出参）。
 *
 * <p>原 aihoo-biz-service/aihoo-admin 的 HospitalPageVo，去掉 doctorCount 字段（按 user 决策不调 doctor 域）。
 */
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
