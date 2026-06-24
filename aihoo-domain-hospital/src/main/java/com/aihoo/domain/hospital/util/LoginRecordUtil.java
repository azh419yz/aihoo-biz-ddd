package com.aihoo.domain.hospital.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginRecordUtil {

    public void saveLoginRecord(HttpServletRequest request, String operation) {
        log.info("[stub] saveLoginRecord operation={}", operation);
    }
}
