package com.aihoo.domain.doctor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 欢迎语设置表
 */
@Data
@TableName("t_doctor_welcome_message")
public class DoctorWelcomeMessageSet implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 医生id
     */
    private String doctorUserId;

    /**
     * 是否开启自动发送 0-未开 1-开启
     */
    private Integer isAuto;

    /**
     * 欢迎语
     */
    private String welcomeMessage;

}
