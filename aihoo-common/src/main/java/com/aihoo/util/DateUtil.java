package com.aihoo.util;

import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Log
public class DateUtil {

    
    public static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    
    public static final String DATE_FORMAT_2 = "yyyyMMddHHmmss";
    
    public static final String DATE_FORMAT_3 = "yyyyMMdd";
    
    public static final String DATE_FORMAT_4 = "HHmmss";
    
    public static final String DATE_FORMAT_5 = "HH";
    
    public static final String DATE_FORMAT_6 = "yyyy-MM-dd";
    
    public static final String DATE_FORMAT_7 = "yyyyMMddHHmm";
    
    public static final String DATE_FORMAT_8 = "MMddHHmmss";
    
    public static final String DATE_FORMAT_9 = "yyyyMMdd HH:mm:ss";
    
    public static final String DATE_FORMAT_10 = "MMdd";
    
    public static final String DATE_FORMAT_11 = "HH:mm:ss";

    
    public static final String DATE_FORMAT_12 = "yyyyMMddHHmmssSSS";

    
    public static final String DATE_FORMAT_13 = "yyMMddHHmmssSSS";

    
    public static final Integer ONE_MINUTE = 60;

    
    public static final Integer DATE_HALF_OF_hour = 1800;
    
    public static final Integer DATE_OF_hour = 3600;
    private static Map<String, ThreadLocal<SimpleDateFormat>> mapThreadLocal = new HashMap<String, ThreadLocal<SimpleDateFormat>>();
    private static final Object lockObj = new Object();
    private static DateUtil classInstance = new DateUtil();

    public static DateUtil getInstance() {
        return classInstance;
    }

    public static SimpleDateFormat getSimpleDateFormat(final String pattern) {
        ThreadLocal<SimpleDateFormat> df = mapThreadLocal.get(pattern);
        if (df == null) {
            synchronized (lockObj) {
                df = mapThreadLocal.get(pattern);
                if (df == null) {
                    df = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    mapThreadLocal.put(pattern, df);
                }
            }
        }
        return df.get();
    }

    
    public static String formatDate(Date date, String pattern) {
        String riqi = null;
        if (date != null && StringUtils.isNotBlank(pattern)) {
            SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
            riqi = dateFormat.format(date);
        }
        return riqi;
    }

    
    public static Date formatDate(String date, String pattern) throws ParseException {
        Date rq = null;
        if (StringUtils.isNotBlank(date) && StringUtils.isNotBlank(pattern)) {
            SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
            try {
                rq = dateFormat.parse(date);
            } catch (ParseException e) {
                throw e;
            }
        }
        return rq;
    }

    
    public static String getTime(String pattern) {
        return getSimpleDateFormat(pattern).format(new Date());
    }

