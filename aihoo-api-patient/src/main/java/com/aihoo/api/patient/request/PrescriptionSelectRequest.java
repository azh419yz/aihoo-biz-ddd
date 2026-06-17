package com.aihoo.api.patient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "开方请求")
public class PrescriptionSelectRequest {
    @Schema(description = "患者id")
    private Long hosSickId;
    private Integer page;
    private Integer limit;
}