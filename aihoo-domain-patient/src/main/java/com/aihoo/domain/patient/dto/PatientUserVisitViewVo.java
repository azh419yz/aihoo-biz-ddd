package com.aihoo.domain.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户我的问诊页面")
public class PatientUserVisitViewVo {
    @Schema(description = "患者列表")
    List<HosSickDto> hosSicks;
}
