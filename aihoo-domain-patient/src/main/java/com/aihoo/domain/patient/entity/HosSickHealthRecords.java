package com.aihoo.domain.patient.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 就诊人健康档案（迁自 patient-api 的 HosSickHealthRecords）。
 */
@Data
@TableName("t_hos_sick_health_records")
public class HosSickHealthRecords implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long hosSickId;

    private Integer idCardVerify;

    private String area;

    private String areaName;

    private String height;

    private String weight;

    private String pastHistory;

    private String allergyHistory;

    private String tongueImages;

    private String faceImages;

    private String medicalRecordImages;

    private String createTime;

    private String updateTime;
}