    public static long getTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.getTimeInMillis();
    }

    
    public static String dateAdd(String date, String pattern, int subInt) throws ParseException {
        SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
        Date rq = dateFormat.parse(date);
        Calendar cdate = Calendar.getInstance();
        cdate.setTime(rq);
        cdate.set(Calendar.DATE, cdate.get(Calendar.DATE) + subInt);
        return dateFormat.format(cdate.getTime());
    }

    
    public static Date dateAdd(Date date, int field, int subInt) throws ParseException {
        Calendar cdate = Calendar.getInstance();
        cdate.setTime(date);
        cdate.set(field, cdate.get(field) + subInt);
        return cdate.getTime();
    }

    public static Date dateSub(Date date, String pattern, int field, int subInt) throws ParseException {
        Calendar cdate = Calendar.getInstance();
        cdate.setTime(date);
        cdate.set(field, cdate.get(field) - subInt);
        return cdate.getTime();
    }

    
    public static String dateSub(String date, String pattern, int subInt) throws ParseException {
        SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
        Date rq = dateFormat.parse(date);
        Calendar cdate = Calendar.getInstance();
        cdate.setTime(rq);
        cdate.set(Calendar.DATE, cdate.get(Calendar.DATE) - subInt);
        return dateFormat.format(cdate.getTime());
    }

    
    public static String dateSub(Date date, String pattern, int subInt) throws ParseException {
        SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
        String date_str = dateFormat.format(date);
        Date rq = dateFormat.parse(date_str);
        Calendar cdate = Calendar.getInstance();
        cdate.setTime(rq);
        cdate.set(Calendar.DATE, cdate.get(Calendar.DATE) - subInt);
        return dateFormat.format(cdate.getTime());
    }

    
    public static String addOrSubTime(String time, int type, int amount, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String date = "";
        try {
            Date dt = sdf.parse(time);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            switch (type) {
                case 0:
                    rightNow.add(Calendar.YEAR, amount);
                    break;
                case 1:
                    rightNow.add(Calendar.MONTH, amount);
                    break;
                case 2:
                    rightNow.add(Calendar.DAY_OF_YEAR, amount);
                    break;
            }
            date = sdf.format(rightNow.getTime());
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
        return date;
    }

    
    public static String getUpMonthFirstDay(String pattern) throws ParseException {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, -1);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        String firstDay = getSimpleDateFormat(pattern).format(cale.getTime());
        return firstDay;
    }

    
    public static String getUpMonthLastDay(String pattern) throws ParseException {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        String lastDay = getSimpleDateFormat(pattern).format(cale.getTime());
        return lastDay;
    }

    
    public static boolean isDate(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date d = sdf.parse(date);
            String newDate = sdf.format(d);
            if (null != newDate && newDate.equals(date)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    
    public static List<String> getTime(String date, String format, Integer type, Integer time) {
        List<String> times = new ArrayList<>();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date formatDate = simpleDateFormat.parse(date);
            for (int i = 1; i <= time; i++) {
                Date newDate = DateUtil.dateSub(formatDate, format, type, i);
                times.add(simpleDateFormat.format(newDate));
            }
        } catch (Exception e) {
            return times;
        }

        return times;
    }

    
    public static int daysBetween(String smdate, String bdate, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date s = sdf.parse(smdate);
        Date d = sdf.parse(bdate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(s);
        long time1 = cal.getTimeInMillis();
        cal.setTime(d);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    
    public static Map<String, String> getCycleDateMap(String sys_day, int cycle_count, int cycle_days) throws Exception {
        try {
            String startDate = sys_day;
            String endDate = null;
            Map<String, String> map = new TreeMap<String, String>();
            for (int i = 0; i < cycle_count; i++) {
                if (endDate == null) {
                    endDate = dateAdd(startDate, DATE_FORMAT_3, cycle_days);
                } else {
                    startDate = dateAdd(endDate, DATE_FORMAT_3, 1);
                    endDate = dateAdd(startDate, DATE_FORMAT_3, cycle_days);
                }
                map.put(startDate, endDate);
            }
            return map;
        } catch (Exception e) {
            throw e;
        }
    }

    final static String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static String dateToString(String pattern, Date date) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date getNow() {
        return new Date(System.currentTimeMillis());
    }

    
    public static Date getDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date zero = calendar.getTime();
        return zero;
    }

    
    public static Date getDayStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }

    public static String getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0) dayOfWeek = 0;
        return dayNames[dayOfWeek];
    }

    public static Date getDate(LocalDateTime localDateTime) {
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        Date date = Date.from(zdt.toInstant());
        return date;
    }

    public static LocalDateTime getDate(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return localDateTime;
    }

    public static String getYearMonthByRepaymentDate(int d, LocalDateTime localDateTime) {
        if (localDateTime.getDayOfMonth() < d) {
            localDateTime = localDateTime.minus(1, ChronoUnit.MONTHS);
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM");
        return df.format(localDateTime);
    }

    
    public static Boolean currentMonthIsPay(String payTime, int d, LocalDateTime localDateTime) {
        if (StringUtils.isEmpty(payTime)) {
            return false;
        } else {
            String m = getYearMonthByRepaymentDate(d, localDateTime);
            return calMonthDiff(payTime, m) <= 0;
        }
    }

    public static String getDateStr(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        String m = df.format(localDateTime);
        return m;
    }

    public static int calMothdiff(String repaymentStartMonth, Integer per, LocalDateTime localDateTime) {

        String startDateStr = repaymentStartMonth + String.format("%02d", Integer.valueOf(per));

        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        long between2 = ChronoUnit.MONTHS.between(startDate, localDateTime.toLocalDate());
        return (int) between2;
    }

    public static int calMonthDiff(String startMonth, String endMonth) {
        LocalDate startDate = LocalDate.parse(startMonth + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(endMonth + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        long between2 = ChronoUnit.MONTHS.between(startDate, endDate);
        return (int) between2;
    }

    
    public static String[] getYearMonth(String yearMonthStr) throws ParseException {
        Date d = formatDate(yearMonthStr + "-01", DATE_FORMAT_6);
        String startDate = null;
        String endDate = null;
        {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.DAY_OF_MONTH, 0);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            startDate = formatDate(c.getTime(), DATE_FORMAT_1);
        }

        {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.DAY_OF_MONTH, 0);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            endDate = formatDate(c.getTime(), DATE_FORMAT_1);
        }
        return new String[]{startDate, endDate};
    }

    public static Date getDayAfter(Date date, int num, int unit) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(unit, num);
            date = cal.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    
    public static Date getDateStart(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format = getDateFormat("yyyy-MM-dd");
            StringBuffer time = new StringBuffer();
            String dateString = format.format(date);
            time.append(dateString).append(" ").append("00:00:00");
            return sdf.parse(time.toString());
        } catch (ParseException e) {
            return date;
        }
    }

    public static Date parseDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateEnd(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format = getDateFormat("yyyy-MM-dd");
            StringBuffer time = new StringBuffer();
            String dateString = format.format(date);
            time.append(dateString).append(" ").append("23:59:59");
            return sdf.parse(time.toString());
        } catch (ParseException e) {
            return date;
        }
    }

    
    public static SimpleDateFormat getDateFormat(String parttern) throws RuntimeException {
        return new SimpleDateFormat(parttern);
    }

    public static Date getMonthOfFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getDateStart(cal.getTime());
    }

    public static Date getMonthOfLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 0);
        return getDateEnd(cal.getTime());
    }

    public static List<String> getDateList(Date startDate, Date endDate,
                                           String format) {
        List<String> list = new ArrayList<String>();
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            Calendar calend = Calendar.getInstance();
            calend.setTime(endDate);
            while (cal.before(calend)) {
                list.add(dateToString(format, cal.getTime()));
                cal.add(Calendar.DATE, 1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getDateList(Date startDate, Date endDate) {
        return getDateList(startDate, endDate, "yyyy-MM-dd");
    }

    
    public static Integer getAgeByBirth(String birthday) {
        if (StringUtils.isNoneBlank(birthday)) {
            SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sft.parse(birthday);
                return getAgeByBirth(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int getAgeByBirth(Date birthday) {
        Calendar cal = Calendar.getInstance();
        Calendar bir = Calendar.getInstance();
        bir.setTime(birthday);
        if (cal.before(birthday)) {
            throw new IllegalArgumentException("The birthday is before Now,It's unbelievable");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayNow = cal.get(Calendar.DAY_OF_MONTH);
        int yearBirth = bir.get(Calendar.YEAR);
        int monthBirth = bir.get(Calendar.MONTH);
        int dayBirth = bir.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow < monthBirth || (monthNow == monthBirth && dayNow < dayBirth)) {
            age--;
        }
        return age;
    }
}
