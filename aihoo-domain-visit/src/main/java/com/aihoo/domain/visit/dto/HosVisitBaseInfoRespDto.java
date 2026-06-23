package com.aihoo.domain.visit.dto;

import lombok.Data;

/**
 * 问诊基本资料响应 DTO（domain 内，service 返回）。
 * 命名避开 HosVisitBaseInfoDTO 以适应 macOS 大小写不敏感文件系统。
 */
@Data
public class HosVisitBaseInfoRespDto {
    private String hosVisitId;
    private HosVisitBaseInfoDTO baseInfo;
    private String createTime;
}
