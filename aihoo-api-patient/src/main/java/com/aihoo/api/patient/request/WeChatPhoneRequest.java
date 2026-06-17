package com.aihoo.api.patient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "绑定微信手机号码请求")
public class WeChatPhoneRequest {

    @Schema(name = "code", description = "动态令牌 code", example = "bc54ca45e5axxxx8cec4538ee8")
    @NotBlank(message = "code 不能为空")
    private String code;
}
