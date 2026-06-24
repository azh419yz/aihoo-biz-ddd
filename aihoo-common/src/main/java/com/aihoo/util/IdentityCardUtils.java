package com.aihoo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class IdentityCardUtils {
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)|(^\\d{17}X$)";

    public static boolean isIdCard(String id) {
        return id.matches(REGEX_ID_CARD);
    }

    public static Map<String, String> getCarMessage(String id) throws Exception {
        if (18 == id.length()) {
            return getCarInfo(id);
        } else {
            return getCarInfo15W(id);
        }
    }

    public static boolean isValidEmail(String email) {
        if ((email != null) && (!email.isEmpty())) {
            return Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
        }
        return false;
    }

    
    public static Map<String, String> getCarInfo(String CardCode)
            throws Exception {
        Map<String, String> map = new HashMap<>();
        String year = CardCode.substring(6).substring(0, 4);
        String yue = CardCode.substring(10).substring(0, 2);
        String day = CardCode.substring(12).substring(0, 2);
        String sex;
        if (Integer.parseInt(CardCode.substring(16).substring(0, 1)) % 2 == 0) {
            sex = "0";
        } else {
            sex = "1";
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fyear = format.format(date).substring(0, 4);
        String fyue = format.format(date).substring(5, 7);
        String fday = format.format(date).substring(8, 10);
        int age = 0;
        if (Integer.parseInt(yue) < Integer.parseInt(fyue)
                || (Integer.parseInt(yue) == Integer.parseInt(fyue) && Integer.parseInt(day) <= Integer.parseInt(fday))) {
            age = Integer.parseInt(fyear) - Integer.parseInt(year) + 1;
        } else if (Integer.parseInt(yue) > Integer.parseInt(fyue)
                || (Integer.parseInt(yue) == Integer.parseInt(fyue) && Integer.parseInt(day) > Integer.parseInt(fday))) {
            age = Integer.parseInt(fyear) - Integer.parseInt(year);
        }
        map.put("sex", sex);
        map.put("age", String.valueOf(age));
        String birthday = yue + "-" + day;
        map.put("birthday", birthday);
        return map;
    }

    
    public static Map<String, String> getCarInfo15W(String card)
            throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String uyear = "19" + card.substring(6, 8);
        String uyue = card.substring(8, 10);
        String uday = card.substring(10, 12);
        String usex = card.substring(14, 15);
        String sex;
        if (Integer.parseInt(usex) % 2 == 0) {
            sex = "0";
        } else {
            sex = "1";
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fyear = format.format(date).substring(0, 4);
        String fyue = format.format(date).substring(5, 7);
        String fday = format.format(date).substring(8, 10);
        int age = 0;
        if (Integer.parseInt(uyue) < Integer.parseInt(fyue)
                || (Integer.parseInt(uyue) == Integer.parseInt(fyue) && Integer.parseInt(uday) <= Integer.parseInt(fday))) {
            age = Integer.parseInt(fyear) - Integer.parseInt(uyear) + 1;
        } else if (Integer.parseInt(uyue) > Integer.parseInt(fyue)
                || (Integer.parseInt(uyue) == Integer.parseInt(fyue) && Integer.parseInt(uday) > Integer.parseInt(fday))) {
            age = Integer.parseInt(fyear) - Integer.parseInt(uyear);
        }
        map.put("sex", sex);
        map.put("age", String.valueOf(age));
        String birthday = uyue + "-" + uday;
        map.put("birthday", birthday);
        return map;
    }
}
