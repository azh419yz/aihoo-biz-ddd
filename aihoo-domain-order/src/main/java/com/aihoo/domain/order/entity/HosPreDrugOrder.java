package com.aihoo.domain.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 药品配送信息表。
 * <p>2026-06-18 迁自 patient-api/HosPreDrugOrder。
 */
@Data
@TableName("t_hos_pre_drug_order")
@Schema(description = "药品配送信息表")
public class HosPreDrugOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

    @TableField("shippend_start_time")
    private String shippendStartTime;

    @TableField("shippend_end_time")
    private String shippendEndTime;

    @TableField("is_pre")
    private String isPre;

    @TableField("hos_prescription_id")
    private String hosPrescriptionId;

    @TableField("order_num")
    private String orderNum;

    @TableField("express_code")
    private String expressCode;

    @TableField("express_name")
    private String expressName;

    @TableField("shipping_no")
    private String shippingNo;

    private String name;
    private String mobile;

    @TableField("province_code")
    private String provinceCode;

    @TableField("city_code")
    private String cityCode;

    @TableField("district_code")
    private String districtCode;

    private String province;
    private String city;
    private String district;
    private String address;

    @TableField("status")
    private String status;

    @TableField("patient_user_id")
    private String patientUserId;

    @TableField("pay_time")
    private String payTime;

    private String isPay;
    private String isDefault;
    private String supplier;
    private String type;
}