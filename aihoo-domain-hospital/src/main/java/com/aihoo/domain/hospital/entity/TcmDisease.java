package com.aihoo.domain.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("tcm_disease")
public class TcmDisease implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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
