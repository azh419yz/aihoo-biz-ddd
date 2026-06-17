package com.aihoo.domain.im.service;

import com.aihoo.domain.im.dto.ImSendGroupMsgRequestDto;
import com.aihoo.domain.im.dto.ImSendGroupMsgRespVo;
import com.aihoo.domain.im.entity.ImGroup;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ImGroupService extends IService<ImGroup> {
    ImSendGroupMsgRespVo sendMsg(ImSendGroupMsgRequestDto req);
}
