package com.aihoo.domain.visit.dto;

import lombok.Data;

@Data
public class PrescriptionSelectDto {
    private Long hosSickId;
    private Integer page;
    private Integer limit;
}