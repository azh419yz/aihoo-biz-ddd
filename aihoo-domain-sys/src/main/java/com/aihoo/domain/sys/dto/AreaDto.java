package com.aihoo.domain.sys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AreaDto {

    @Schema(name = "name", description = "名称", example = "北京市")
    private String name;

    @Schema(name = "areaCode", description = "地区编码", example = "1001")
    private String areaCode;

    @Schema(name = "children", description = "子级")
    private List<AreaDto> children;
}