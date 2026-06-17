package com.aihoo.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作
 *
 * @author damonzhl
 */
public class StringHandler {
    /**
     * 图片
     */
    public static final List<String> IMG_CONTENT_TYPE = Arrays.asList(".jpg", ".png", ".jpeg", ".gif", ".JPG", ".PNG", ".JPEG", ".GIF");

    /**
     * 反转
     *
     * @param str
     * @return
     */
    public static String reverseByRecursive(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (str.length() == 1) {
            return str;
        } else {
            //从下标为1开始截取字符串，在返回下标为0的字符
            return reverseByRecursive(str.substring(1)) + str.charAt(0);
        }
    }

    /**
     * 字符串分割
     *
     * @param str
     * @return
     */
    public static String[] split(String str, String separatorChars) {
        if (equals(str, "null"))
            return null;
        return StringUtils.split(str, separatorChars);
    }

    public static String trim(String value) {
        return value.trim();
    }

    /**
     * 判断字符串非空
     *
     * @param str
     * @return
     */
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

    /**
     * 匹配前缀首位字符串
     *
     * @param str    ：字符串
     * @param prefix ：匹配
     * @return
     */
    public static boolean startsWith(String str, String prefix) {
        return StringUtils.startsWith(str, prefix);
    }

    /**
     * 匹配末尾首位字符串
     *
     * @param str    ：字符串
     * @param prefix ：匹配
     * @return
     */
    public static boolean endsWith(String str, String suffix) {
        return StringUtils.endsWith(str, suffix);
    }

    /**
     * 字符串内存比对
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equals(String str1, String str2) {
        return StringUtils.equals(str1, str2);
    }

    /**
     * 指定字符串最大长度,当大于len时,多余长度数值去除
     *
     * @param str : 值
     * @param len ：长度
     * @return String
     */
    public static String left(String str, int len) {
        return StringUtils.left(str, len);
    }

    /**
     * 长度不足时,指定padChar参数值左填充
     *
     * @param str     ：字符串
     * @param len     ：长度
     * @param padChar ：填充值
     * @return String
     */
    public static String leftPad(Object str, int len, String padChar) {
        return StringUtils.leftPad(str.toString(), len, padChar);
    }

    /**
     * 长度不足时,指定padChar参数值右填充
     *
     * @param str     ：字符串
     * @param len     ：长度
     * @param padChar ：填充值
     * @return String
     */
    public static String rightPad(String str, int len, String padChar) {
        return StringUtils.rightPad(str, len, padChar);
    }

    /**
     * 字符串截取-左截取
     *
     * @param str ：字符串
     * @param len ：长度
     * @return String
     */
    public static String left(Object str, int len) {
        return StringUtils.left(str.toString(), len);
    }

    /**
     * 字符串截取-右截取
     *
     * @param str ：字符串
     * @param len ：长度
     * @return String
     */
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

    /**
     * 手机号中间四位替换成*
     *
     * @param phone
     * @return
     */
    public static String getSubMobile(String phone) {
        if (StringHandler.isEmpty(phone))
            return "";
        return phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
    }


    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     * @author ：shijing
     * 2016年12月5日下午4:34:46
     */
    public static boolean isMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }


    /**
     * 前面填充0
     *
     * @param sourceDate
     * @param formatLength
     * @return
     */
    public static String frontCompWithZero(int sourceDate, int formatLength) {
        String newString = String.format("%0" + formatLength + "d", sourceDate);
        return newString;
    }
}
