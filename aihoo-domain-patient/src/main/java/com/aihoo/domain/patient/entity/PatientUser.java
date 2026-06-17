package com.aihoo.domain.patient.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_patient_user")
public class PatientUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String mobile;
    private String wechatOpenId;
    private String alipayOpenId;
    private String nickName;
    private String headImg;
    private String name;
    private String idCard;
    private String sex;
    private String birthDay;
    private String status;
    private String isAuth;
    private String authTime;
    private String token;
    private String unionId;
    private String sessionKey;
    private String appleId;
    private String imUserId;
    private String imUserSig;
    private String isCancel;
}
