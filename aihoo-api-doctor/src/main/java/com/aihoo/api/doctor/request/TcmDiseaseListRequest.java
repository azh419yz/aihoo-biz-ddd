package com.aihoo.api.doctor.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "获取中医疾病列表请求对象")
public class TcmDiseaseListRequest {

    @Schema(description = "疾病名称或拼音首字母模糊搜索")
    private String keyword;
}
