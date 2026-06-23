package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.dto.ImCallbackReqDto;
import com.aihoo.domain.im.entity.ImGroup;
import com.aihoo.domain.im.service.ImCallBackService;
import com.aihoo.domain.im.service.ImGroupService;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service("imCallbackAfterCreateGroupService")
@RequiredArgsConstructor
public class ImCallbackAfterCreateGroupService implements ImCallBackService {

    private final ImGroupService imGroupService;

    @Override
    public void callBack(ImCallbackReqDto request) {
        JSONObject createGroupRequest = JSONObject.parseObject(request.getCallBackBody());
        String groupId = createGroupRequest.getString("GroupId");
        if (StringUtils.isEmpty(groupId)) {
            log.info("异常:{},参数:{}", "没有获取到groupId", createGroupRequest);
            return;
        }

        ImGroup imGroup = new ImGroup();
        imGroup.setGroupOwnerAccount(createGroupRequest.getString("Owner_Account"));
        imGroup.setGroupType(createGroupRequest.getString("Type"));
        imGroup.setGroupName(createGroupRequest.getString("Name"));
        imGroup.setGroupId(createGroupRequest.getString("GroupId"));
        log.info("执行创建群后回调结果:{},参数:{}", imGroupService.save(imGroup), createGroupRequest);
    }
}
