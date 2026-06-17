package com.aihoo.api.patient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "删除收货地址请求对象")
public class PatientUserAddressDeleteRequest {
    @NotNull(message = "id不能为空")
    @Schema(name = "id", description = "地址id", example = "1")
    private Long id;
}
