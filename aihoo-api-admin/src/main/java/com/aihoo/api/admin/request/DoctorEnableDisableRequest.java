package com.aihoo.api.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "医生启用/禁用请求")
public class DoctorEnableDisableRequest {

    @Schema(description = "医生id")
    @NotBlank(message = "未携带医生id")
    private String id;

    @Schema(description = "状态 0-停用 1-启用")
    @NotBlank(message = "status不明确（未携带状态）")
    private String status;
}