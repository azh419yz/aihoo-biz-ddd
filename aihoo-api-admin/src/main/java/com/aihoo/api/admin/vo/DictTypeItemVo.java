package com.aihoo.api.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "字典项VO")
public class DictTypeItemVo {
    @Schema(description = "类型名称")
    private String typeName;
    @Schema(description = "编码")
    private String code;
    @Schema(description = "名称")
    private String name;
}
