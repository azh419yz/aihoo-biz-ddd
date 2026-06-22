package com.aihoo.api.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "药房新增修改请求参数")
public class SaveUpdateDrugstoreRequest {

    public interface Save {
    }

    public interface Update {
    }

    @Schema(description = "药房ID", example = "1")
    @NotBlank(message = "药房ID不能为空", groups = Update.class)
    private String id;

    @Schema(description = "药房名称", example = "北京朝阳大药房")
    @NotBlank(message = "药房名称不能为空", groups = Save.class)
    private String name;

    @Schema(description = "药房图片OSS地址", example = "https://aihoo-static.oss-cn-wulanchabu.aliyuncs.com/123.jpg")
    private String image;

    @Schema(description = "省份CODE", example = "['140000', '210000', '360000']")
    private List<String> provinceList;

    @Schema(description = "药态CODE", example = "[1, 2, 3]")
    private List<Integer> medicineStatusList;

    @Schema(description = "标签", example = "药品齐全")
    private String tags;

    @Schema(description = "发货描述", example = "16:00前下单当天寄出")
    private String dispatchDesc;

    @Schema(description = "状态(1:启用 0:停用)", example = "1")
    private String status;
}
