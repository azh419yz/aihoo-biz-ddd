package com.aihoo.api.doctor.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 医生端-发送群消息响应 VO。
 */
@Data
@Schema(description = "im群发普通消息")
public class ImSendGroupMsgRespVo {

    @Schema(description = "请求处理结果")
    @JsonProperty("ActionStatus")
    private String actionStatus;

    @Schema(description = "错误码")
    @JsonProperty("ErrorCode")
    private Integer errorCode;

    @Schema(description = "详细错误信息")
    @JsonProperty("ErrorInfo")
    private String errorInfo;

    @Schema(description = "消息时间戳")
    @JsonProperty("MsgTime")
    private Long msgTime;

    @Schema(description = "32位序列号")
    @JsonProperty("MsgSeq")
    private Long msgSeq;
}
