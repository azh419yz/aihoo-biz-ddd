package com.aihoo.api.patient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "im信息发送请求参数")
public class ImSendMsgRequest {
    private String toAccount;
    private String fromAccount;
    private String msgSeq;
    private String msgRandom;
    private String msgType;
    private String msgContent;
    private String cloudCustomData;
    private String supportMessageExtension;
    private String visitNo;
}
