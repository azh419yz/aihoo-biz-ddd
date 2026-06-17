package com.aihoo.domain.drug.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "药品查询请求参数")
public class SearchDrugRequestDto {

    @Schema(description = "药房ID", example = "1")
    @NotBlank(message = "药房ID不能为空")
    private String drugstoreId;

    @Schema(description = "药品名称", example = "砒霜")
    private String name;

    @Schema(description = "拼音首字母", example = "RS（人参）")
    private String initial;
}
