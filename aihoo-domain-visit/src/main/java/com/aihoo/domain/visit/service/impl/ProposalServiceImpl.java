package com.aihoo.domain.visit.service.impl;

import com.aihoo.domain.visit.entity.Proposal;
import com.aihoo.domain.visit.mapper.ProposalMapper;
import com.aihoo.domain.visit.service.ProposalService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ProposalServiceImpl extends ServiceImpl<ProposalMapper, Proposal> implements ProposalService {

    @Autowired
    private ProposalMapper proposalMapper;

    @Override
    public boolean createProposal(String doctorUserId, String content, String type) {
        Proposal proposal = new Proposal();
        proposal.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        proposal.setDoctorUserId(doctorUserId);
        proposal.setContent(content);
        proposal.setType(type);
        return proposalMapper.insert(proposal) > 0;
    }

    @Override
    public Long countByDoctorUserId(String doctorUserId) {
        return proposalMapper.selectCount(new LambdaQueryWrapper<Proposal>()
                .eq(Proposal::getDoctorUserId, doctorUserId));
    }
}
