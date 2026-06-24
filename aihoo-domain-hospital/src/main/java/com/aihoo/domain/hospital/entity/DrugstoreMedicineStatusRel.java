package com.aihoo.domain.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("t_drugstore_medicine_status_rel")
public class DrugstoreMedicineStatusRel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

    @TableField("create_user_id")
    private String createUserId;

    @TableField("drugstore_id")
    private String drugstoreId;

    @TableField("medicine_status_code")
    private Integer medicineStatusCode;
}
