package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.dto.ImCallbackReqDto;
import com.aihoo.domain.im.service.ImCallBackService;
import com.aihoo.domain.im.service.ImMsgService;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("imCallBackAfterSendMsgService")
@RequiredArgsConstructor
public class ImCallBackAfterSendMsgServiceImpl implements ImCallBackService {

    private final ImMsgService imMsgService;

    @Override
    public void callBack(ImCallbackReqDto request) {
        log.info("请求保存消息接口:{}", JSONObject.toJSONString(request));
        JSONObject jsonObject = JSONObject.parseObject(request.getCallBackBody());
        imMsgService.imMsgSave(jsonObject);
        log.info("发送消息后回调接口成功!");
    }
}
