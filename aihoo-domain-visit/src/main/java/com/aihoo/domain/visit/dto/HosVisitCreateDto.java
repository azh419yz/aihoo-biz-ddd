package com.aihoo.domain.visit.dto;

import lombok.Data;

/**
 * 创建问诊单 DTO（domain 内，service 入参）。
 */
@Data
public class HosVisitCreateDto {
    private String doctorId;
    private Integer price;
}
