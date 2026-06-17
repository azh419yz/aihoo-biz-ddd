package com.aihoo.util;

import lombok.Getter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/30 21:28
 */
@Getter
public class DndResult {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private boolean hit;            // 是否命中
    private String startTime;       // 命中时段的开始时间 (HH:mm)
    private String endTime;         // 命中时段的结束时间 (HH:mm)
    private boolean isCrossDay;     // 是否跨天

    // 私有构造函数，强制使用工厂方法
    private DndResult(boolean hit) {
        this.hit = hit;
    }

    // 工厂方法：未命中
    public static DndResult miss() {
        return new DndResult(false);
    }

    // 工厂方法：命中
    public static DndResult hit(LocalTime start, LocalTime end, boolean crossDay) {
        DndResult result = new DndResult(true);
        result.startTime = start.format(FORMATTER);
        result.endTime = end.format(FORMATTER);
        result.isCrossDay = crossDay;
        return result;
    }

    @Override
    public String toString() {
        if (!hit) return "未命中免打扰";
        return String.format("命中免打扰: %s 至 %s (跨天:%s)", startTime, endTime, isCrossDay);
    }
}
