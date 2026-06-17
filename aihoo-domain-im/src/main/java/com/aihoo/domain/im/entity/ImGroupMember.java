package com.aihoo.domain.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_im_group_member")
public class ImGroupMember {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long memberId;
    private Integer memberType;
    private String memberIdentity;
    private String imGroupId;
    private String createTime;
    private String updateTime;
}
