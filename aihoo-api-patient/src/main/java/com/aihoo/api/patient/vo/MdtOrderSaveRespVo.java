package com.aihoo.api.patient.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "创建订单返回结果")
@NoArgsConstructor
@AllArgsConstructor
public class MdtOrderSaveRespVo {
    @Schema(description = "执行结果")
    private Boolean result;
    @Schema(description = "返回信息")
    private String msg;
    @Schema(description = "保存成功后返回order视图")
    private MdtOrderSaveView order;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "保存成功后返回order视图")
    public static class MdtOrderSaveView {
        @Schema(description = "订单ID")
        private Long id;
        @Schema(description = "订单号")
        private String orderNum;
        @Schema(description = "处方ID")
        private Long preId;
    }
}
