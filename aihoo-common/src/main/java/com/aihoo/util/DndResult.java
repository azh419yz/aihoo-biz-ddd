package com.aihoo.util;

import lombok.Getter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
public class DndResult {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private boolean hit;
    private String startTime;
    private String endTime;
    private boolean isCrossDay;

    private DndResult(boolean hit) {
        this.hit = hit;
    }

    public static DndResult miss() {
        return new DndResult(false);
    }

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
