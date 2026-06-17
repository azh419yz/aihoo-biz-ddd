package com.aihoo.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;

public class CodeUtils {

    public static String idCardMask(String idCardNum) {
        if (StringUtils.isEmpty(idCardNum)) return "";
        StringBuilder stringBuilder = new StringBuilder(idCardNum);
        return stringBuilder.replace(6, 14, "********").toString();
    }

    public static String phoneMask(String phone) {
        if (StringUtils.isEmpty(phone)) return "";
        StringBuilder stringBuilder = new StringBuilder(phone);
        return stringBuilder.replace(3, 7, "****").toString();
    }

    public static String stringSixMask(String string) {
        if (StringUtils.isEmpty(string)) return "";
        int length = string.length();
        if (length > 6) {
            StringBuilder stringBuilder = new StringBuilder(string);
            String join = String.join("", Collections.nCopies(length - 6, "*"));
            return stringBuilder.replace(3, length - 3, join).toString();
        } else {
            return string;
        }
    }
}