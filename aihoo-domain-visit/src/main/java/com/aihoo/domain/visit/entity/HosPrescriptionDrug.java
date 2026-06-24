package com.aihoo.domain.visit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_hos_prescription_drug")
public class HosPrescriptionDrug implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

    @TableField("hos_prescription_id")
    private String hosPrescriptionId;

    @TableField("drug_id")
    private String drugId;

    @TableField("name")
    private String name;

    @TableField("size")
    private String size;

    @TableField("drug_dos_code")
    private String drugDosCode;

    @TableField("drug_dos_name")
    private String drugDosName;

    @TableField("unit_measure")
    private String unitMeasure;

    @TableField("pack_unit_code")
    private String packUnitCode;

    @TableField("pack_unit_name")
    private String packUnitName;

    @TableField("price")
    private String price;

    @TableField("freq_med_code")
    private String freqMedCode;

    @TableField("freq_med_name")
    private String freqMedName;

    @TableField("route_admi_code")
    private String routeAdmiCode;

    @TableField("route_admi_name")
    private String routeAdmiName;

    @TableField("is_antibiotics")
    private String isAntibiotics;

    @TableField("is_injection")
    private String isInjection;

    @TableField("is_anesthesia")
    private String isAnesthesia;

    @TableField("is_monitor")
    private String isMonitor;

    @TableField("content")
    private String content;

    @TableField("number")
    private String number;

    @TableField("use_day")
    private String useDay;

    @TableField("dosage")
    private String dosage;

    @TableField("erp")
    private String erp;

    @TableField("erp_id")
    private String erpId;

    @TableField("method")
    private String method;
}