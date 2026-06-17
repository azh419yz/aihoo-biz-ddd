package com.aihoo.api.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "中医辨病 VO")
public class TcmDiseaseVo {
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
