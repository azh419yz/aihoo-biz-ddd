package com.aihoo.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHandler {
    
    public static final List<String> IMG_CONTENT_TYPE = Arrays.asList(".jpg", ".png", ".jpeg", ".gif", ".JPG", ".PNG", ".JPEG", ".GIF");

    
    public static String reverseByRecursive(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (str.length() == 1) {
            return str;
        } else {

            return reverseByRecursive(str.substring(1)) + str.charAt(0);
        }
    }

    
    public static String[] split(String str, String separatorChars) {
        if (equals(str, "null"))
            return null;
        return StringUtils.split(str, separatorChars);
    }

    public static String trim(String value) {
        return value.trim();
    }

    
    public static boolean isNotBlank(String str) {
        if (equals(str, "null"))
            str = null;
        return StringUtils.isNotBlank(str);
    }

    public static boolean isEmpty(String str) {
        if (equals(str, "null"))
            str = null;
        return StringUtils.isEmpty(str);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return StringUtils.equalsIgnoreCase(str1, str2);
    }

    
    public static boolean startsWith(String str, String prefix) {
        return StringUtils.startsWith(str, prefix);
    }

    
    public static boolean endsWith(String str, String suffix) {
        return StringUtils.endsWith(str, suffix);
    }

    
    public static boolean equals(String str1, String str2) {
        return StringUtils.equals(str1, str2);
    }

    
    public static String left(String str, int len) {
        return StringUtils.left(str, len);
    }

    
    public static String leftPad(Object str, int len, String padChar) {
        return StringUtils.leftPad(str.toString(), len, padChar);
    }

    
    public static String rightPad(String str, int len, String padChar) {
        return StringUtils.rightPad(str, len, padChar);
    }

    
    public static String left(Object str, int len) {
        return StringUtils.left(str.toString(), len);
    }

    
    public static String right(Object str, int len) {
        return StringUtils.right(str.toString(), len);
    }

    public static String substrPad(Object str, int leftSize, int rightSize, String padChar) {
        if (str == null)
            return null;

        StringBuffer buffer = new StringBuffer();
        String string = str.toString();
        String leftStr = StringUtils.left(string, leftSize);
        buffer.append(leftStr);

        buffer.append(padChar);

        if (string.length() > (leftSize + rightSize)) {
            String rightStr = StringUtils.right(string, rightSize);
            buffer.append(rightStr);
        }
        return buffer.toString();
    }

    
    public static String getSubMobile(String phone) {
        if (StringHandler.isEmpty(phone))
            return "";
        return phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
    }

    
    public static boolean isMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    
    public static String frontCompWithZero(int sourceDate, int formatLength) {
        String newString = String.format("%0" + formatLength + "d", sourceDate);
        return newString;
    }

    
    public static String rightSubStr(String str, String separator) {
        if (str == null) {
            return null;
        }
        int idx = str.lastIndexOf(separator);
        if (idx == -1 || idx == str.length() - 1) {
            return "";
        }
        return str.substring(idx + 1);
    }
}
