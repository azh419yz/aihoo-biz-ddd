package com.aihoo.api.patient.request;

import com.aihoo.domain.visit.dto.HosVisitBaseInfoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "问诊资料")
public class HosVisitInfoRequest {

    @Schema(name = "hosVisitId", description = "问诊单ID", example = "1")
    @NotBlank(message = "问诊单ID不能为空")
    private String hosVisitId;

    @Schema(name = "healthInfo", description = "问诊单json", example = "{\"1\":\"饮水正常\"}")
    private String healthInfo;

    @Schema(name = "baseInfo", description = "基本情况")
    private HosVisitBaseInfoDTO baseInfo;
}