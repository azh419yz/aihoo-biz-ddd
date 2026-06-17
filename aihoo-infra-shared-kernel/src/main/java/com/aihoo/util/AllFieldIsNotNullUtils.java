package com.aihoo.util;

import java.lang.reflect.Field;

/**
 * @Classname AllFieldIsNotNullUtils
 * @Description hf
 * @Date 2020/11/12 18:57
 * @Created by ad
 */
public class AllFieldIsNotNullUtils {

    public static boolean objCheckIsNull(Object object) {
        if (object == null) {
            return true;
        }
        // 得到类对象
        Class clazz = object.getClass();
        // 得到所有属性
        Field[] fields = clazz.getDeclaredFields();
        //定义返回结果，默认为true
        boolean flag = true;
        for (Field field : fields) {
            //设置权限（很重要，否则获取不到private的属性）
            field.setAccessible(true);
            Object fieldValue = null;
            String fieldName = null;
            try {
                //得到属性值
                fieldValue = field.get(object);
                //得到属性名
                fieldName = field.getName();

            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            //只要有一个属性值不为null 就返回false 表示对象不为null
            if (fieldValue != null && !"serialVersionUID".equals(fieldName)) {
                flag = false;
                break;
            }
        }
        return flag;
    }
}
