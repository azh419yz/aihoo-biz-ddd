package com.aihoo.domain.doctor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 医生操作表（迁自 doctor-api: DoctorUserLog）。
 */
@Data
@TableName("t_doctor_user_log")
public class DoctorUserLog implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;

    private String updateTime;

    private String doctorUserId;

    private String actionType;

    private String osName;

    private String ipAddress;

    private String remark;

    private String city;
}
