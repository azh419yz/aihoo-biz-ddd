package com.aihoo.api.patient.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ImGetGroupMemberInfoRespVo extends ImRespVo {
    @JSONField(name = "GroupId")
    private String groupId;
    @JSONField(name = "MemberList")
    private List<MemberInfoItem> memberList;
    @JSONField(name = "MemberNum")
    private Integer memberNum;
    @JSONField(name = "RequestId")
    private String requestId;

    @Data
    public static class MemberInfoItem {
        @JSONField(name = "JoinTime")
        private String joinTime;
        @JSONField(name = "LastSendMsgTime")
        private String lastSendMsgTime;
        @JSONField(name = "Member_Account")
        private String memberAccount;
        @JSONField(name = "MsgFlag")
        private String msgFlag;
        @JSONField(name = "MsgSeq")
        private Long msgSeq;
        @JSONField(name = "MuteUntil")
        private Long muteUntil;
        @JSONField(name = "MutedUntil")
        private Long mutedUntil;
        @JSONField(name = "Role")
        private String role;
        @JSONField(name = "ShutUpUntil")
        private String shutUpUntil;
    }
}
