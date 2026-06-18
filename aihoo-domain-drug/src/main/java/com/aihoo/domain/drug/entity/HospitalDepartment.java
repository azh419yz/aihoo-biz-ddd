package com.aihoo.domain.drug.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname HospitalDepartment
 * @Description hf
 * @Date 2020/9/17 11:13
 * @Created by ad
 */
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
