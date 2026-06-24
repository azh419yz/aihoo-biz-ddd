package com.aihoo.domain.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;

    private String updateTime;

    
    private String level;

    
    private String iconImg;

    
    private String code;

    
    private String name;

    
    private String parentCode;

    
    private String isShow;

    @TableField("`index`")
    private String index;

    
    private String status;
}
