package com.aihoo.domain.visit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PrescriptionDrugDto {

    @Schema(name = "drugId", description = "药品ID", example = "1")
    private String drugId;

    @Schema(name = "name", description = "药品名称", example = "葛根")
    private String name;

    @Schema(name = "price", description = "价格", example = "2.8")
    private String price;

    @Schema(name = "number", description = "数量", example = "10")
    private String number;

    @Schema(name = "method", description = "方式", example = "10")
    private String method;
}