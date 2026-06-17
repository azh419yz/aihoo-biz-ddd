package com.aihoo.domain.tcm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 中医辨证 DTO（domain 层用，承载 service 返回值；由 controller 转 vo）。
 */
@Data
@Schema(description = "中医辨证 DTO")
public class TcmSyndromeDto {
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
