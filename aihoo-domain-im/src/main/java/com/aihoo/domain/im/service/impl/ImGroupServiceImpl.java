package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.dto.ImSendGroupMsgRequestDto;
import com.aihoo.domain.im.dto.ImSendGroupMsgRespVo;
import com.aihoo.domain.im.entity.ImGroup;
import com.aihoo.domain.im.mapper.ImGroupMapper;
import com.aihoo.domain.im.service.ImGroupService;
import com.aihoo.domain.im.util.TencentImGroupUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImGroupServiceImpl extends ServiceImpl<ImGroupMapper, ImGroup> implements ImGroupService {

    private final TencentImGroupUtil tencentImGroupUtil;

    @Override
    public ImSendGroupMsgRespVo sendMsg(ImSendGroupMsgRequestDto req) {
        return tencentImGroupUtil.sendGroupMessage(req);
    }
}
