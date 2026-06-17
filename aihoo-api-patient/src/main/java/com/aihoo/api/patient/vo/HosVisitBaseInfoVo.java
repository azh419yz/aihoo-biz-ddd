package com.aihoo.api.patient.vo;

import com.aihoo.domain.visit.dto.HosVisitBaseInfoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HosVisitBaseInfoVo {
    @Schema(name = "hosVisitId", description = "问诊单ID", example = "1")
    private String hosVisitId;

    @Schema(name = "baseInfo", description = "基本情况")
    private HosVisitBaseInfoDTO baseInfo;

    @Schema(name = "createTime", description = "创建时间", example = "2020-01-01 15:30:00")
    private String createTime;
}