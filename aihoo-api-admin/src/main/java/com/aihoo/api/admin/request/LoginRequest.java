package com.aihoo.api.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "账号密码登录表单参数")
public class LoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名或账号")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
