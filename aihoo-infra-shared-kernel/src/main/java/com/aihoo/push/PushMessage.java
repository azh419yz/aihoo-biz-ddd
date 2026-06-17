package com.aihoo.push;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_push_message")
public class PushMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createUserId;
    private String type;
    private String pesronalId;
    private String title;
    private String intro;
    private String messageType;
    private String otherId;
    private String img;
    private String content;
    private String isPush;
    private String noticeType;
    private String pushTime;
    private String setTime;
}