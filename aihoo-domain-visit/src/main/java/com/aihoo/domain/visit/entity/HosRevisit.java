package com.aihoo.domain.visit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_hos_revisit")
public class HosRevisit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String patientUserId;
    private String hosSickId;
    private String doctorUserId;
    private String name;
    private String idCard;
    private String sex;
    private String age;
    private String mobile;
    private String hospitalName;
    private String departName;
    private String diseaseName;
    private String revisitStartTime;
    private String revisitEndTime;
    private String revisitDateStr;
    private String revisitTimeStr;
    private String content;
    private String totalPrice;
    private String payType;
    private String payTime;
    private String status;
    private String msg;
    private String orderNum;
    private String fiveStar;
    private String doctorAdvice;
    private String haveTime;
    private String startTime;
    private String endTime;
    private String isReadIm;
    private String isPay;
}