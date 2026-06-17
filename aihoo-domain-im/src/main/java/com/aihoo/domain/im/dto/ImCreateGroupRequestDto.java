package com.aihoo.domain.im.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "创建群")
public class ImCreateGroupRequestDto {
    @JSONField(name = "Owner_Account")
    private String ownerAccount;
    @JSONField(name = "Type")
    private String type;
    @JSONField(name = "Name")
    private String name;
    @JSONField(name = "GroupId")
    private String groupId;
    @JSONField(name = "MaxMemberNum")
    private Integer maxMemberNum;
    @JSONField(name = "MemberList")
    private List<MemberItem> memberList;

    @Data
    public static class MemberItem {
        @JSONField(name = "Member_Account")
        private String memberAccount;
    }
}
