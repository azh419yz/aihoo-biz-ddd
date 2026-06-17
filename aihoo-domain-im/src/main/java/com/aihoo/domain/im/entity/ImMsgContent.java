package com.aihoo.domain.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_im_msg_content")
@Schema(description = "IM消息内容表")
public class ImMsgContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

    @TableField("create_time_str")
    private String createTimeStr;

    @TableField("msg_type")
    private String msgType;

    @TableField("msg_type_name")
    private String msgTypeName;

    @TableField("msg_content")
    private String msgContent;

    @TableField("im_msg_id")
    private String imMsgId;
}
