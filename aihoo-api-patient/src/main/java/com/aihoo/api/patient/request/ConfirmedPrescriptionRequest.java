package com.aihoo.api.patient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "确认处方")
public class ConfirmedPrescriptionRequest {
    @Schema(description = "处方ID")
    private Long prescriptionId;
}