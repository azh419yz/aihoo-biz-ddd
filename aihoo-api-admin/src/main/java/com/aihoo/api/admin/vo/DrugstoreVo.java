package com.aihoo.api.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class DrugstoreVo {
    
    private String id;

    
    private String createTime;

    
    private String updateTime;

    
    private String createUserId;

    @Schema(name = "name", description = "药房名称")
    private String name;

    @Schema(name = "image", description = "药房图片OSS地址", example = "https://aihoo-static.oss-cn-wulanchabu.aliyuncs.com/123.jpg")
    private String image;

    @Schema(description = "省份CODE", example = "['140000', '210000', '360000']")
    private List<String> provinceList;

    @Schema(description = "药态CODE", example = "[1, 2, 3]")
    private List<Integer> medicineStatusList;

    @Schema(name = "tags", description = "标签", example = "药品齐全")
    private String tags;

    @Schema(name = "dispatchDesc", description = "发货描述", example = "16:00前下单当天寄出")
    private String dispatchDesc;

    @Schema(name = "status", description = "状态(是否启用 1:启用 0:停用)")
    private String status;
}
