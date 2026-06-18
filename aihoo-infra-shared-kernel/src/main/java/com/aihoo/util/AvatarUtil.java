package com.aihoo.util;

/**
 * 患者头像路径工具（迁自 doctor/patient/visit 域重复的 getAvatarPath）。
 * 性别 1 → M，其他 → W；年龄区间 0-6/7-20/21-60/60+ → 1/2/3/4。
 */
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
            // ignore parse error, default to 0
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