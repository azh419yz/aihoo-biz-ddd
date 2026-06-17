package com.aihoo.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 地区表（合并自旧代码 DArea 与 Area，对应 d_area 表）
 *
 * @author mcp
 * @since 2020-08-10
 */
@Data
@TableName("d_area")
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型 PROVINCE-省 CITY-市 DISTRICT-区
     */
    private String type;

    /**
     * 编码
     */
    private String areaCode;

    /**
     * 上级编码
     */
    private String parentAreaCode;
}