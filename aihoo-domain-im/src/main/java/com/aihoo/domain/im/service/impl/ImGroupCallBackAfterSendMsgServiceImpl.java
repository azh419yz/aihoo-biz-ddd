package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.dto.ImCallbackReqDto;
import com.aihoo.domain.im.service.ImCallBackService;
import com.aihoo.domain.im.service.ImMsgService;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service("imGroupCallBackAfterSendMsgService")
@RequiredArgsConstructor
public class ImGroupCallBackAfterSendMsgServiceImpl implements ImCallBackService {

    private final ImMsgService imMsgService;

    @Override
    public void callBack(ImCallbackReqDto request) {
        JSONObject body = JSONObject.parseObject(request.getCallBackBody());
        JSONObject customData = body.getJSONObject("CloudCustomData");
        body.put("visitNo", MapUtils.getString(customData, "visitNo"));
        if (StringUtils.isNotEmpty(MapUtils.getString(customData, "toAccount"))) {
            body.put("To_Account", MapUtils.getString(customData, "toAccount"));
        }
        body.put("MsgRandom", MapUtils.getLong(body, "Random"));
        imMsgService.imMsgSave(body);
        log.info("发送消息后回调接口成功!");
    }
}
