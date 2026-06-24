package com.aihoo.domain.patient.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_hos_sick")
public class HosSick implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;

    private String updateTime;

    private String patientUserId;

    private String name;

    private String idCard;

    private String sex;

    private String age;

    private String mobile;

    private String isDefault;

    private String isDelete;

    private String address;

    private Date birthday;

    private String imUserId;

    private String imUserSig;
}