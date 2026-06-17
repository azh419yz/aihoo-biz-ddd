package com.aihoo.domain.im.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "im群消息发送")
public class ImSendGroupMsgRequestDto {
    @JSONField(name = "GroupId")
    private String groupId;
    @JSONField(name = "From_Account")
    private String fromAccount;
    private String toAccount;
    @JSONField(name = "Random")
    private Long random;
    @JSONField(name = "MsgBody")
    private List<MessageBody> msgBody;
    @JSONField(name = "CloudCustomData")
    private String cloudCustomData;
    private String visitNo;
    private String suffixMsg;
    private Integer msgType;

    @Data
    public static class MessageBody {
        @JSONField(name = "MsgType")
        private String msgType;
        @JSONField(name = "MsgContent")
        private MsgParam msgContent;
    }

    @Data
    public static class MsgParam {
        @JSONField(name = "Text")
        private String text;
        @JSONField(name = "Index")
        private Integer index;
        @JSONField(name = "Data")
        private String data;
    }
}
