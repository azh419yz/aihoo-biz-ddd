package com.aihoo.domain.visit.dto;

import lombok.Data;

import java.util.List;

@Data
public class HosPrescriptionInnerDto {
    private String id;
    private String createTime;
    private String name;
    private String sex;
    private String age;
    private String advice;
    private String diseaseSyndrome;
    private String departName;
    private String drugstoreName;
    private String medicineStatusCode;
    private String method;
    private String receiveMsg;
    private String hosSickRemark;
    private String doctorName;
    private String checkDoctorName;
    private String allocateDoctorName;
    private List<PrescriptionDrugInnerDto> drugVoList;
}
