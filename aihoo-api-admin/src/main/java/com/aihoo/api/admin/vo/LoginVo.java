package com.aihoo.api.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "登录成功返回对象")
public class LoginVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private String id;
    @Schema(description = "账号/用户名")
    private String name;
    @Schema(description = "用户昵称")
    private String nickName;
    @Schema(description = "用户头像地址")
    private String avatar;
    @Schema(description = "性别")
    private String sex;
    @Schema(description = "IM 用户通信签名")
    private String userSig;
    @Schema(description = "关联的主治医生ID(如有)")
    private String doctorId;
    @Schema(description = "主治医生 IM 签名(如有)")
    private String doctorUserSig;
    @Schema(description = "密码重置提示信息，如90天未改密码")
    private String hint;
    @Schema(description = "认证Token")
    private String token;
}
