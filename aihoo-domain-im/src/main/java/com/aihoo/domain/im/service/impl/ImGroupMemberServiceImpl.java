package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.entity.ImGroupMember;
import com.aihoo.domain.im.mapper.ImGroupMemberMapper;
import com.aihoo.domain.im.service.ImGroupMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ImGroupMemberServiceImpl extends ServiceImpl<ImGroupMemberMapper, ImGroupMember> implements ImGroupMemberService {
}
