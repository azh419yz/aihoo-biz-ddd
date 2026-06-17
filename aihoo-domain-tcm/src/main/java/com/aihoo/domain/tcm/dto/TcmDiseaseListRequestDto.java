package com.aihoo.domain.tcm.dto;

import lombok.Data;

/**
 * 中医疾病列表查询 DTO（domain 层用，与 api 层 request 字段对齐）。
 */
@Data
public class TcmDiseaseListRequestDto {
    private String keyword;
}
