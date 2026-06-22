package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.dto.VisitChatDto;

/**
 * 医生端-问诊复诊会话服务（迁自 doctor-api 的 ChatService，只保留 ChatV2Controller 实际用到的 4 个 V2 方法）。
 */
public interface ChatService {

    VisitChatDto startVisitChatV2(String id);

    VisitChatDto stopVisitChatV2(String id);

}
