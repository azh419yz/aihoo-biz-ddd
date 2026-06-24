package com.aihoo.domain.visit.dto;

import lombok.Data;

import java.util.List;

@Data
public class SavePrescriptionDto {

    private String id;
    private String patientUserId;
    private String doctorUserId;
    private String hosSickId;
    private String visitMdtNum;
    private String name;
    private String sex;
    private String age;
    private String idCard;
    private String mobile;
    private String drugstoreId;
    private String drugstoreProvincesCode;
    private String drugstoreCityCode;
    private String medicineStatusCode;
    private String disease;
    private String syndrome;
    private String kidneyStatus;
    private String liverStatus;
    private String womanStatus;
    private String allegeName;

    private List<PrescriptionDrugDto> drugList;
    private PrescriptionInstructionDto instruction;
    private PrescriptionConsultationFeeDto consultationFee;
}
