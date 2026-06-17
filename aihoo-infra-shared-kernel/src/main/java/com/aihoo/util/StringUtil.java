package com.aihoo.util;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {

    public static String getDateTimes(String str) {
        return getStr(str).replaceAll("-", "");
    }

    public static boolean isBlank(Object obj) {
        return obj == null || isBlank(String.valueOf(obj));
    }

    public static boolean isNotBlank(Object obj) {
        return !isBlank(obj);
    }

    public static String getStr(Object obj) {
        return isBlank(obj) ? "" : String.valueOf(obj);
    }

    public static String ifBlank(Object obj, String str) {
        return isBlank(obj) ? str : String.valueOf(obj);
    }

    public static Integer getInteger(Object obj) {
        String str = String.valueOf(obj);
        return isNumeric(str) ? Integer.parseInt(str) : 0;
    }

    public static String textOverflow(Object obj, int maxLen) {
        return textOverflow(obj, maxLen, null);
    }

    public static String textOverflow(Object obj, int maxLen, String ifEmpty) {
        String str = getStr(obj);
        if (isBlank(str) && isNotBlank(ifEmpty)) str = ifEmpty;
        if (str.length() > maxLen) {
            str = String.join("", Arrays.copyOfRange(str.split(""), 0, maxLen - 1)) + "…";
        }
        return str;
    }

    public static boolean isMobile(String mobile) {
        if (isBlank(mobile)) return false;
        String regExp = "^1[0-9]{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(mobile);
        return m.find();
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[+\\-]?\\d+(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static boolean isBlank(String str) {
        return str == null || str.isEmpty() || str.replaceAll(" ", "").isEmpty();
    }

    public static boolean isBlank(String... strs) {
        for (String str : strs) {
            if (isBlank(str)) return true;
        }
        return false;
    }

    public static boolean isNotBlank(String... strs) {
        return !isBlank(strs);
    }

    public static boolean contains(String str, String key) {
        return str != null && str.contains(key);
    }

    public static boolean contains(String... strs) {
        for (int i = 0; i < strs.length - 1; i++) {
            if (contains(strs[i], strs[strs.length - 1])) return true;
        }
        return false;
    }

    public static boolean contains(String[] strs, String key) {
        for (String str : strs) {
            if (contains(str, key)) return true;
        }
        return false;
    }

    public static String upperHeadChar(String in) {
        String head = in.substring(0, 1);
        return head.toUpperCase() + in.substring(1);
    }

    public static String getIntegerStr(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public static boolean isNullOrEmpty(Object obj) {
        return obj == null || "".equals(obj);
    }
}