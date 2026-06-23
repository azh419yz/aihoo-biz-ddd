package com.aihoo.domain.im.dto;

import lombok.Data;

@Data
public class ImCallbackReqDto {
    private String SdkAppid;
    private String CallbackCommand;
    private String contenttype;
    private String ClientIP;
    private String OptPlatform;
    private String callBackBody;
}
