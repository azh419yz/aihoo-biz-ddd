package com.aihoo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DndUtils {

    private static final Logger logger = LoggerFactory.getLogger(DndUtils.class);

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private DndUtils() {
    }

    
    public static DndResult checkDnd(String configStr) {
        return checkDnd(configStr, LocalTime.now());
    }

    
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

                    return DndResult.hit(start, end, isCrossDay);
                }
            } catch (Exception e) {

            }
        }

        return DndResult.miss();
    }

    
    private static boolean isTimeInRange(LocalTime current, LocalTime start, LocalTime end, boolean isCrossDay) {
        if (!isCrossDay) {

            return !current.isBefore(start) && !current.isAfter(end);
        } else {

            return !current.isBefore(start) || !current.isAfter(end);
        }
    }
}
