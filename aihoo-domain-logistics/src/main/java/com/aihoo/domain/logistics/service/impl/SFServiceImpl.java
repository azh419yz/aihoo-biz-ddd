package com.aihoo.domain.logistics.service.impl;

import com.aihoo.domain.logistics.dto.sf.AddressRequest;
import com.aihoo.domain.logistics.dto.sf.OrderPriceRequest;
import com.aihoo.domain.logistics.service.SFService;
import com.aihoo.domain.logistics.util.SFUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 顺丰运费 service 实现（迁自 patient-api 的 SFServiceImpl）。
 */
@Service
@Log4j2
public class SFServiceImpl implements SFService {

    @Override
    public BigDecimal getExpressPrice(String from, String to) {
        try {
            AddressRequest srcAddr = new AddressRequest();
            srcAddr.setProvince(from);
            AddressRequest destAddr = new AddressRequest();
            destAddr.setProvince(to);
            OrderPriceRequest bizReq = new OrderPriceRequest();
            bizReq.setWeight(1.0);
            bizReq.setBusinessType("1");
            bizReq.setSearchPrice("1");
            bizReq.setSrcAddress(srcAddr);
            bizReq.setDestAddress(destAddr);
            return SFUtil.queryPrice(bizReq);
        } catch (Exception e) {
            log.info("顺丰计算价格异常", e);
        }
        return new BigDecimal("0.0");
    }
}