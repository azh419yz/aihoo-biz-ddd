package com.aihoo.util;

public class IdUtils {

    private static final String LENGTH = "6";

    public static String getDoctorID(Integer id) {
        String format = String.format("%0" + LENGTH + "d", id);
        return "D" + format;
    }

    public static String getHospitalID(Integer id) {
        String format = String.format("%0" + LENGTH + "d", id);
        return "H" + format;
    }

    public static String getDrugID(Integer id) {
        String format = String.format("%0" + LENGTH + "d", id);
        return "M" + format;
    }

}
