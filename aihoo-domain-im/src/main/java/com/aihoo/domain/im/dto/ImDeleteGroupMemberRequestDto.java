package com.aihoo.domain.im.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除群组用户")
public class ImDeleteGroupMemberRequestDto {
    @JSONField(name = "GroupId")
    private String groupId;
    @JSONField(name = "Silence")
    private Integer silence;
    @JSONField(name = "MemberToDel_Account")
    private String[] memberList;
}
