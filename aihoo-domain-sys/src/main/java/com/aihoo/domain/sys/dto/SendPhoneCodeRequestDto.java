package com.aihoo.domain.sys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "发送手机验证码请求对象")
public class SendPhoneCodeRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13800138000")
    @NotBlank(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号格式错误")
    private String phone;
}
