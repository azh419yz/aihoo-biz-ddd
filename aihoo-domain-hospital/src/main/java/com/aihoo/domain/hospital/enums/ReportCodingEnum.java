package com.aihoo.domain.hospital.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
