package com.aihoo.domain.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_im_group")
public class ImGroup {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String groupId;
    private String groupName;
    private String groupOwnerAccount;
    private String groupType;
    private String createTime;
    private String updateTime;
}
