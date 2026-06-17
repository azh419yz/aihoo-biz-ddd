package com.aihoo.domain.visit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "创建问诊单请求")
public class HosVisitCreateRequest {

    @Schema(name = "doctorId", description = "医生ID", example = "1")
    @NotBlank(message = "医生ID不能为空")
    private String doctorId;

    @Schema(name = "price", description = "价格", example = "120")
    @NotNull(message = "价格不能为空")
    private Integer price;
}