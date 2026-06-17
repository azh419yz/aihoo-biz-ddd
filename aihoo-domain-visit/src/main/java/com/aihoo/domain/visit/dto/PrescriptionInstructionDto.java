package com.aihoo.domain.visit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PrescriptionInstructionDto {

    @Schema(name = "usage", description = "用法 1:内服 2:外用", example = "1")
    private String usage;

    @Schema(name = "doseNumber", description = "全部剂数", example = "14")
    private String doseNumber;

    @Schema(name = "dose", description = "每日剂量", example = "2")
    private String dose;

    @Schema(name = "times", description = "每剂使用次数", example = "1")
    private String times;

    @Schema(name = "decoctionMethod", description = "煎药方式 1:代煎 2:自煎", example = "1")
    private String decoctionMethod;

    @Schema(name = "decoctionSize", description = "煎药规格 1:100ml/袋 2:200ml/袋", example = "1")
    private String decoctionSize;

    @Schema(name = "advice", description = "医嘱", example = "好好睡觉")
    private String advice;

    @Schema(name = "remark", description = "备注", example = "无")
    private String remark;
}