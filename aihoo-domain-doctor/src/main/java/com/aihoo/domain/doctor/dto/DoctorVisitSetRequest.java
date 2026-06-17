package com.aihoo.domain.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 问诊设置 request DTO（迁自 doctor-api: DoctorVisitSetRequest）。
 */
@Data
@Schema(description = "问诊设置请求")
public class DoctorVisitSetRequest {

    @Schema(description = "是否开启图文问诊 0-未开 1-开启", example = "1")
    @NotNull(message = "是否开启图文问诊不能为空")
    private Integer isImg;

    @Schema(description = "图文问诊价格", example = "30")
    @NotNull(message = "图文问诊价格不能为空")
    private Integer imgPrice;

    @Schema(description = "接单上限", example = "99")
    @NotNull(message = "接单上限不能为空")
    private Integer upperLimit;

    @Schema(description = "是否开启免打扰时段 0-未开 1-开启", example = "0")
    @NotNull(message = "是否开启免打扰时段不能为空")
    private Integer isDisturb;

    @Schema(description = "免打扰时段(开始时间-结束时间-跨天, 逗号分割)", example = "18:00-08:00-1,11:30-13:00-0")
    private String noDisturbTime;
}
