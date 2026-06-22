package com.aihoo.domain.drug.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 操作日志工具（admin 阶段桩实现）。
 *
 * <p>原 admin 阶段会写登录/操作日志表。当前 DDD 阶段操作日志基础设施未迁移，
 * 此处做桩：仅记录日志，不写库。
 */
@Slf4j
@Component
public class LoginRecordUtil {

    public void saveLoginRecord(HttpServletRequest request, String operation) {
        log.info("[stub] saveLoginRecord operation={}", operation);
    }
}
