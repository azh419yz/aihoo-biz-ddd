package com.aihoo.api.doctor.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "手机验证码相关请求")
public class PhoneCodeRequest {

    @Schema(description = "手机号码", example = "13800001111")
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String mobile;

    @Schema(description = "短信验证码", example = "123456")
    @NotBlank(message = "短信验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "短信验证码必须为6位数字")
    private String code;
}
