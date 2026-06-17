package com.aihoo.api.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "地区VO")
public class AreaVo {
    @Schema(description = "名称")
    private String name;
    @Schema(description = "地区编码")
    private String areaCode;
    @Schema(description = "子级")
    private List<AreaVo> children;
}
