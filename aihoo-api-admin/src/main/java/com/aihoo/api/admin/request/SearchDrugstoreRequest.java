package com.aihoo.api.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "药房查询请求参数")
public class SearchDrugstoreRequest {

    @Schema(description = "药房名称", example = "北京朝阳大药房")
    private String name;

    @Schema(description = "所在省", example = "110000")
    private String provincesCode;

    @Schema(description = "药态CODE", example = "[1, 2, 3]")
    private List<Integer> medicineStatusList;
}
