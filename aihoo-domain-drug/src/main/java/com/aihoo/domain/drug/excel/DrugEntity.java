package com.aihoo.domain.drug.excel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 药品 Excel 导入/导出实体（admin 阶段老规范）。
 *
 * <p>字段与 Drug entity 大致一致，无 @TableName 注解，由 ExcelUtils 直接读取/写入。
 * <p>仅用于 drugBulkExport/drugExcelParsing 端点（admin 阶段桩实现）。
 */
@Data
@Schema(description = "药品 Excel 实体")
public class DrugEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "创建人名称")
    private String createUserName;

    @Schema(description = "药品名称")
    private String name;

    @Schema(description = "药品规格")
    private String size;

    @Schema(description = "药品剂型编码")
    private String drugDosCode;

    @Schema(description = "药品剂型")
    private String drugDosName;

    @Schema(description = "单位剂量")
    private String unitMeasure;

    @Schema(description = "剂量单位")
    private String doseUnit;

    @Schema(description = "剂量单位编码")
    private String doseUnitCode;

    @Schema(description = "厂家")
    private String manufacturers;

    @Schema(description = "批准文号")
    private String approvalNumber;

    @Schema(description = "包装单位")
    private String packUnitName;

    @Schema(description = "包装单位编码")
    private String packUnitCode;

    @Schema(description = "药品单价")
    private String price;

    @Schema(description = "默认用药频次")
    private String freqMedName;

    @Schema(description = "默认用药频次编码")
    private String freqMedCode;

    @Schema(description = "默认用药途径")
    private String routeAdmiName;

    @Schema(description = "默认用药途径编码")
    private String routeAdmiCode;

    @Schema(description = "是否基药")
    private String isBasicMedicine;

    @Schema(description = "基药标识")
    private String basicMedicine;

    @Schema(description = "基药标识编码")
    private String basicMedicineCode;

    @Schema(description = "是否抗生素")
    private String isAntibiotics;

    @Schema(description = "是否注射")
    private String isInjection;

    @Schema(description = "是否麻醉药")
    private String isAnesthesia;

    @Schema(description = "是否监控药物")
    private String isMonitor;

    @Schema(description = "精神药物级别")
    private String psychotropicDrug;

    @Schema(description = "是否院内制剂")
    private String hospitalPreparations;

    @Schema(description = "供应商")
    private String supplier;

    @Schema(description = "阳光码")
    private String healthCode;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "创建人id")
    private String createUserId;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "更新时间")
    private String updateTime;
}
