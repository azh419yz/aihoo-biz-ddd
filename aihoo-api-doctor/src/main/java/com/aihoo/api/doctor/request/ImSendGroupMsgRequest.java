package com.aihoo.api.doctor.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 医生端-im 群消息发送请求。
 */
@Data
@Schema(description = "im群消息发送")
public class ImSendGroupMsgRequest {

    @JsonProperty("GroupId")
    @Schema(name = "groupId", description = "群ID")
    private String groupId;

    @JsonProperty("From_Account")
    @Schema(name = "fromAccount", description = "发送人")
    private String fromAccount;

    @Schema(name = "toAccount", description = "接收人")
    private String toAccount;

    @JsonProperty("Random")
    @Schema(name = "random", description = "随机数")
    private Long random;

    @JsonProperty("MsgBody")
    @Schema(name = "msgBody", description = "消息内容")
    private List<MessageBody> msgBody;

    @JsonProperty("CloudCustomData")
    @Schema(name = "cloudCustomData", description = "传递参数")
    private String cloudCustomData;

    @Schema(name = "visitNo", description = "订单号")
    private String visitNo;

    @Schema(name = "loadParam")
    private Integer loadParam;

    @Schema(name = "msgType")
    private Integer msgType;

    @Data
    public static class MessageBody {
        @JsonProperty("MsgType")
        private String msgType;
        @JsonProperty("MsgContent")
        private MsgParam msgContent;
    }

    @Data
    public static class MsgParam {
        @JsonProperty("Text")
        private String text;
        @JsonProperty("Index")
        private Integer index;
        @JsonProperty("Data")
        private String data;
    }
}
