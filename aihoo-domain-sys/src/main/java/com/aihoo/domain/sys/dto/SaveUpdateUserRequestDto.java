package com.aihoo.domain.sys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户请求参数")
public class SaveUpdateUserRequestDto {

    public interface Save {}
    public interface Update {}

    @Schema(description = "用户ID", example = "1")
    @NotBlank(message = "用户ID不能为空", groups = Update.class)
    private String id;

    @Schema(description = "姓名", example = "老王")
    @NotBlank(message = "姓名不能为空", groups = Save.class)
    private String trueName;

    @Schema(description = "手机号码", example = "13800001111")
    @NotBlank(message = "手机号码不能为空", groups = Save.class)
    private String phone;

    @Schema(description = "药房ID", example = "['1','2']")
    private List<String> drugstoreIdList;

    @Schema(description = "管理权限 1:是 0:否", example = "1")
    private Integer permission;
}
