package com.aihoo.api.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "中医辨证 VO")
public class TcmSyndromeVo {
    private Long id;
    private Long diseaseId;
    private String syndromeName;
    private String syndromeType;
    private String mainSymptoms;
    private String secondarySymptoms;
    private String tonguePulse;
    private String pathogenesis;
    private String treatmentPrinciple;
    private String recommendedFormula;
    private String modifiedFormula;
    private String acupoints;
    private String dietaryAdvice;
    private String dailyRegimen;
    private String prognosis;
    private String differentialDiagnosis;
    private String remark;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
