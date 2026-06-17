package com.aihoo.api.doctor.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "获取中医证候列表请求对象")
public class TcmSyndromeListRequest {

    @Schema(description = "证候名称模糊搜索")
    private String keyword;
}
