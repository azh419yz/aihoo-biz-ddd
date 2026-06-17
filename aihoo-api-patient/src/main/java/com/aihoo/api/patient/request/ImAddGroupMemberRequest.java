package com.aihoo.api.patient.request;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class ImAddGroupMemberRequest {
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
