package com.aihoo.api.doctor.request;

import lombok.Data;

@Data
public class PrescriptionQueryRequest {

    private Long page = 1L;

    private Long limit = 10L;
}
