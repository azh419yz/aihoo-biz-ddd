package com.aihoo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharUtil {

    private static final String chars = "QAZWSXEDCRFVTGBYHNUJMIKOLP";
    private static final String nums = "0123456789QAZWSXEDCRFVTGBYHNUJMIKOLPqazwsxedcrfvtgbyhnujmikolp";

    
    public static String trimString(String str) {
        if (StringHandler.isNotBlank(str)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }

    
    public static String getRandomChar(String prefix, int length) {
        StringBuffer buffer = new StringBuffer(prefix);
        for (int i = 0; i < length; i++) {
            char c = chars.charAt((int) (Math.random() * 26));
            buffer.append(c);
        }
        return buffer.toString();
    }

    
    public static String getRandomNum(String prefix, int length) {
        StringBuffer buffer = new StringBuffer(prefix);
        for (int i = 0; i < length; i++) {
            char c = nums.charAt((int) (Math.random() * 62));
            buffer.append(c);
        }
        return buffer.toString();
    }

    
    public static boolean isMobile(String mobile) {
        String regExp = "^[1][0-9]{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(mobile);
        return m.find();
    }

    
    public static boolean isIdCard(String idCard) {
        String regExp = "^[0-9]{17}([0-9]{1}|X|x)$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(idCard);
        return m.find();
    }

    
    public static boolean isCardNo(String cardNo) {
        String regExp = "^[0-9]{16,19}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(cardNo);
        return m.find();
    }

    public static boolean isYuan(String amt) {
        String regExp = "^([0-9]{1,}\\.[0-9]{2}|[0-9]{1,})$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(amt);
        return m.find();
    }

    public static boolean isYuan_2(String amt) {
        String regExp = "^([0-9]{1,}\\.[0-9]{1,2}|[0-9]{1,})$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(amt);
        return m.find();
    }

    public static boolean isMonth(String month) {
        String regExp = "^[0-9]{1,2}:[0-9]{1,2}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(month);
        return m.find();
    }

    public static void main(String[] args) {

        String month = "11.2";
        System.out.println(isYuan_2(month));

    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }

    public static boolean isChineseByName(String str) {
        if (str == null) {
            return false;
        }

        String reg = "\\p{InCJK Unified Ideographs}&&\\P{Cn}";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(str.trim()).find();
    }
}
