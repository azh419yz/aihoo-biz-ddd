package com.aihoo.api.patient.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CallBackVo {
    private String ActionStatus;
    private String ErrorInfo;
    private String ErrorCode;
}
