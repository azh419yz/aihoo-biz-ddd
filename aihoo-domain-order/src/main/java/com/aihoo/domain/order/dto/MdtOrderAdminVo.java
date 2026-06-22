package com.aihoo.domain.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class MdtOrderAdminVo {

    private String id;
    private String name;
    private String sex;
    private String age;
    private String preId;
    private String drugstoreName;
    private String medicineStatusCode;
    private String doseNumber;
    private List<PrescriptionDrugDTO> drugList;
    private String status;
    private String hosSickRemark;
    private String remark;
    private List<String> picList;
    private String pdfFlag;
    private String expressFlag;
    private String receiveName;
    private String receivePhone;
    private String receiveArea;
    private String receiveAddress;
    private String payTime;
}