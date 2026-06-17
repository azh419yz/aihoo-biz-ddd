package com.aihoo.util;

public class ConvertorTime {

    public static String secToTimes(int seconds) {
        return msecToTime(seconds * 1000).substring(0, 8);
    }

    public static String secToTime(int seconds) {
        int hour = seconds / 3600;
        int minute = (seconds - hour * 3600) / 60;
        int second = (seconds - hour * 3600 - minute * 60);

        StringBuilder sb = new StringBuilder();
        if (hour > 0) {
            sb.append(hour).append("小时");
        }
        if (minute > 0) {
            sb.append(minute).append("分");
        }
        if (second > 0) {
            sb.append(second).append("秒");
        }
        if (second == 0) {
            sb.append("<1秒");
        }
        return sb.toString();
    }

    public static String msecToTime(int time) {
        if (time <= 0) return "00:00:00.000";

        int hour = 0;
        int minute = 0;
        int second = 0;
        int millisecond = 0;

        second = time / 1000;
        minute = second / 60;
        millisecond = time % 1000;

        if (second < 60) {
            return "00:00:" + unitFormat(second) + "." + unitFormat2(millisecond);
        } else if (minute < 60) {
            second = second % 60;
            return "00:" + unitFormat(minute) + ":" + unitFormat(second) + "." + unitFormat2(millisecond);
        } else {
            hour = minute / 60;
            minute = minute % 60;
            second = second - hour * 3600 - minute * 60;
            return unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second) + "."
                    + unitFormat2(millisecond);
        }
    }

    public static String unitFormat(int i) {
        return i >= 0 && i < 10 ? "0" + i : String.valueOf(i);
    }

    public static String unitFormat2(int i) {
        if (i >= 0 && i < 10) return "00" + i;
        else if (i >= 10 && i < 100) return "0" + i;
        else return String.valueOf(i);
    }
}