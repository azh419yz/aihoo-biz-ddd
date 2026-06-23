package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.dto.ImCallbackReqDto;
import com.aihoo.domain.im.entity.ImGroupMember;
import com.aihoo.domain.im.service.ImCallBackService;
import com.aihoo.domain.im.service.ImGroupMemberService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("imCallbackAfterNewMemberJoinService")
@RequiredArgsConstructor
public class ImCallbackAfterNewMemberJoinService implements ImCallBackService {

    private final ImGroupMemberService imGroupMemberService;

    @Override
    public void callBack(ImCallbackReqDto request) {
        JSONObject join = JSONObject.parseObject(request.getCallBackBody());
        String groupId = join.getString("GroupId");
        if (StringUtils.isEmpty(groupId)) {
            log.info("异常:{},参数:{}", "没有获取到groupId", join);
            return;
        }
        JSONArray memberList = join.getJSONArray("NewMemberList");
        if (CollectionUtils.isEmpty(memberList)) {
            log.info("异常:{},参数:{}", "没有获取到会员列表", join);
            return;
        }
        List<ImGroupMember> members = Lists.newArrayList();
        for (int i = 0; i < memberList.size(); i++) {
            JSONObject getMember = memberList.getJSONObject(i);
            ImGroupMember member = new ImGroupMember();
            String account = getMember.getString("Member_Account");
            if (StringUtils.isEmpty(account)) {
                continue;
            }
            member.setMemberType(StringUtils.contains(account, "PATIENT") ? 1 : 2);
            member.setMemberId(Long.valueOf(account.split("_")[1]));
            member.setMemberIdentity("Member");
            member.setImGroupId(groupId);
        }
        log.info("保存用户入群信息结果:{},请求参数:{}", imGroupMemberService.saveBatch(members), join);
    }
}
