package com.aihoo.api.doctor.vo;

import com.aihoo.domain.visit.dto.PrescriptionConsultationFeeDto;
import com.aihoo.domain.visit.dto.PrescriptionDrugDto;
import com.aihoo.domain.visit.dto.PrescriptionInstructionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 医生端-最近处方 VO（由 RecentPreDto 转换）。
 */
@Data
@Schema(description = "医生端-最近处方 VO")
public class RecentPreVo {

    private String id;
    private String patientUserId;
    private String hosSickId;
    private String doctorUserId;
    private String doctorUserName;
    private String visitMdtNum;
    private String name;
    private String sex;
    private String age;
    private String disease;
    private String syndrome;
    private String drugstoreProvincesCode;
    private String status;
    private String drugstoreCityCode;
    private String drugstoreId;
    private String medicineStatusCode;
    private String checkTime;
    private String checkPharmaceutist;
    private Integer confirmedStatus;
    private BigDecimal expressPrice;

    private List<PrescriptionDrugDto> drugList;
    private PrescriptionInstructionDto instruction;
    private PrescriptionConsultationFeeDto consultationFee;
    private String img;

    private String createTime;
    private String updateTime;
    private String type;
    private String otherId;
    private String orderNum;
    private String prescriptionNum;
    private String feeType;
    private String idCard;
    private String mobile;
    private String departCode;
    private String departName;
    private String medicalCertificate;
    private String seal;
    private String doctorSignet;
    private String checkStatus;
    private String checkPharmaceutistId;
    private String checkContent;
    private String checkReturn;
    private String totalPrice;
    private String payType;
    private String payTime;
    private String isPay;
    private String endTime;
    private String isCancel;
    private String kidneyStatus;
    private String liverStatus;
    private String womanStatus;
    private String allegeName;
    private String isDisable;
    private String isPush;
    private String checkTimeout;
    private String manualCheckContent;
    private String manualCheckTime;
    private String manualCheckPharmaceutist;
    private String manualCheckPharmaceutistId;
    private String isCanForce;
    private String manualCheckReturn;
}
