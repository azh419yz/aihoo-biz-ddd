package com.aihoo.util;

import java.math.BigDecimal;

/**
 * 金额、费率计算工具类(加、减、乘、除)
 * 1、ROUND_UP
 * 　　舍入远离零的舍入模式。
 * 　　在丢弃非零部分之前始终增加数字(始终对非零舍弃部分前面的数字加1)。
 * 　　注意,此舍入模式始终不会减少计算值的大小。
 * 2、ROUND_DOWN
 * 　　接近零的舍入模式。
 * 　　在丢弃某部分之前始终不增加数字(从不对舍弃部分前面的数字加1,即截短)。
 * 　　注意,此舍入模式始终不会增加计算值的大小。
 * 3、ROUND_CEILING
 * 　　接近正无穷大的舍入模式。
 * 　　如果 BigDecimal 为正,则舍入行为与 ROUND_UP 相同;
 * 　　如果为负,则舍入行为与 ROUND_DOWN 相同。
 * 　　注意,此舍入模式始终不会减少计算值。
 * 4、ROUND_FLOOR
 * 　　接近负无穷大的舍入模式。
 * 　　如果 BigDecimal 为正,则舍入行为与 ROUND_DOWN 相同;
 * 　　如果为负,则舍入行为与 ROUND_UP 相同。
 * 　　注意,此舍入模式始终不会增加计算值。
 * 5、ROUND_HALF_UP
 * 　　向"最接近的"数字舍入,如果与两个相邻数字的距离相等,则为向上舍入的舍入模式。
 * 　　如果舍弃部分 >= 0.5,则舍入行为与 ROUND_UP 相同;否则舍入行为与 ROUND_DOWN 相同。
 * 　　注意,这是我们大多数人在小学时就学过的舍入模式(四舍五入)。
 * 6、ROUND_HALF_DOWN
 * 　　向"最接近的"数字舍入,如果与两个相邻数字的距离相等,则为上舍入的舍入模式。
 * 　　如果舍弃部分 > 0.5,则舍入行为与 ROUND_UP 相同;否则舍入行为与 ROUND_DOWN 相同(五舍六入)。
 * 7、ROUND_HALF_EVEN
 * 　　向"最接近的"数字舍入,如果与两个相邻数字的距离相等,则向相邻的偶数舍入。
 * 　　如果舍弃部分左边的数字为奇数,则舍入行为与 ROUND_HALF_UP 相同;
 * 　　如果为偶数,则舍入行为与 ROUND_HALF_DOWN 相同。
 * 　　注意,在重复进行一系列计算时,此舍入模式可以将累加错误减到最小。
 * 　　此舍入模式也称为"银行家舍入法",主要在美国使用。四舍六入,五分两种情况。
 * 　　如果前一位为奇数,则入位,否则舍去。
 * 　　以下例子为保留小数点1位,那么这种舍入方式下的结果。
 * 　　1.15>1.2 1.25>1.2
 * 8、ROUND_UNNECESSARY
 * 断言请求的操作具有精确的结果，因此不需要舍入。
 * 如果对获得精确结果的操作指定此舍入模式，则抛出ArithmeticException。
 */
public class CalculateUtil {

    public static final int two = 2;
    public static final int three = 3;
    public static final int four = 4;
    public static final int fir = 5;

    //舍入模式
    public static int ROUND_UP = BigDecimal.ROUND_UP;
    public static int ROUND_DOWN = BigDecimal.ROUND_DOWN;
    public static int ROUND_CEILING = BigDecimal.ROUND_CEILING;
    public static int ROUND_FLOOR = BigDecimal.ROUND_FLOOR;
    public static int ROUND_HALF_UP = BigDecimal.ROUND_HALF_UP;
    public static int ROUND_HALF_DOWN = BigDecimal.ROUND_HALF_DOWN;
    public static int ROUND_HALF_EVEN = BigDecimal.ROUND_HALF_EVEN;
    public static int ROUND_UNNECESSARY = BigDecimal.ROUND_UNNECESSARY;

    //整除数
    public static final Object ALIQUOT_100 = "100";
    public static final Object ALIQUOT_1000 = "1000";
    public static final Object ALIQUOT_10000 = "10000";
    public static final Object ALIQUOT_100000 = "100000";
    public static final Object ALIQUOT_1000000 = "1000000";

    /**
     * 进行加法运算
     *
     * @param d1           : Object
     * @param d2           : Object
     * @param newScale     : 保留小数点
     * @param roundingMode : 舍入模式
     * @return BigDecimal
     */
    public static BigDecimal add(Object d1, Object d2, int newScale, int roundingMode) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.add(b2).setScale(newScale, roundingMode);
    }

    /**
     * 进行减法运算
     *
     * @param d1           : Object(减数)
     * @param d2           : Object(被减数)
     * @param newScale     : 保留小数点
     * @param roundingMode : 舍入模式
     * @return BigDecimal
     */
    public static BigDecimal sub(Object d1, Object d2, int newScale, int roundingMode) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.subtract(b2).setScale(newScale, roundingMode);
    }

    /**
     * 进行乘法运算
     *
     * @param d1           : Object
     * @param d2           : Object
     * @param newScale     : 保留小数点
     * @param roundingMode : 舍入模式
     * @return BigDecimal
     */
    public static BigDecimal multiply(Object d1, Object d2, int newScale, int roundingMode) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.multiply(b2).setScale(newScale, roundingMode);
    }


    /**
     * 提供精确的乘法运算
     *
     * @param v1
     * @param v2
     * @return 两个参数的数学积，以字符串格式返回
     */
    public static BigDecimal multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1.trim());
        BigDecimal b2 = new BigDecimal(v2.trim());
        return b1.multiply(b2);
    }


    /**
     * 指定小数点位数、指定四舍五入值,不保留小数点最后一位0
     *
     * @param value        : 数值 String
     * @param scale        : 设置小数点位数
     * @param roundingMode : 表示四舍五入，可以选择其他舍值方式，例如去尾，等等
     * @return float
     */
    public static BigDecimal roundFloat_roundingMode(String value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        return bd;
    }

    /**
     * 进行除法运算
     *
     * @param d1           : Object(除数)
     * @param d2           : Object(被除数)
     * @param newScale     : 保留小数点
     * @param roundingMode : 舍入模式
     * @return BigDecimal
     */
    public static BigDecimal div(Object d1, Object d2, int newScale, int roundingMode) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.divide(b2, newScale, roundingMode);
    }

    /**
     * 俩个数相除取得倍数、不要余数
     *
     * @return int
     */
    public static BigDecimal div(String num1, String num2) {
        BigDecimal bd1 = new BigDecimal(num1);
        BigDecimal bd2 = new BigDecimal(num2);
        return bd1.divide(bd2);
    }

}
