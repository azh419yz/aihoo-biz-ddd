package com.aihoo.api.doctor.request;

import lombok.Data;

/**
 * 医生端-处方分页查询请求。
 */
@Data
public class PrescriptionQueryRequest {

    private Long page = 1L;

    private Long limit = 10L;
}
