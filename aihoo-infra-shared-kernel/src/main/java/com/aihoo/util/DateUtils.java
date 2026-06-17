package com.aihoo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtils {

    private static final Logger log = LoggerFactory.getLogger(DateUtils.class);

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    private DateUtils() {
    }

    public static SimpleDateFormat getDateFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf;
    }

    public static Date parseDate(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        try {
            return getDateFormat(DATE_FORMAT).parse(str);
        } catch (ParseException e) {
            log.warn("解析日期字符串出错！格式：yyyy-MM-dd", e);
            return null;
        }
    }

    public static Date parseDate(String str, String pattern) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        try {
            return getDateFormat(pattern).parse(str);
        } catch (ParseException e) {
            log.warn("解析日期字符串出错！格式：" + pattern, e);
            return null;
        }
    }

    public static Date parseDatetime(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        try {
            return getDateFormat(DATETIME_FORMAT).parse(str);
        } catch (ParseException e) {
            log.warn("解析日期字符串出错！格式：yyyy-MM-dd HH:mm:ss", e);
            return null;
        }
    }
}