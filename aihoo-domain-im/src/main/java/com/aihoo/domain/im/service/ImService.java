package com.aihoo.domain.im.service;

import com.aihoo.domain.im.dto.ImSendMsgRequest;
import com.aihoo.domain.im.dto.ImSendMsgRespVo;
import com.aihoo.domain.im.enums.ImServiceApiEnum;

public interface ImService {
    ImSendMsgRespVo callTim(ImServiceApiEnum api, ImSendMsgRequest imSendMsgRequest);

    String callTimV1(String api, String payload);
}
