package com.aihoo.domain.im.service;

import com.aihoo.domain.im.dto.ImWithdrawMsgRequestDto;
import com.aihoo.domain.im.entity.ImMsg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface ImMsgService extends IService<ImMsg> {

    
    void imMsgSave(Map<String, Object> map);

    
    boolean withdrawMsg(ImWithdrawMsgRequestDto req);
}