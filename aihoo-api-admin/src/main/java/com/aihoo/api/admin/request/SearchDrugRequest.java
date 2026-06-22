package com.aihoo.api.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "药品查询请求参数")
public class SearchDrugRequest {

    @Schema(description = "药房ID", example = "1")
    private String drugstoreId;

    @Schema(description = "药品名称", example = "砒霜")
    private String name;

    @Schema(description = "拼音首字母", example = "RS（人参）")
    private String initial;
}
