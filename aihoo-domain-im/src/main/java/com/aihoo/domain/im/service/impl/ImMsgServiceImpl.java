package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.dto.ImWithdrawMsgRequestDto;
import com.aihoo.domain.im.entity.ImMsg;
import com.aihoo.domain.im.mapper.ImMsgMapper;
import com.aihoo.domain.im.service.ImMsgService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ImMsgServiceImpl extends ServiceImpl<ImMsgMapper, ImMsg> implements ImMsgService {

    @Override
    public void imMsgSave(com.alibaba.fastjson2.JSONObject jsonObject) {
        // 老实现需在 ImMsg / ImMsgContent / Doctor / HosVisit / HosPrescription /
        // MdtOrder / HosPreDrugOrder / PushMessage 之间写入数据。当前未迁完整，
        // 保留日志以便 IM 回调链路不报错。后续若需要消息持久化再回填。
        log.info("imMsgSave 调用,参数:{}", jsonObject);
    }

    @Override
    public boolean withdrawMsg(ImWithdrawMsgRequestDto req) {
        String visitNo = req.getMsgReq().split("_")[1];
        ImMsg msg = getOne(new LambdaQueryWrapper<ImMsg>()
                .eq(ImMsg::getMsgSeq, req.getMsgReq())
                .eq(ImMsg::getOrderNum, visitNo)
                .last("limit 1"));
        if (msg != null) {
            msg.setMsgStatus("WITHDRAW");
            return updateById(msg);
        }
        return false;
    }
}