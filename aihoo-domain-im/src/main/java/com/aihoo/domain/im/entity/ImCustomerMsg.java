package com.aihoo.domain.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("t_im_customer_msg")
public class ImCustomerMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

    @TableField("create_time_str")
    private String createTimeStr;

    private String fromAccount;
    private String toAccount;
    private String adminId;
    private String patientId;
    private String msgSeq;
    private String msgRandom;
    private String msgTime;
    private String msgKey;
    private String sendMsgResult;
    private String errorInfo;

    @TableField(exist = false)
    private List<ImMsgCustomerContent> imMsgCustomerContents;
}