package com.aihoo.domain.visit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 处方诊金表（迁自 patient-api 的 HosPrescriptionFee）。
 */
@Data
@TableName("t_hos_prescription_fee")
public class HosPrescriptionFee implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;

    private String updateTime;

    private String hosPrescriptionId;

    private String visitAmount;

    private String afterService;

    private String seePrescription;

    private String drugAmount;

    private String decoctionAmount;

    private String serviceAmount;

    private String amount;
}