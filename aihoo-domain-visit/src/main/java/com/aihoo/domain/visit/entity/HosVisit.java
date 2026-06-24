package com.aihoo.domain.visit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_hos_visit")
public class HosVisit implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;

    private String updateTime;

    private String patientUserId;

    private String hosSickId;

    private String name;

    private String idCard;

    private String sex;

    private String age;

    private String mobile;

    private String content;

    private String type;

    private String totalPrice;

    private String payType;

    private String payTime;

    private String status;

    private String msg;

    private String orderNum;

    private String fiveStar;

    private String healthInfo;

    private String baseInfo;

    private String doctorUserId;

    private String infoSubmitTime;

    private String haveTime;

    private String doctorAdvice;

    private String firstVisit;

    private String startTime;

    private String endTime;

    private String isReadIm;

    private String isPay;

    @TableField(exist = false)
    private String imGroupId;
}