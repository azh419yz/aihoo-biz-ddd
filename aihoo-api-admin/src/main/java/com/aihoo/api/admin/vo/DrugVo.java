package com.aihoo.api.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DrugVo {
    
    private String id;

    
    private String createTime;

    
    private String updateTime;

    
    private String createUserId;

    
    @Schema(name = "name", description = "药品名称")
    private String name;

    
    @Schema(name = "price", description = "药品单价")
    private String price;

    @Schema(name = "method", description = "煎药方式")
    private String method;

    
    @Schema(name = "status", description = "状态(是否启用 1:启用 0:停用)")
    private String status;

    
    @Schema(name = "drugstoreId", description = "药房ID")
    private String drugstoreId;

    
    @Schema(name = "drugstoreName", description = "药房名称")
    private String drugstoreName;
}
