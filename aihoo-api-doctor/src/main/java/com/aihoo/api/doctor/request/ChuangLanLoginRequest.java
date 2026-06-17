package com.aihoo.api.doctor.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创蓝登录请求req")
public class ChuangLanLoginRequest {
    @Schema(name = "token", description = "SDK请求token", example = "")
    private String token;

    @Schema(name = "loginType", description = "1:置换手机号 2:本机登录", example = "1")
    private Integer loginType;

    @Schema(name = "source", description = "IOS or Android", example = "IOS")
    private String source;

    @Schema(name = "mobile", description = "待校验的手机号码 只需要在本机登录时传递", example = "")
    private String mobile;
}
