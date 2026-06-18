package com.aihoo.domain.im.service;

import com.aihoo.domain.im.dto.ImSendGroupMsgRequestDto;
import com.aihoo.domain.im.dto.ImSendMsgRequest;
import com.aihoo.domain.im.dto.ImSendMsgRespVo;
import com.aihoo.domain.im.dto.ImWithdrawMsgRequestDto;
import com.aihoo.domain.im.enums.ImServiceApiEnum;

public interface ImService {
    ImSendMsgRespVo callTim(ImServiceApiEnum api, ImSendMsgRequest imSendMsgRequest);

    String callTimV1(String api, String payload);

    /**
     * 发送群组消息（迁自 doctor-api IMService.sendGroupMsg）。
     * 与 ImGroupService.sendMsg 的区别：本方法还会保存 ImMsg / ImMsgContent 到数据库。
     */
    boolean sendGroupMsg(ImSendGroupMsgRequestDto req);

    /**
     * 撤回消息（迁自 doctor-api IMService.withdrawMsg，逻辑转发 ImMsgService.withdrawMsg）。
     */
    boolean withdrawMsg(ImWithdrawMsgRequestDto req);
}
