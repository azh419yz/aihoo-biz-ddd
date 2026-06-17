package com.aihoo.api.patient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "获取用户Im会话请求体")
public class ImContactRequest {
    @NotBlank(message = "会话账号不能为空")
    private String fromAccount;
    private Integer timeStamp;
    private Integer startIndex;
    private Integer topTimeStamp;
    private Integer topStartIndex;
    private Integer assistFlags = 15;
}
