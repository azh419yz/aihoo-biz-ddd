package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.entity.Proposal;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProposalService extends IService<Proposal> {

    boolean createProposal(String doctorUserId, String content, String type);

    Long countByDoctorUserId(String doctorUserId);
}
