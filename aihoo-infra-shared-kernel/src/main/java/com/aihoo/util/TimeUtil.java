package com.aihoo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtil {
    public static Date getDateByString(String time) {
        if (time == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(time);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getDayTime(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);
        return df.format(date);
    }

    public static String hourFormat(int h) {
        return h < 10 ? "0" + h : String.valueOf(h);
    }

    public static String getShortTime(String time) {
        String shortString = null;
        long now = Calendar.getInstance().getTimeInMillis();
        Date date = getDateByString(time);
        if (date == null) {
            return shortString;
        }
        long diff = (now - date.getTime()) / 1000;
        if (diff < 60) {
            return "刚刚";
        } else if (diff < 60 * 60) {
            return diff / 60 + "分钟前";
        } else if (diff < 60 * 60 * 24) {
            return diff / (60 * 60) + "小时前";
        } else if (diff < 60 * 60 * 24 * 30) {
            return diff / (60 * 60 * 24) + "天前";
        } else {
            return getDayTime(date, "yyyy-MM-dd");
        }
    }

    public static String getBetweenTime(Date start, Date end, Integer cutDown) {
        long between = cutDown * 1000 - start.getTime() + end.getTime();
        long day = between / (24 * 60 * 60 * 1000);
        if (day != 0) return "0";
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (hour < 0 || min < 0 || s < 0) {
            return "0";
        }
        return hourFormat((int) hour) + ":" + hourFormat((int) min) + ":" + hourFormat((int) s);
    }
}
