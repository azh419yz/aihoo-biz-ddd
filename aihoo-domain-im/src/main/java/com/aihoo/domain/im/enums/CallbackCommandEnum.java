package com.aihoo.domain.im.enums;

import java.util.Arrays;

public enum CallbackCommandEnum {
    NONE("", "", ""),
    CALL_BACK_AFTER_SEND_MSG("发送消息后回调接口", "C2C.CallbackAfterSendMsg", "imCallBackAfterSendMsgService"),
    GROUP_CALL_BACK_AFTER_MGS_READ_RECEIPT("群组已读回调接口", "Group.CallbackAfterReadReceipt", "imCallBackMsgReadService"),
    GROUP_CALL_BACK_AFTER_SEND_MSG("群组发送消息后回调", "Group.CallbackAfterSendMsg", "imGroupCallBackAfterSendMsgService"),
    GROUP_CALL_BACK_AFTER_CREATE_GROUP("创建群组后回调", "Group.CallbackAfterCreateGroup", "imCallbackAfterCreateGroupService"),
    GROUP_CALL_BACK_AFTER_NEW_MEMBER_JOIN("新成员入群后回调", "Group.CallbackAfterNewMemberJoin", "imCallbackAfterNewMemberJoinService");

    private String typeName;
    private String value;
    private String serviceName;

    CallbackCommandEnum(String typeName, String value, String serviceName) {
        this.typeName = typeName;
        this.serviceName = serviceName;
        this.value = value;
    }

    public static CallbackCommandEnum fromValue(String value) {
        return Arrays.stream(values())
                .filter(command -> command.getValue().equals(value))
                .findFirst()
                .orElse(NONE);
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
