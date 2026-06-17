package com.aihoo.domain.sys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 地区 Dto（合并自旧 AreaVo/DAreaVo，结构相同）。
 * 由 service 返回，controller 负责转换为 api 层 vo。
 *
 * @author mcp
 * @since 2020-08-10
 */
@Data
public class AreaDto {

    @Schema(name = "name", description = "名称", example = "北京市")
    private String name;

    @Schema(name = "areaCode", description = "地区编码", example = "1001")
    private String areaCode;

    @Schema(name = "children", description = "子级")
    private List<AreaDto> children;
}