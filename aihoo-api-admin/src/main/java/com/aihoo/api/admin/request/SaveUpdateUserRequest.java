package com.aihoo.api.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户请求")
public class SaveUpdateUserRequest {
    public interface Save {}
    public interface Update {}

    @Schema(description = "用户ID")
    @NotBlank(message = "用户ID不能为空", groups = Update.class)
    private String id;

    @Schema(description = "姓名")
    @NotBlank(message = "姓名不能为空", groups = Save.class)
    private String trueName;

    @Schema(description = "手机号码")
    @NotBlank(message = "手机号码不能为空", groups = Save.class)
    private String phone;

    @Schema(description = "药房ID")
    private List<String> drugstoreIdList;

    @Schema(description = "管理权限 1:是 0:否")
    private Integer permission;
}
