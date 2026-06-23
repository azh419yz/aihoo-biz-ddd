package com.aihoo.domain.visit.dto;

import lombok.Data;

/**
 * 问诊健康资料响应 DTO（domain 内，service 返回）。
 */
@Data
public class HosVisitHealthInfoDto {
    private String hosVisitId;
    private String healthInfo;
    private String createTime;
}
