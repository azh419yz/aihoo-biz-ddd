package com.aihoo.api.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DrugVo {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 创建人id
     */
    private String createUserId;

    /**
     * 药品名称
     */
    @Schema(name = "name", description = "药品名称")
    private String name;

    /**
     * 药品单价
     */
    @Schema(name = "price", description = "药品单价")
    private String price;

    @Schema(name = "method", description = "煎药方式")
    private String method;

    /**
     * 状态(是否启用 1:启用 0:停用)
     */
    @Schema(name = "status", description = "状态(是否启用 1:启用 0:停用)")
    private String status;

    /**
     * 药房ID
     */
    @Schema(name = "drugstoreId", description = "药房ID")
    private String drugstoreId;

    /**
     * 药房名称
     */
    @Schema(name = "drugstoreName", description = "药房名称")
    private String drugstoreName;
}
