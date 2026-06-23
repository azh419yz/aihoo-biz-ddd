package com.aihoo.domain.im.service;

import com.aihoo.domain.im.dto.ImSendGroupMsgRequestDto;
import com.aihoo.domain.im.dto.ImSendGroupMsgRespDto;
import com.aihoo.domain.im.entity.ImGroup;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ImGroupService extends IService<ImGroup> {
    ImSendGroupMsgRespDto sendMsg(ImSendGroupMsgRequestDto req);
}
