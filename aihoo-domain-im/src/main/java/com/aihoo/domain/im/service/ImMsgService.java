package com.aihoo.domain.im.service;

import com.aihoo.domain.im.dto.ImWithdrawMsgRequestDto;
import com.aihoo.domain.im.entity.ImMsg;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ImMsgService extends IService<ImMsg> {
    void imMsgSave(com.alibaba.fastjson2.JSONObject jsonObject);

    boolean withdrawMsg(ImWithdrawMsgRequestDto req);
}
