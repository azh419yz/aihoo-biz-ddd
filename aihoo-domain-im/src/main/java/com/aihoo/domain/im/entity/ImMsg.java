package com.aihoo.domain.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_im_msg")
@Schema(description = "IM消息表")
public class ImMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

    @TableField("create_time_str")
    private String createTimeStr;

    @TableField("from_account")
    private String fromAccount;

    @TableField("to_account")
    private String toAccount;

    @TableField("msg_seq")
    private String msgSeq;

    @TableField("msg_random")
    private String msgRandom;

    @TableField("msg_time")
    private String msgTime;

    @TableField("msg_key")
    private String msgKey;

    @TableField("send_msg_result")
    private String sendMsgResult;

    @TableField("error_info")
    private String errorInfo;

    @TableField("order_num")
    private String orderNum;

    @TableField("order_type")
    private String orderType;

    @TableField("peer_read_status")
    private Integer peerReadStatus;

    @TableField("sick_peer_read_status")
    private Integer sickPeerReadStatus;

    @TableField("doctor_peer_read_status")
    private Integer doctorPeerReadStatus;

    @TableField("load_param")
    private Integer loadParam;

    @TableField("msg_type")
    private Integer msgType;

    @TableField("msg_status")
    private String msgStatus;
}
