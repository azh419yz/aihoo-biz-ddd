package com.aihoo.domain.tcm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 中医辨病 DTO（domain 层用，承载 service 返回值；由 controller 转 vo）。
 */
@Data
@Schema(description = "中医辨病 DTO")
public class TcmDiseaseDto {
    private Long id;
    private String diseaseName;
    private String diseasePinyin;
    private String diseasePinyinInitial;
    private String diseaseEnglish;
    private String diseaseAlias;
    private String diseaseCategory;
    private String diseaseDescription;
    private String commonSymptoms;
    private String mainFeatures;
    private String causeAnalysis;
    private String prognosis;
    private String prevention;
    private String remark;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
