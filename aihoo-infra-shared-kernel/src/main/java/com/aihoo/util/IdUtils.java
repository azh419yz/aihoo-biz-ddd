package com.aihoo.util;

/**
 * @Classname IdUtils
 * @Description hf
 * @Date 2020/11/22 14:24
 * @Created by ad
 */
public class IdUtils {

    private static final String LENGTH = "6";

    // 医生工号规则
    public static String getDoctorID(Integer id) {
        String format = String.format("%0" + LENGTH + "d", id);
        return "D" + format;
    }

    // 医院id 生成
    public static String getHospitalID(Integer id) {
        String format = String.format("%0" + LENGTH + "d", id);
        return "H" + format;
    }

    // 药品id
    public static String getDrugID(Integer id) {
        String format = String.format("%0" + LENGTH + "d", id);
        return "M" + format;
    }

}
