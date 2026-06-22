package com.aihoo.api.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "药品新增修改请求参数")
public class SaveUpdateDrugRequest {

    public interface Save {
    }

    public interface Update {
    }

    @Schema(description = "药品ID", example = "1")
    @NotBlank(message = "药品ID不能为空", groups = Update.class)
    private String id;

    @Schema(description = "药房ID", example = "1")
    @NotBlank(message = "药房ID不能为空", groups = Save.class)
    private String drugstoreId;

    @Schema(description = "药品名称", example = "阿莫西林胶囊")
    @NotBlank(message = "药品名称不能为空", groups = Save.class)
    private String name;

    @Schema(description = "药品价格", example = "0.01")
    @NotBlank(message = "药品价格不能为空", groups = Save.class)
    private String price;

    @Schema(description = "煎药方式", example = "后入")
    private String method;

    @Schema(description = "状态(1:启用 0:停用)", example = "1")
    private String status;
}
