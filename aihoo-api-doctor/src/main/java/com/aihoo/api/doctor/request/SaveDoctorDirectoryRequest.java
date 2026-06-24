package com.aihoo.api.doctor.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "保存医生通讯录请求")
public class SaveDoctorDirectoryRequest {
    @Schema(description = "就诊人id")
    private Long sickId;
    @Schema(description = "患者id")
    private Long patientUserId;
    @Schema(description = "医生id")
    private Long doctorId;
    @Schema(description = "患者名称")
    private String sickName;
}
