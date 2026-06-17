package com.aihoo.api.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色VO")
public class SysRoleVo {
    @Schema(description = "角色ID")
    private String id;
    @Schema(description = "角色名称")
    private String roleName;
    @Schema(description = "备注")
    private String comments;
}
