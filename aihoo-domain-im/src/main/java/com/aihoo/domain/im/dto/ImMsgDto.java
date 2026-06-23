package com.aihoo.domain.im.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "IM消息视图")
public class ImMsgDto {
    private String createTimeStr;
    private String fromAccount;
    private String toAccount;
    private String msgSeq;
    private String msgRandom;
    private String msgTime;
    private String msgKey;
    private String sendMsgResult;
    private String errorInfo;
    private String orderNum;
    private String orderType;
    private Integer msgType;
    private List<ImMsgContentDto> msgContents;
}
