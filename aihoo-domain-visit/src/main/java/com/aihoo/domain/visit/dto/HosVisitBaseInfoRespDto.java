package com.aihoo.domain.visit.dto;

import lombok.Data;

@Data
public class HosVisitBaseInfoRespDto {
    private String hosVisitId;
    private HosVisitBaseInfoDTO baseInfo;
    private String createTime;
}
