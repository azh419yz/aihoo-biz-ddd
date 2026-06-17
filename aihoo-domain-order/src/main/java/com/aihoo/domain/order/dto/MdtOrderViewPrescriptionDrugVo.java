package com.aihoo.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MdtOrderViewPrescriptionDrugVo {
    @Schema(description = "主键ID")
    private String id;
    @Schema(description = "药品名称")
    private String name;
    @Schema(description = "药品用量")
    private String dosage;
}