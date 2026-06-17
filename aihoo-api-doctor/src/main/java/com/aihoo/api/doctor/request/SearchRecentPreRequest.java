package com.aihoo.api.doctor.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 医生端-最近处方查询请求。
 */
@Data
public class SearchRecentPreRequest {

    @NotBlank(message = "就诊人ID不能为空")
    private String hosSickId;

    @NotBlank(message = "医生ID不能为空")
    private String doctorUserId;
}
