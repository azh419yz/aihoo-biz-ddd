package com.aihoo.api.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DrugVo {

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "药房ID")
    private String drugstoreId;

    @Schema(description = "药品名称")
    private String name;

    @Schema(description = "药品规格")
    private String size;

    @Schema(description = "药品剂型编码")
    private String drugDosCode;

    @Schema(description = "药品剂型")
    private String drugDosName;

    @Schema(description = "单位计量")
    private String unitMeasure;

    @Schema(description = "包装单位编码")
    private String packUnitCode;

    @Schema(description = "包装单位")
    private String packUnitName;

    @Schema(description = "药品单价")
    private String price;

    @Schema(description = "默认用药频次编码")
    private String freqMedCode;

    @Schema(description = "默认用药频次")
    private String freqMedName;

    @Schema(description = "默认用药途径编码")
    private String routeAdmiCode;

    @Schema(description = "默认用药途径")
    private String routeAdmiName;

    @Schema(description = "煎药方式")
    private String method;

    @Schema(description = "拼音首字母")
    private String pinyinInitial;

    @Schema(description = "是否抗生素")
    private String isAntibiotics;

    @Schema(description = "是否注射")
    private String isInjection;

    @Schema(description = "是否麻醉药")
    private String isAnesthesia;

    @Schema(description = "是否监控药物")
    private String isMonitor;

    @Schema(description = "药品说明书")
    private String content;

    @Schema(description = "状态(是否启用 1:启用 0:停用)")
    private String status;
}
