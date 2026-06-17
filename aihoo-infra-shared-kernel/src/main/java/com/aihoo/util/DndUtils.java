package com.aihoo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/30 21:00
 */
public class DndUtils {

    private static final Logger logger = LoggerFactory.getLogger(DndUtils.class);

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private DndUtils() {
    }

    /**
     * 检测免打扰状态
     *
     * @param configStr 配置字符串
     * @return DndResult 结果对象
     */
    public static DndResult checkDnd(String configStr) {
        return checkDnd(configStr, LocalTime.now());
    }

    /**
     * 核心检测方法 (支持指定时间，方便测试)
     */
    public static DndResult checkDnd(String configStr, LocalTime currentTime) {
        if (configStr == null || configStr.trim().isEmpty()) {
            return DndResult.miss();
        }

        String[] rules = configStr.split(",");

        for (String ruleStr : rules) {
            String[] parts = ruleStr.trim().split("-");
            if (parts.length != 3) continue;

            try {
                LocalTime start = LocalTime.parse(parts[0], TIME_FORMATTER);
                LocalTime end = LocalTime.parse(parts[1], TIME_FORMATTER);
                int crossDayFlag = Integer.parseInt(parts[2]);
                boolean isCrossDay = (crossDayFlag == 1);

                if (isTimeInRange(currentTime, start, end, isCrossDay)) {
                    // 命中！返回包含详细信息的对象
                    return DndResult.hit(start, end, isCrossDay);
                }
            } catch (Exception e) {
                // 解析错误忽略
            }
        }

        return DndResult.miss();
    }

    /**
     * 判断逻辑 (逻辑与之前相同，只是参数变了)
     */
    private static boolean isTimeInRange(LocalTime current, LocalTime start, LocalTime end, boolean isCrossDay) {
        if (!isCrossDay) {
            // 不跨天: start <= current <= end
            return !current.isBefore(start) && !current.isAfter(end);
        } else {
            // 跨天: current >= start OR current <= end
            return !current.isBefore(start) || !current.isAfter(end);
        }
    }
}
