package com.aihoo.domain.hospital.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("t_drugstore")
public class Drugstore implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    
    private String createTime;

    
    private String updateTime;

    
    private String createUserId;

    
    private String name;

    
    private String image;

    
    private String tags;

    
    private String dispatchDesc;

    
    private String status;
}
