package com.aihoo.domain.logistics.service;

import java.math.BigDecimal;

/**
 * 顺丰运费 service（迁自 patient-api 的 SFService）。
 */
public interface SFService {
    /**
     * @param from 发货地址
     * @param to   收货地址
     * @return 价格
     */
    BigDecimal getExpressPrice(String from, String to);
}