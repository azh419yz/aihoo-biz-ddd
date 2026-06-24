package com.aihoo.domain.logistics.service;

import java.math.BigDecimal;

public interface SFService {
    
    BigDecimal getExpressPrice(String from, String to);
}