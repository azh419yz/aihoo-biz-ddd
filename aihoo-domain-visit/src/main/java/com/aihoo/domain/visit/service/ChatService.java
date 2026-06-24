package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.dto.VisitChatDto;

public interface ChatService {

    VisitChatDto startVisitChatV2(String id);

    VisitChatDto stopVisitChatV2(String id);

}
