package com.aihoo.api.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "系统用户VO")
public class SysUserVo {
    @Schema(description = "用户ID")
    private String id;
    @Schema(description = "账号")
    private String userName;
    @Schema(description = "昵称")
    private String nickName;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "性别")
    private String sex;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "真实姓名")
    private String trueName;
    @Schema(description = "身份证号")
    private String idCard;
    @Schema(description = "状态")
    private String status;
    @Schema(description = "注册时间")
    private String createdDate;
    @Schema(description = "修改时间")
    private String updatedDate;
    @Schema(description = "管理权限 1:是 0:否")
    private Integer permission;

    @Schema(description = "药房ID集合")
    private List<String> drugstoreIdList;
}
