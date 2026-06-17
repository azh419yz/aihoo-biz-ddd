package com.aihoo.api.doctor.request;

import com.aihoo.domain.visit.dto.PrescriptionConsultationFeeDto;
import com.aihoo.domain.visit.dto.PrescriptionDrugDto;
import com.aihoo.domain.visit.dto.PrescriptionInstructionDto;
import lombok.Data;

import java.util.List;

/**
 * 医生端-开方保存请求。
 */
@Data
public class SavePrescriptionRequest {

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
