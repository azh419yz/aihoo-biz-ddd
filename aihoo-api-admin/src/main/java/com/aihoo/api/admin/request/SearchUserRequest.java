package com.aihoo.api.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户查询请求")
public class SearchUserRequest {
    @Schema(description = "姓名")
    private String trueName;
    @Schema(description = "手机号码")
    private String phone;
}
