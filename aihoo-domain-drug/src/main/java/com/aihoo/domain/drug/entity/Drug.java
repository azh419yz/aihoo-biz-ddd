package com.aihoo.domain.drug.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 药品信息表（迁自 patient-api 的 Drug 实体）。
 *
 * <p>注意：doctor-api 也有同名 Drug 实体，字段比本实体多（drugstoreId/method/pinyinInitial/basicMedicine* 等），
 * 后续迁移 doctor-api 时按 controller 合并方法、并按需扩展本实体字段。
 *
 * @author carl
 * @since 2020-09-27
 */
@Data
@TableName("t_drug")
@Schema(description = "药品信息表")
public class Drug implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(description = "药房ID")
    @TableField("drugstore_id")
    private String drugstoreId;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private String createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private String updateTime;

    @Schema(description = "创建人id")
    @TableField("create_user_id")
    private String createUserId;

    @Schema(description = "药品名称")
    @TableField("name")
    private String name;

    @Schema(description = "药品规格")
    @TableField("size")
    private String size;

    @Schema(description = "煎药方式")
    @TableField("method")
    private String method;

    @Schema(description = "拼音首字母")
    @TableField("pinyin_initial")
    private String pinyinInitial;

    @Schema(description = "药品剂型编码 d_dict type=DRUG_DOS")
    @TableField("drug_dos_code")
    private String drugDosCode;

    @Schema(description = "药品剂型")
    @TableField("drug_dos_name")
    private String drugDosName;

    @Schema(description = "单位计量")
    @TableField("unit_measure")
    private String unitMeasure;

    @Schema(description = "包装单位编码 d_dict type=PACK_UNIT")
    @TableField("pack_unit_code")
    private String packUnitCode;

    @Schema(description = "包装单位")
    @TableField("pack_unit_name")
    private String packUnitName;

    @Schema(description = "药品单价")
    @TableField("price")
    private String price;

    @Schema(description = "默认用药频次编码 d_dict type=FREQ_MED")
    @TableField("freq_med_code")
    private String freqMedCode;

    @Schema(description = "默认用药频次")
    @TableField("freq_med_name")
    private String freqMedName;

    @Schema(description = "默认用药途径编码 d_dict type=ROUTE_ADMI")
    @TableField("route_admi_code")
    private String routeAdmiCode;

    @Schema(description = "默认用药途径")
    @TableField("route_admi_name")
    private String routeAdmiName;

    @Schema(description = "是否抗生素")
    @TableField("is_antibiotics")
    private String isAntibiotics;

    @Schema(description = "是否注射")
    @TableField("is_injection")
    private String isInjection;

    @Schema(description = "是否麻醉药")
    @TableField("is_anesthesia")
    private String isAnesthesia;

    @Schema(description = "是否监控药物")
    @TableField("is_monitor")
    private String isMonitor;

    @Schema(description = "药品说明书")
    @TableField("content")
    private String content;

    @Schema(description = "状态(是否启用 1:启用 0:停用)")
    @TableField("status")
    private String status;

    @Schema(description = "erp编码")
    @TableField("erp")
    private String erp;

    @Schema(description = "erpId")
    @TableField("erp_id")
    private String erpId;

    @Schema(description = "药品编号")
    @TableField("sku_code")
    private String skuCode;

    @Schema(description = "合偶平方id")
    @TableField("msh_id")
    private String mshId;

    @Schema(description = "阳光码")
    @TableField("health_code")
    private String healthCode;
}
