package com.aihoo.wechat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class WeChatMobileDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "手机详情")
    @JsonProperty("phone_info")
    private PhoneInfo phoneInfo;

    @Schema(description = "错误码")
    @JsonProperty("errcode")
    private Integer errCode;

    @Schema(description = "错误信息")
    @JsonProperty("errmsg")
    private String errMsg;

    public boolean isSuccess() {
        return errCode == null || errCode == 0;
    }

    @Data
    public static class PhoneInfo {
        @Schema(description = "用户绑定的手机号")
        @JsonProperty("phoneNumber")
        private String phoneNumber;

        @Schema(description = "没有区号的手机号")
        @JsonProperty("purePhoneNumber")
        private String purePhoneNumber;

        @Schema(description = "区号")
        @JsonProperty("countryCode")
        private String countryCode;

        @Schema(description = "水印")
        @JsonProperty("watermark")
        private Watermark watermark;
    }

    @Data
    public static class Watermark {
        @JsonProperty("timestamp")
        private Long timestamp;

        @JsonProperty("appid")
        private String appid;
    }
}