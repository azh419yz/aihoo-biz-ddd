package com.aihoo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 订单号工具类
 * <p>
 * Created by fjy
 */
public class OrderNoUtil {

    /**
     * 获取订单号
     */
    public static String getOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result;
    }

}