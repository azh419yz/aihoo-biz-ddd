package com.aihoo.util;

import cn.hutool.core.util.IdcardUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CardUtil {

    public static Map<String, String> getCarInfo(String cardCode) {
        Map<String, String> map = new HashMap<>();
        try {
            String year = cardCode.substring(6).substring(0, 4);
            String yue = cardCode.substring(10).substring(0, 2);
            String day = cardCode.substring(12).substring(0, 2);
            String sex;
            if (IdcardUtil.getGenderByIdCard(cardCode) == 0) {
                sex = "女";
            } else {
                sex = "男";
            }
            String birthday = year + "-" + yue + "-" + day;
            map.put("sex", sex);
            map.put("age", IdcardUtil.getAgeByIdCard(cardCode) + "");
            map.put("birthday", birthday);
        } catch (Exception e) {
            // ignore
        }
        return map;
    }

    public static int getAgeByBirth(Date birthDay) {
        if (birthDay == null) return 0;
        int age = 0;
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            return 0;
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;
    }

    public static Map<String, Object> getCarInfo15W(String card) {
        Map<String, Object> map = new HashMap<>();
        try {
            String uyear = "19" + card.substring(6, 8);
            String uyue = card.substring(8, 10);
            String usex = card.substring(14, 15);
            String sex;
            if (Integer.parseInt(usex) % 2 == 0) {
                sex = "女";
            } else {
                sex = "男";
            }
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String fyear = format.format(date).substring(0, 4);
            String fyue = format.format(date).substring(5, 7);
            int age = 0;
            if (Integer.parseInt(uyue) <= Integer.parseInt(fyue)) {
                age = Integer.parseInt(fyear) - Integer.parseInt(uyear) + 1;
            } else {
                age = Integer.parseInt(fyear) - Integer.parseInt(uyear);
            }
            map.put("sex", sex);
            map.put("age", age);
        } catch (Exception e) {
            // ignore
        }
        return map;
    }

    private static class SimpleDateFormat {
        private String pattern;

        public SimpleDateFormat(String pattern) {
            this.pattern = pattern;
        }

        public String format(Date date) {
            if (date == null) return "";
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);
            return sdf.format(date);
        }
    }
}