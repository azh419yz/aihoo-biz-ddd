package com.aihoo.redis;

public class RedisConstant {

    public final static String TEL = "Tel_";

    public final static String PATIENT_LOGIN_KEY = "Patient_Login_";

    public final static String ADMIN_LOGIN_KEY = "Admin_Login_";

    public final static String DOCKER_LOGIN_KEY = "Docker_Login_";

    public final static String STUDENT_PWD_ERROR_TIME_KEY = "YuCheng_Student_Pwd_Error_Time_";
    public final static String TEACHER_PWD_ERROR_TIME_KEY = "YuCheng_Teacher_Pwd_Error_Time_";

    public final static String STUDENT_ACCESS_TOKEN_KEY = "YuCheng_Student_Access_Token_Key_";
    public final static String TEACHER_ACCESS_TOKEN_KEY = "YuCheng_Teacher_Access_Token_Key_";

    public final static String STUDENT_COLLECTION_COURSE = "YuCheng_CollectionCourse_";

    public final static String COURSE_CLICKS = "YuCheng_Clicks_";

    public final static int TOKEN_SURVIVE_TIME = 60 * 60 * 24 * 30 * 6;

    public final static String USER_ATTENTION_COUNT = "User_Attention_Count_{0}_{1}";

    public final static String PROVINCES_KEY = "PROVINCES_KEY";

    public final static String PROVINCES_EXCEL_KEY = "PROVINCES_EXCEL_KEY";

    public final static Integer PROVINCES_KEY_TIME_OUT = 60 * 60 * 24 * 30;

    public final static Integer ADMIN_PHONE_CODE_EXPIRATION_TIME = 60 * 3;
    public final static String ADMIN_PHONE_CODE = "ADMIN_PHONE_CODE";

    public final static Integer SESSION_SURVIVE_TIME = 60 * 60 * 24 * 30;
    public final static Integer SHORTMESSAGE_MOBILE_NUM = 5;
    public final static Integer SHORTMESSAGE_DELAY_TIME = 60 * 30;
    public final static Integer SHORTMESSAGE_DAY_TIME = 60 * 60 * 24;
    public final static Integer PWD_ERROR_EXPIRE_TIME = 60 * 60;
    public final static Integer TEL_TIME = 60 * 10;
    public final static Integer TEL_TIME_NUM = 3;
}
