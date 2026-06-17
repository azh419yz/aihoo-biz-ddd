package com.aihoo.common.utils;


/**
 * Created by john on 2016/11/26.
 */
public class HttpCodeConstants {

    /**
     * 返回状态 码
     */
    public static final int SC_OK = 200; //正常请求
    public static final int SC_BAD_REQUEST = 400; //Bad Request 请求出现语法错误
    public static final int SC_FORBIDDEN = 401; //无权限查看
    public static final int SC_NOT_FOUND = 404; //不存在
    public static final int SC_CONFLICT = 403;    //存在冲突，请求无法完成。重新提交新的请求
    public static final int SC_GONE = 405;//请求方式不支持
    public static final int SC_NO_PARAM = 406;//参数不存在
    public static final int SC_SERVER_ERROR = 500;//服务器出现错误
    public static final int sc_SERVER_TIME_OUT = 504;//服务超时
}
