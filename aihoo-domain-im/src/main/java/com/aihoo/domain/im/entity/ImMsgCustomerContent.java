package com.aihoo.domain.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * IM 客服消息内容表。
 * <p>2026-06-18 迁自 patient-api/ImMsgCustomerContent。
 */
@Data
@TableName("t_im_msg_customer_content")
public class ImMsgCustomerContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

    @TableField("create_time_str")
    private String createTimeStr;

    private String msgType;
    private String msgTypeName;
    private String msgContent;

    @TableField("im_msg_id")
    private String imMsgId;
}