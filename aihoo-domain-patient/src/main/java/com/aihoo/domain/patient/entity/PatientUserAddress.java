package com.aihoo.domain.patient.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_patient_user_address")
public class PatientUserAddress implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long patientUserId;

    private String receiverName;

    private String receiverPhone;

    private String areaCode;

    private String areaName;

    private String detailAddress;

    private String createTime;

    private String updateTime;

    private Integer isDefault;
}
