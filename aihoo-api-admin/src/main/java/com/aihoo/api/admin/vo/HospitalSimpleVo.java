package com.aihoo.api.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "医院简要信息")
public class HospitalSimpleVo {

    @Schema(description = "医院id")
    private String id;

    @Schema(description = "医院名称")
    private String hosName;
}