package com.aihoo.domain.hospital.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "药品新增/修改请求（admin 阶段）")
public class SaveUpdateDrugRequestDto {

    public interface Save {
    }

    public interface Update {
    }

    @Schema(description = "药品ID")
    @NotBlank(message = "药品ID不能为空", groups = Update.class)
    private String id;

    @Schema(description = "药房ID")
    @NotBlank(message = "药房ID不能为空", groups = Save.class)
    private String drugstoreId;

    @Schema(description = "药品名称")
    @NotBlank(message = "药品名称不能为空", groups = Save.class)
    private String name;

    @Schema(description = "药品价格")
    @NotBlank(message = "药品价格不能为空", groups = Save.class)
    private String price;

    @Schema(description = "煎药方式")
    private String method;

    @Schema(description = "状态(1:启用 0:停用)")
    private String status;
}
