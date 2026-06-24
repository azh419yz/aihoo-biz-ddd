package com.aihoo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {

    
    public static boolean isEmail(String email) {
        try {

            final String pattern1 = "[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+@[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+\\.[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+";

            final Pattern pattern = Pattern.compile(pattern1);
            final Matcher mat = pattern.matcher(email);
            return mat.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    
    private static final String REGEX_MOBILE = "(134[0-8]\\d{7})" + "|(" + "((13([0-3]|[5-9]))" + "|149"
            + "|15([0-3]|[5-9])" + "|166" + "|17(3|[5-8])" + "|18[0-9]" + "|19[8-9]" + ")" + "\\d{8}" + ")";

    
    public static boolean isMobile(String tel) {
        return Pattern.matches(REGEX_MOBILE, tel);
    }

    public static void main(String[] args) {
        String str = "25137623721";
        System.out.println(isMobile(str));
        System.out.println(str.length());

        String emil = "minchangpeng@aihoo.n";
        System.out.println(isEmail(emil));

    }

}