package com.aihoo.wechat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class WeChatSessionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户唯一标识")
    private String openid;

    @Schema(description = "会话密钥")
    @JsonProperty("session_key")
    private String sessionKey;

    @Schema(description = "用户在开放平台的唯一标识符")
    private String unionid;

    @Schema(description = "错误码")
    @JsonProperty("errcode")
    private Integer errCode;

    @Schema(description = "错误信息")
    @JsonProperty("errmsg")
    private String errMsg;

    public boolean isSuccess() {
        return errCode == null || errCode == 0;
    }
}