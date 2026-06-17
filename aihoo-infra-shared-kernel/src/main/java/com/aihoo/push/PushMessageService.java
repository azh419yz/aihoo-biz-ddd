package com.aihoo.push;

public interface PushMessageService {
    int insertOne(String title, String intro, String messageType, String otherId, String content, String isPush, String createUserId, String pesronalId);

    int insertDoctor(String title, String doctorId, String intro, String messageType, String otherId, String content, String isPush);

    int insertAdmin(String title, String admin, String intro, String messageType, String otherId, String content, String isPush);
}