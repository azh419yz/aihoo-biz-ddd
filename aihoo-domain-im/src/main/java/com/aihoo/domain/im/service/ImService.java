package com.aihoo.domain.im.service;

import com.aihoo.domain.im.dto.ImSendGroupMsgRequestDto;
import com.aihoo.domain.im.dto.ImSendMsgReqDto;
import com.aihoo.domain.im.dto.ImSendMsgRespDto;
import com.aihoo.domain.im.dto.ImWithdrawMsgRequestDto;
import com.aihoo.domain.im.enums.ImServiceApiEnum;

public interface ImService {
    ImSendMsgRespDto callTim(ImServiceApiEnum api, ImSendMsgReqDto imSendMsgRequest);

    String callTimV1(String api, String payload);

    
    boolean sendGroupMsg(ImSendGroupMsgRequestDto req);

    
    boolean withdrawMsg(ImWithdrawMsgRequestDto req);
}
