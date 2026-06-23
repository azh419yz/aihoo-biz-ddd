package com.aihoo.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单保存响应 DTO（domain 内）。内部包含 result/msg/order。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MdtOrderSaveRespDto {
    private Boolean result;
    private String msg;
    private MdtOrderSaveView order;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MdtOrderSaveView {
        private String id;
        private String orderNum;
        private String preId;
        private String totalPrice;
        private String status;
    }
}
