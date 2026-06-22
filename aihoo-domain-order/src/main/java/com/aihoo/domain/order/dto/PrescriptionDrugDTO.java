package com.aihoo.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PrescriptionDrugDTO {

    @Schema(name = "drugId", description = "药品ID", example = "1")
    private String drugId;

    @Schema(name = "name", description = "药品名称", example = "葛根")
    private String name;

    @Schema(name = "price", description = "价格", example = "2.8")
    private String price;

    @Schema(name = "number", description = "数量", example = "10")
    private String number;
}