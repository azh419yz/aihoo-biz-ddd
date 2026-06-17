package com.aihoo.domain.drug.entity;

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
     * 创建人id
     */
    private String createUserId;

    /**
     * 药房名称
     */
    private String name;

    /**
     * 药房图片
     */
    private String image;

    /**
     * 标签
     */
    private String tags;

    /**
     * 发货描述
     */
    private String dispatchDesc;

    /**
     * 状态(是否启用 1:启用 0:停用)
     */
    private String status;
}
