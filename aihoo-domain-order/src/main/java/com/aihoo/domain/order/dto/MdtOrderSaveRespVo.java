package com.aihoo.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "创建订单返回结果")
@NoArgsConstructor
@AllArgsConstructor
public class MdtOrderSaveRespVo {
    private Boolean result;
    private String msg;
    private MdtOrderSaveView order;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "保存成功后返回order视图")
    public static class MdtOrderSaveView {
        private Long id;
        private String orderNum;
        private Long preId;
    }
}
