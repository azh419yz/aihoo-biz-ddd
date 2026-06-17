package com.aihoo.api.patient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "手机号码请求")
public class PhoneRequest {

    @Schema(name = "mobile", description = "手机号码", example = "13800001111")
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String mobile;
}
