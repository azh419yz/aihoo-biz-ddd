package com.aihoo.api.patient.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "IM 通用响应")
public class ImRespVo {
    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;

    public boolean isSuccess() {
        return "OK".equals(ActionStatus);
    }
}
