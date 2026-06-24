package com.aihoo.domain.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_hospital_department")
public class HospitalDepartment implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;

    private String updateTime;

    private String createUserId;

    private String hospitalId;

    private String departCode;

    private String departName;
}
