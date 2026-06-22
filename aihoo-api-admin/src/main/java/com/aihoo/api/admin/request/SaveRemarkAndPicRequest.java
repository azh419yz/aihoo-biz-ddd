package com.aihoo.api.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "保存备注和图片请求参数")
public class SaveRemarkAndPicRequest {

    @Schema(description = "订单ID", example = "1")
    @NotBlank(message = "订单ID不能为空")
    private String orderId;

    @Schema(description = "药师备注", example = "慢点配")
    private String remark;

    @Schema(description = "药品照片URL列表", example = "[\"http://xxx/1.jpg\",\"http://xxx/2.jpg\"]")
    private List<String> picList;

}