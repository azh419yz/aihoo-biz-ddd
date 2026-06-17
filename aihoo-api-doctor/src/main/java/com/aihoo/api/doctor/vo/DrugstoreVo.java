package com.aihoo.api.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class DrugstoreVo {

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "更新时间")
    private String updateTime;

    @Schema(description = "创建人id")
    private String createUserId;

    @Schema(description = "药房名称")
    private String name;

    @Schema(description = "药房图片OSS地址")
    private String image;

    @Schema(description = "省份CODE")
    private List<String> provinceList;

    @Schema(description = "药态CODE")
    private List<Integer> medicineStatusList;

    @Schema(description = "标签")
    private String tags;

    @Schema(description = "发货描述")
    private String dispatchDesc;

    @Schema(description = "状态(是否启用 1:启用 0:停用)")
    private String status;
}
