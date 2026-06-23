package com.aihoo.domain.im.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "IM 通用响应")
public class ImRespDto {
    @Schema(description = "请求处理结果")
    private String ActionStatus;

    @Schema(description = "错误码")
    private Integer ErrorCode;

    @Schema(description = "详细错误信息")
    private String ErrorInfo;

    public boolean isSuccess() {
        return "OK".equalsIgnoreCase(ActionStatus);
    }
}
