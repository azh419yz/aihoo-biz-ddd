package com.aihoo.domain.patient.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_patient_user_log")
public class PatientUserLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;

    private String updateTime;

    private String patientUserId;

    /** LOGIN登录 LOGOUT登出 CANCEL注销 BIND绑定手机号 */
    private String actionType;

    private String osName;

    private String ipAddress;

    private String remark;

    private String city;
}