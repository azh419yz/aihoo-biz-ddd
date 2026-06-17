package com.aihoo.domain.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 欢迎语设置 request DTO（迁自 doctor-api: DoctorWelcomeMessageRequest）。
 */
@Data
@Schema(description = "欢迎语设置请求")
public class DoctorWelcomeMessageRequest {

    @Schema(description = "是否开启自动发送 0-未开 1-开启", example = "1")
    @NotNull(message = "是否开启自动发送不能为空")
    private Integer isAuto;

    @Schema(description = "欢迎语", example = "有何吩咐？")
    private String welcomeMessage;
}
