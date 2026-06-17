package com.aihoo.api.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "手机验证码登录请求")
public class PhoneLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号格式错误")
    private String phone;

    @Schema(description = "短信验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;
}
