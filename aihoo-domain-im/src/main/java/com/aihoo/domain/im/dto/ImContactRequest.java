package com.aihoo.domain.im.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "获取用户Im会话请求体")
public class ImContactRequest {
    @NotBlank(message = "会话账号不能为空")
    private String From_Account;
    private Integer TimeStamp;
    private Integer StartIndex;
    private Integer TopTimeStamp;
    private Integer TopStartIndex;
    private Integer AssistFlags = 15;
}
