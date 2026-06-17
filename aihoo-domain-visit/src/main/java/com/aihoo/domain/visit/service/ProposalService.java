package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.entity.Proposal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 反馈意见表 服务接口（迁自 doctor-api 的 ProposalService，迁入 visit 域；表 t_proposal 与 visit 同库）。
 */
public interface ProposalService extends IService<Proposal> {

    boolean createProposal(String doctorUserId, String content, String type);

    Long countByDoctorUserId(String doctorUserId);
}
