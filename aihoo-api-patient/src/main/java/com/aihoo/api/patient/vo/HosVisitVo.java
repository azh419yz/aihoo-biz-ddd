package com.aihoo.api.patient.vo;

import com.aihoo.domain.visit.entity.HosPrescription;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 问诊单 VO（迁自 patient-api 的 HosVisitVo）。
 */
@Data
public class HosVisitVo {

    private String id;
    private String createTime;
    private String updateTime;
    private String patientUserId;
    private String hosSickId;
    private String name;
    private String idCard;
    private String sex;
    private String age;
    private String mobile;
    private String content;
    private String type;
    private String totalPrice;
    private String payType;
    private String payTime;
    private String status;
    private String msg;
    private String orderNum;
    private String fiveStar;
    private String healthInfo;
    private String baseInfo;
    private String doctorUserId;
    private String infoSubmitTime;
    private String haveTime;
    private String doctorAdvice;
    private String firstVisit;
    private String startTime;
    private String endTime;
    private String isReadIm;
    private String isPay;

    private String imGroupId;

    @Schema(description = "医生姓名")
    private String doctorName;

    @Schema(description = "医生头像")
    private String doctorHeadImg;

    @Schema(description = "处方")
    private List<HosPrescription> hosPrescriptions;
}