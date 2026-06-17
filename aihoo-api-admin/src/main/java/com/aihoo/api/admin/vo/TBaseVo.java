package com.aihoo.api.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "常用语VO")
public class TBaseVo {
    @Schema(description = "ID")
    private String id;
    @Schema(description = "内容")
    private String content;
    @Schema(description = "排序")
    private String index;
}
