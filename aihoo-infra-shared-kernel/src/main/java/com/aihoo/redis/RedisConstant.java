package com.aihoo.redis;

public class RedisConstant {

    public final static String TEL = "Tel_";//缓存手机发验证码次数 Tel_{手机号}

    public final static String PATIENT_LOGIN_KEY = "Patient_Login_";// 登录信息 User_Login_{用户id}

    public final static String ADMIN_LOGIN_KEY = "Admin_Login_";// 后台登录信息 Admin_Login_{token}

    public final static String DOCKER_LOGIN_KEY = "Docker_Login_";// 登录信息 User_Login_{用户id}

    public final static String STUDENT_PWD_ERROR_TIME_KEY = "YuCheng_Student_Pwd_Error_Time_";//密码错误次数 Pwd_Error_Time_{用户id}
    public final static String TEACHER_PWD_ERROR_TIME_KEY = "YuCheng_Teacher_Pwd_Error_Time_";//密码错误次数 Pwd_Error_Time_{用户id}

    public final static String STUDENT_ACCESS_TOKEN_KEY = "YuCheng_Student_Access_Token_Key_";//保存用户accessToken 用于下次清缓存 User_Access_Token_{用户id}
    public final static String TEACHER_ACCESS_TOKEN_KEY = "YuCheng_Teacher_Access_Token_Key_";//保存用户accessToken 用于下次清缓存 User_Access_Token_{用户id}

    public final static String STUDENT_COLLECTION_COURSE = "YuCheng_CollectionCourse_";//学生收藏课程

    public final static String COURSE_CLICKS = "YuCheng_Clicks_";//课程点击量

    public final static int TOKEN_SURVIVE_TIME = 60 * 60 * 24 * 30 * 6;//登录过期时间 6个月

    public final static String USER_ATTENTION_COUNT = "User_Attention_Count_{0}_{1}";//用户关注

    public final static String PROVINCES_KEY = "PROVINCES_KEY"; //省市区联动数据缓存key

    public final static String PROVINCES_EXCEL_KEY = "PROVINCES_EXCEL_KEY"; //EXCEL 地址校验 省市区联动数据缓存key

    public final static Integer PROVINCES_KEY_TIME_OUT = 60 * 60 * 24 * 30; //省市区联动数据缓存key
    //*****************************************************************************//

    public final static Integer ADMIN_PHONE_CODE_EXPIRATION_TIME = 60 * 3;//手机验证码存活时间三分钟
    public final static String ADMIN_PHONE_CODE = "ADMIN_PHONE_CODE";//手机验证码key

    public final static Integer SESSION_SURVIVE_TIME = 60 * 60 * 24 * 30;//token的生命周期 30天
    public final static Integer SHORTMESSAGE_MOBILE_NUM = 5;// 一个手机号24小时之内只能发5条
    public final static Integer SHORTMESSAGE_DELAY_TIME = 60 * 30;// 延时30分钟
    public final static Integer SHORTMESSAGE_DAY_TIME = 60 * 60 * 24;// 现在一天只能发5条短信
    public final static Integer PWD_ERROR_EXPIRE_TIME = 60 * 60;//  3600s密码错误次数5次
    public final static Integer TEL_TIME = 60 * 10;//同一个手机，不管任何操作，10分钟只能只能发3条,这是短信通道商的规定
    public final static Integer TEL_TIME_NUM = 3;//同一个手机，不管任何操作，10分钟只能只能发3条,这是短信通道商的规定
}
