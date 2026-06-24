package com.aihoo.util;

import java.lang.reflect.Field;

public class AllFieldIsNotNullUtils {

    public static boolean objCheckIsNull(Object object) {
        if (object == null) {
            return true;
        }

        Class clazz = object.getClass();

        Field[] fields = clazz.getDeclaredFields();

        boolean flag = true;
        for (Field field : fields) {

            field.setAccessible(true);
            Object fieldValue = null;
            String fieldName = null;
            try {

                fieldValue = field.get(object);

                fieldName = field.getName();

            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

            if (fieldValue != null && !"serialVersionUID".equals(fieldName)) {
                flag = false;
                break;
            }
        }
        return flag;
    }
}
