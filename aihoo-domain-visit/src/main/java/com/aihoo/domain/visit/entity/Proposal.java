package com.aihoo.domain.visit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_proposal")
@Schema(description = "反馈意见表")
public class Proposal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private String createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private String updateTime;

    @Schema(description = "类型 PATIENT-患者 DOCKER-医生")
    @TableField("type")
    private String type;

    @Schema(description = "患者用户ID")
    @TableField("patient_user_id")
    private String patientUserId;

    @Schema(description = "医生ID")
    @TableField("doctor_user_id")
    private String doctorUserId;

    @Schema(description = "意见内容")
    @TableField("content")
    private String content;
}
