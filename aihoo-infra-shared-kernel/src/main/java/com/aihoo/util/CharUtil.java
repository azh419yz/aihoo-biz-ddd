package com.aihoo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CharUtil {

    private static final String chars = "QAZWSXEDCRFVTGBYHNUJMIKOLP";
    private static final String nums = "0123456789QAZWSXEDCRFVTGBYHNUJMIKOLPqazwsxedcrfvtgbyhnujmikolp";


    /**
     * 去除字符串中的空格、回车、换行符、制表符
     *
     * @param str
     * @return
     */
    public static String trimString(String str) {
        if (StringHandler.isNotBlank(str)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }

    /**
     * 获取A-Z随机数
     *
     * @param prefix : 前缀字符
     * @param length : 获取位数
     * @return
     */
    public static String getRandomChar(String prefix, int length) {
        StringBuffer buffer = new StringBuffer(prefix);
        for (int i = 0; i < length; i++) {
            char c = chars.charAt((int) (Math.random() * 26));
            buffer.append(c);
        }
        return buffer.toString();
    }

    /**
     * 获取A-Z随机数
     *
     * @param prefix : 前缀字符
     * @param length : 获取位数
     * @return
     */
    public static String getRandomNum(String prefix, int length) {
        StringBuffer buffer = new StringBuffer(prefix);
        for (int i = 0; i < length; i++) {
            char c = nums.charAt((int) (Math.random() * 62));
            buffer.append(c);
        }
        return buffer.toString();
    }

    /**
     * 验证手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        String regExp = "^[1][0-9]{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(mobile);
        return m.find();
    }

    /**
     * 验证身份证
     *
     * @param idCard
     * @return
     */
    public static boolean isIdCard(String idCard) {
        String regExp = "^[0-9]{17}([0-9]{1}|X|x)$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(idCard);
        return m.find();
    }

    /**
     * 验证银行卡号
     *
     * @param cardNo
     * @return
     */
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

//		String mobile = "15221477930";
//		System.out.println(isMobile(mobile));
//		String idCard = "430281199011284310";
//		System.out.println(isIdCard(idCard));
//		String cardNo = "6212261704006862086";
//		System.out.println(isCardNo(cardNo));

//		String str = "你好";
//		System.out.println("===========> 测试字符串：" + str);
//        System.out.println("正则判断结果：" + isChineseByREG(str) + " -- " + isChineseByName(str));
//        System.out.println("Unicode判断结果 ：" + isChinese(str));
//        System.out.println("详细判断列表：");
//        char[] ch = str.toCharArray();
//        for (int i = 0; i < ch.length; i++) {
//            char c = ch[i];
//            System.out.println(c + " --> " + (isChinese(c) ? "是" : "否"));
//        }
    }

    // 根据Unicode编码完美的判断中文汉字和符号
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

    // 完整的判断中文汉字和符号
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

    // 只能判断部分CJK字符（CJK统一汉字）
    public static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }

    // 只能判断部分CJK字符（CJK统一汉字）
    public static boolean isChineseByName(String str) {
        if (str == null) {
            return false;
        }
        // 大小写不同：\\p 表示包含，\\P 表示不包含
        // \\p{Cn} 的意思为 Unicode 中未被定义字符的编码，\\P{Cn} 就表示 Unicode中已经被定义字符的编码
        String reg = "\\p{InCJK Unified Ideographs}&&\\P{Cn}";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(str.trim()).find();
    }
}
