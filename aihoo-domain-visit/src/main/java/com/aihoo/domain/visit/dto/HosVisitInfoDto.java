package com.aihoo.domain.visit.dto;

import lombok.Data;

@Data
public class HosVisitInfoDto {
    private String hosVisitId;
    private String healthInfo;
    private HosVisitBaseInfoDTO baseInfo;
}
