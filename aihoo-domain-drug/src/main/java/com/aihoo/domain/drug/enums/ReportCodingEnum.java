package com.aihoo.domain.drug.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 医保上报编码常量（admin 阶段桩实现）。
 *
 * <p>原 admin 阶段用于 DicMedicines 写入医保上报数据。drugBulkImport/insert 老规范方法
 * 不在本期迁移范围（DrugController 未调用），此处仅保留枚举结构供 service 引用。
 */
@Getter
@AllArgsConstructor
public enum ReportCodingEnum {

    YLJGDM(""),
    WSJGDM(""),
    KSSBZ(""),
    DMJFBS(""),
    XGBZ("");

    private final String code;
}
