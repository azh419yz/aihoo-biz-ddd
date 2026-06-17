package com.aihoo.domain.im.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class ImAddGroupMemberRequestDto {
    @JSONField(name = "GroupId")
    private String groupId;
    @JSONField(name = "MemberList")
    private List<MemberItem> memberList;
    @JSONField(name = "Silence")
    private Integer silence;

    @Data
    public static class MemberItem {
        @JSONField(name = "Member_Account")
        private String memberAccount;
    }
}
