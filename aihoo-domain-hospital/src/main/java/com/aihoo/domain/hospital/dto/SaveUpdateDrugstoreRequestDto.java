package com.aihoo.domain.hospital.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "药房新增/修改请求（admin 阶段）")
public class SaveUpdateDrugstoreRequestDto {

    public interface Save {
    }

    public interface Update {
    }

    @Schema(description = "药房ID")
    @NotBlank(message = "药房ID不能为空", groups = Update.class)
    private String id;

    @Schema(description = "药房名称")
    @NotBlank(message = "药房名称不能为空", groups = Save.class)
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

    @Schema(description = "状态(1:启用 0:停用)")
    private String status;
}
