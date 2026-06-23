package com.aihoo.domain.im.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "im对话")
public class ImSessionItemDto {
    @Schema(description = "会话类型： 1： 表示 C2C 会话 2：表示 G2C 会话")
    private Integer Type;
    @Schema(description = "C2C 会话才会返回")
    private String To_Account;
    @Schema(description = "会话时间")
    private Integer MsgTime;
    @Schema(description = "置顶标记")
    private Integer TopFlag;
    @Schema(description = "头像")
    private String headImg;
    @Schema(description = "真实姓名")
    private String userName;
}
