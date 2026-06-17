package com.aihoo.domain.doctor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_doctor_set")
public class DoctorSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String doctorUserId;

    private String isImg;
    private String isVoice;
    private String isVideo;
    private String isRevisit;
    private String imgPrice;
    private String voicePrice;
    private String videoPrice;
    private Byte isMdt;
}