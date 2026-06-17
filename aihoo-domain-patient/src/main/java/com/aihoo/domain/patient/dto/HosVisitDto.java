package com.aihoo.domain.patient.dto;

import com.aihoo.domain.visit.entity.HosPrescription;
import lombok.Data;

import java.util.List;

/**
 * 问诊 dto（domain 层用，承载 service 返回值）。
 */
@Data
public class HosVisitDto {

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

    private String doctorName;
    private String doctorHeadImg;

    private List<HosPrescription> hosPrescriptions;
}