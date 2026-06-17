package com.aihoo.domain.visit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PrescriptionConsultationFeeDto {

    @Schema(name = "visitAmount", description = "诊金", example = "5.00")
    private String visitAmount;

    @Schema(name = "afterService", description = "是否诊后服务 1:是 0:否", example = "1")
    private String afterService;

    @Schema(name = "seePrescription", description = "是否可见处方 1:是 0:否", example = "1")
    private String seePrescription;

    @Schema(name = "drugAmount", description = "药费", example = "284.98")
    private String drugAmount;

    @Schema(name = "decoctionAmount", description = "代煎费", example = "100")
    private String decoctionAmount;

    @Schema(name = "serviceAmount", description = "诊后服务费", example = "90")
    private String serviceAmount;

    @Schema(name = "amount", description = "总费", example = "479.98")
    private String amount;
}