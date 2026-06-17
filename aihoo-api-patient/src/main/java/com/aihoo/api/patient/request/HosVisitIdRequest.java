package com.aihoo.api.patient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "问诊请求")
public class HosVisitIdRequest {

    @Schema(name = "hosVisitId", description = "问诊单ID", example = "1")
    @NotBlank(message = "问诊单ID不能为空")
    private String hosVisitId;

    @Schema(name = "hosSickId", description = "就诊人ID", example = "1")
    private String hosSickId;
}