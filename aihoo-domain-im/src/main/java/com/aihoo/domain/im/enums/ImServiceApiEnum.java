package com.aihoo.domain.im.enums;

public enum ImServiceApiEnum {
    SEND_MSG("v4/openim/sendmsg", "{"
            + "    \"SyncOtherMachine\": 1, "
            + "    \"To_Account\": \"${toAccount}\","
            + "    \"From_Account\": \"${fromAccount}\","
            + "    \"MsgSeq\": ${msgSeq},"
            + "    \"MsgRandom\": ${msgRandom},"
            + "    \"MsgBody\": ["
            + "        {"
            + "            \"MsgType\": \"${msgType}\","
            + "            \"MsgContent\": "
            + "              ${msgContent}"
            + "        }"
            + "    ],"
            + "    \"CloudCustomData\": \"${cloudCustomData}\","
            + "    \"SupportMessageExtension\": ${supportMessageExtension}"
            + "}"),
    MODIFY_C2C_MSG("v4/openim/modify_c2c_msg", "{"
            + "    \"From_Account\": \"${fromAccount}\","
            + "    \"To_Account\": \"${toAccount}\","
            + "    \"MsgKey\": \"${msgKey}\","
            + "    \"MsgBody\": ["
            + "        {"
            + "            \"MsgType\": \"${msgType}\","
            + "            \"MsgContent\": {"
            + "                \"Text\": \"${msgContent}\""
            + "            }"
            + "        }"
            + "    ]"
            + "}");

    private String apiName;
    private String apiTemplate;

    ImServiceApiEnum(String apiName, String apiTemplate) {
        this.apiName = apiName;
        this.apiTemplate = apiTemplate;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiTemplate() {
        return apiTemplate;
    }

    public void setApiTemplate(String apiTemplate) {
        this.apiTemplate = apiTemplate;
    }
}
