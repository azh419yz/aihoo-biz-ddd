package com.aihoo.util;

public final class AvatarUtil {

    private AvatarUtil() {
    }

    public static String getAvatarPath(String sex, String ageStr) {
        String genderPrefix = "1".equals(sex) ? "M" : "W";
        int age = 0;
        try {
            if (StringUtil.isNotBlank(ageStr)) {
                String numericAge = ageStr.replaceAll("\\D+", "");
                if (StringUtil.isNotBlank(numericAge)) {
                    age = Integer.parseInt(numericAge);
                }
            }
        } catch (Exception e) {

        }

        String ageSuffix;
        if (age <= 6) {
            ageSuffix = "1";
        } else if (age <= 20) {
            ageSuffix = "2";
        } else if (age <= 60) {
            ageSuffix = "3";
        } else {
            ageSuffix = "4";
        }

        return "patient_aihoo/avatar/" + genderPrefix + ageSuffix + ".jpg";
    }
}