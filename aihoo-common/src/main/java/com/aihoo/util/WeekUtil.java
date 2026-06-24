package com.aihoo.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class WeekUtil {
    public static int getMinute(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.MINUTE);
    }

    public static Date addMinute(Date time, int minute) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(time);
        instance.add(Calendar.MINUTE, minute);
        return instance.getTime();
    }

    public static String dateToWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        String weekCode = "Monday";
        switch (week) {
            case 1: weekCode = "Sunday"; break;
            case 2: weekCode = "Monday"; break;
            case 3: weekCode = "Tuesday"; break;
            case 4: weekCode = "Wednesday"; break;
            case 5: weekCode = "Thursday"; break;
            case 6: weekCode = "Friday"; break;
            case 7: weekCode = "Saturday"; break;
            default: break;
        }
        return weekCode;
    }

    public static String todayWeek() {
        return dateToWeek(new Date());
    }

    public static boolean inTime(String now, String startTime, String endTime) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date date = parseTime(df, now);
        Date begin = parseTime(df, startTime);
        Date end = parseTime(df, endTime);
        return inTime(date, begin, end);
    }

    public static boolean inTime(Date now, Date startTime, Date endTime) {
        return now.after(startTime) && now.before(endTime);
    }

    private static Date parseTime(SimpleDateFormat df, String time) {
        try {
            return df.parse(time);
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean inEqTime(String one, String two, String startTime, String endTime) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date date1 = parseTime(df, one);
        Date date2 = parseTime(df, two);
        Date begin = parseTime(df, startTime);
        Date end = parseTime(df, endTime);
        return (date1.after(begin) || one.equals(startTime))
                && (date2.before(end) || two.equals(endTime));
    }
}
