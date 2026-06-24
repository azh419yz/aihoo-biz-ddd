package com.aihoo.util;

import java.math.BigDecimal;

public class CalculateUtil {

    public static final int two = 2;
    public static final int three = 3;
    public static final int four = 4;
    public static final int fir = 5;

    public static int ROUND_UP = BigDecimal.ROUND_UP;
    public static int ROUND_DOWN = BigDecimal.ROUND_DOWN;
    public static int ROUND_CEILING = BigDecimal.ROUND_CEILING;
    public static int ROUND_FLOOR = BigDecimal.ROUND_FLOOR;
    public static int ROUND_HALF_UP = BigDecimal.ROUND_HALF_UP;
    public static int ROUND_HALF_DOWN = BigDecimal.ROUND_HALF_DOWN;
    public static int ROUND_HALF_EVEN = BigDecimal.ROUND_HALF_EVEN;
    public static int ROUND_UNNECESSARY = BigDecimal.ROUND_UNNECESSARY;

    public static final Object ALIQUOT_100 = "100";
    public static final Object ALIQUOT_1000 = "1000";
    public static final Object ALIQUOT_10000 = "10000";
    public static final Object ALIQUOT_100000 = "100000";
    public static final Object ALIQUOT_1000000 = "1000000";

    
    public static BigDecimal add(Object d1, Object d2, int newScale, int roundingMode) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.add(b2).setScale(newScale, roundingMode);
    }

    
    public static BigDecimal sub(Object d1, Object d2, int newScale, int roundingMode) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.subtract(b2).setScale(newScale, roundingMode);
    }

    
    public static BigDecimal multiply(Object d1, Object d2, int newScale, int roundingMode) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.multiply(b2).setScale(newScale, roundingMode);
    }

    
    public static BigDecimal multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1.trim());
        BigDecimal b2 = new BigDecimal(v2.trim());
        return b1.multiply(b2);
    }

    
    public static BigDecimal roundFloat_roundingMode(String value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        return bd;
    }

    
    public static BigDecimal div(Object d1, Object d2, int newScale, int roundingMode) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.divide(b2, newScale, roundingMode);
    }

    
    public static BigDecimal div(String num1, String num2) {
        BigDecimal bd1 = new BigDecimal(num1);
        BigDecimal bd2 = new BigDecimal(num2);
        return bd1.divide(bd2);
    }

}
