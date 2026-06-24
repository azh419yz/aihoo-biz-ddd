package com.aihoo.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("d_area")
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    
    private String name;

    
    private String type;

    
    private String areaCode;

    
    private String parentAreaCode;
}