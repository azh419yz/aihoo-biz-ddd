package com.aihoo.api.patient.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "im群新增用户")
public class ImAddGroupMemberRespVo extends ImRespVo {
    @JSONField(name = "MemberList")
    @Schema(description = "用户列表")
    private List<MemberResult> memberList;

    @Data
    public static class MemberResult {
        @JSONField(name = "Member_Account")
        private String memberAccount;
        @JSONField(name = "JoinStatus")
        private Integer joinStatus;
    }
}
