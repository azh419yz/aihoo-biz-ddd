package com.aihoo.domain.visit.dto;

import lombok.Data;

@Data
public class PrescriptionQueryDto {
    private Long page = 1L;
    private Long limit = 10L;
}
