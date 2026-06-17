package com.aihoo.domain.visit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PrescriptionDrugDto {

    @Schema(name = "drugId", description = "药品ID", example = "1")
    private String drugId;

    @Schema(name = "name", description = "药品名称", example = "葛根")
    private String name;

    @Schema(name = "price", description = "价格", example = "2.8")
    private String price;

    @Schema(name = "number", description = "数量", example = "10")
    private String number;

    @Schema(name = "method", description = "方式", example = "10")
    private String method;

    @Schema(name = "size", description = "规格")
    private String size;

    @Schema(name = "drugDosCode", description = "药品剂型编码")
    private String drugDosCode;

    @Schema(name = "drugDosName", description = "药品剂型")
    private String drugDosName;

    @Schema(name = "unitMeasure", description = "单位计量")
    private String unitMeasure;

    @Schema(name = "packUnitCode", description = "包装单位编码")
    private String packUnitCode;

    @Schema(name = "packUnitName", description = "包装单位")
    private String packUnitName;

    @Schema(name = "freqMedCode", description = "默认用药频次编码")
    private String freqMedCode;

    @Schema(name = "routeAdmiCode", description = "默认用药途径编码")
    private String routeAdmiCode;

    @Schema(name = "dosage", description = "用量")
    private String dosage;

    @Schema(name = "useDay", description = "用药天数")
    private String useDay;

    @Schema(name = "isAntibiotics", description = "是否抗生素")
    private String isAntibiotics;

    @Schema(name = "isInjection", description = "是否注射")
    private String isInjection;

    @Schema(name = "isAnesthesia", description = "是否麻醉药")
    private String isAnesthesia;

    @Schema(name = "isMonitor", description = "是否监控药物")
    private String isMonitor;

    @Schema(name = "content", description = "药品说明书")
    private String content;

    @Schema(name = "erp", description = "erp编码")
    private String erp;

    @Schema(name = "erpId", description = "erpId")
    private String erpId;
}
