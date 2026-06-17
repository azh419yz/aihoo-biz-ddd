package com.aihoo.api.doctor.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "问诊复诊请求")
public class VisitChatRequest {

    @Schema(name = "id", description = "复诊订单id", example = "1")
    @NotBlank(message = "复诊订单id不能为空")
    private String id;
}
