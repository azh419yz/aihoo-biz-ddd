package com.aihoo.push;

import cn.hutool.core.date.DateUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PushMessageServiceImpl implements PushMessageService {

    @Resource
    private PushMessageMapper pushMessageMapper;

    @Override
    public int insertOne(String title, String intro, String messageType, String otherId, String content, String isPush, String createUserId, String pesronalId) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setCreateUserId(createUserId);
        pushMessage.setType("PATIENT_PERSONAL");
        pushMessage.setPesronalId(pesronalId);
        pushMessage.setTitle(title);
        pushMessage.setIntro(intro);
        pushMessage.setMessageType(messageType);
        pushMessage.setOtherId(otherId);
        pushMessage.setContent(content);
        pushMessage.setIsPush(isPush);
        pushMessage.setNoticeType("SERVICE");
        pushMessage.setPushTime(DateUtil.now());
        pushMessage.setSetTime(DateUtil.now());
        return pushMessageMapper.insert(pushMessage);
    }

    @Override
    public int insertDoctor(String title, String doctorId, String intro, String messageType, String otherId, String content, String isPush) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setCreateUserId(doctorId);
        pushMessage.setType("DOCKER_PERSONAL");
        pushMessage.setPesronalId(doctorId);
        pushMessage.setTitle(title);
        pushMessage.setIntro(intro);
        pushMessage.setMessageType(messageType);
        pushMessage.setOtherId(otherId);
        pushMessage.setContent(content);
        pushMessage.setIsPush(isPush);
        pushMessage.setNoticeType("SERVICE");
        pushMessage.setPushTime(DateUtil.now());
        pushMessage.setSetTime(DateUtil.now());
        return pushMessageMapper.insert(pushMessage);
    }

    @Override
    public int insertAdmin(String title, String admin, String intro, String messageType, String otherId, String content, String isPush) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setCreateUserId(admin);
        pushMessage.setType("ADMIN_PERSONAL");
        pushMessage.setPesronalId(admin);
        pushMessage.setTitle(title);
        pushMessage.setIntro(intro);
        pushMessage.setMessageType(messageType);
        pushMessage.setOtherId(otherId);
        pushMessage.setContent(content);
        pushMessage.setIsPush(isPush);
        pushMessage.setNoticeType("SERVICE");
        pushMessage.setPushTime(DateUtil.now());
        pushMessage.setSetTime(DateUtil.now());
        return pushMessageMapper.insert(pushMessage);
    }
}