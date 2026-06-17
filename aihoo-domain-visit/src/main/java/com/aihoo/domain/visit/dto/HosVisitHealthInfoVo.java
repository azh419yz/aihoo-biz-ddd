package com.aihoo.domain.visit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HosVisitHealthInfoVo {
    @Schema(name = "hosVisitId", description = "问诊单ID", example = "1")
    private String hosVisitId;

    @Schema(name = "healthInfo", description = "问诊单json", example = "{\"1\":\"饮水正常\"}")
    private String healthInfo;

    @Schema(name = "createTime", description = "创建时间", example = "2020-01-01 15:30:00")
    private String createTime;
}