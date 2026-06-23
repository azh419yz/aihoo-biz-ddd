package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.dto.ImCallbackReqDto;
import com.aihoo.domain.im.entity.ImMsg;
import com.aihoo.domain.im.service.ImCallBackService;
import com.aihoo.domain.im.service.ImMsgService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("imCallBackMsgReadService")
@RequiredArgsConstructor
public class ImCallBackMsgReadServiceImpl implements ImCallBackService {

    private final ImMsgService imMsgService;

    @Override
    public void callBack(ImCallbackReqDto request) {
        log.info("回调接口执行:{}", JSONObject.toJSONString(request));

        JSONObject jsonObject = JSONObject.parseObject(request.getCallBackBody());
        JSONArray msgInfos = jsonObject.getJSONArray("GroupMsgReceiptList");
        for (int i = 0; i < msgInfos.size(); i++) {
            JSONObject msgInfo = msgInfos.getJSONObject(i);
            ImMsg imMsg = imMsgService.getOne(new LambdaQueryWrapper<ImMsg>()
                    .eq(ImMsg::getMsgSeq, msgInfo.getString("MsgSeq")));
            if (imMsg == null) {
                log.info("没有查询到该条消息，回调参数为:{}", jsonObject);
                return;
            }
            imMsg.setPeerReadStatus(1);
            imMsgService.updateById(imMsg);
        }
    }
}
