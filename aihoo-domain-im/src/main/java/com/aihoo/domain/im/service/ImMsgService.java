package com.aihoo.domain.im.service;

import com.aihoo.domain.im.dto.ImWithdrawMsgRequestDto;
import com.aihoo.domain.im.entity.ImMsg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * IM 消息 service。
 * <p>2026-06-18 完整迁移 patient-api/ImMsgService：
 * imMsgSave / imList / pushMessage / getPushMessageOne / withdrawMsg 全部实现。
 */
public interface ImMsgService extends IService<ImMsg> {

    /**
     * 回调：保存 IM 消息（区分普通消息 / 客服消息）。
     */
    void imMsgSave(Map<String, Object> map);

    /**
     * 撤回消息。
     */
    boolean withdrawMsg(ImWithdrawMsgRequestDto req);
}