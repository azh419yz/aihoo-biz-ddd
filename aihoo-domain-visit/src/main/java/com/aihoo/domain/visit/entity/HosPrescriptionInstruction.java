package com.aihoo.domain.visit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_hos_prescription_instruction")
public class HosPrescriptionInstruction implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;

    private String updateTime;

    private String hosPrescriptionId;

    @TableField("`usage`")
    private String usage;

    private String doseNumber;

    private String dose;

    private String times;

    private String decoctionMethod;

    private String decoctionSize;

    private String advice;

    private String remark;
}