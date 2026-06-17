package com.aihoo.api.patient.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 地区 Vo（合并自旧 AreaVo/DAreaVo，结构相同）。
 *
 * @author mcp
 * @since 2020-08-10
 */
@Data
public class AreaVo {

    @Schema(name = "name", description = "名称", example = "北京市")
    private String name;

    @Schema(name = "areaCode", description = "地区编码", example = "1001")
    private String areaCode;

    @Schema(name = "children", description = "子级")
    private List<AreaVo> children;
}