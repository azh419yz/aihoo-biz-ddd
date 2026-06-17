package com.aihoo.domain.im.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "查询分组下所有用户")
public class ImGetGroupMemberInfoRequestDto {
    @JSONField(name = "GroupId")
    private String groupId;
    @JSONField(name = "Offset")
    private Integer offset;
    @JSONField(name = "Limit")
    private Integer limit;
    @JSONField(name = "MemberInfoFilter")
    private List<String> memberInfoFilter;
    @JSONField(name = "MemberRoleFilter")
    private Integer memberRoleFilter;
}
