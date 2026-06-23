package com.aihoo.domain.visit.dto;

import lombok.Data;

/**
 * 问诊资料 DTO（domain 内，service 入参）。
 */
@Data
public class HosVisitInfoDto {
    private String hosVisitId;
    private String healthInfo;
    private HosVisitBaseInfoDTO baseInfo;
}
