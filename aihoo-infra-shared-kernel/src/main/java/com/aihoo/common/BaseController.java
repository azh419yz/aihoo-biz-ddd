package com.aihoo.common;

import com.aihoo.common.utils.HttpCodeConstants;

/**
 * Controller基类
 * Created by wangfan on 2017-6-10 上午10:10
 */
public class BaseController {

    /**
     * 返回成功
     */
    public static JsonResult ok() {
        return ok(HttpCodeConstants.SC_OK, null);
    }

    /**
     * 返回成功
     */
    public static JsonResult ok(String message) {
        return ok(HttpCodeConstants.SC_OK, message);
    }

    /**
     * 返回成功
     */
    public static JsonResult ok(int code, String message) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("code", code);
        jsonResult.put("msg", message);
        return jsonResult;
    }

    /**
     * 返回失败
     */
    public static JsonResult error() {
        return ok(HttpCodeConstants.SC_SERVER_ERROR, null);
    }


    /**
     * 返回失败
     */
    public static JsonResult error(String message) {
        return ok(HttpCodeConstants.SC_SERVER_ERROR, message);
    }

    /**
     * 返回失败
     */
    public static JsonResult error(int code, String message) {
        return ok(code, message);
    }

    /**
     * 无权限查看
     *
     * @return
     */
    protected static JsonResult forbidden() {
        return ok(HttpCodeConstants.SC_FORBIDDEN, null);
    }

    /**
     * 无权限查看
     *
     * @return
     */
    protected static JsonResult forbidden(String message) {
        return ok(HttpCodeConstants.SC_FORBIDDEN, message);
    }

    /**
     * 不存在
     *
     * @return
     */
    protected static JsonResult notFound() {
        return ok(HttpCodeConstants.SC_NOT_FOUND, null);
    }

    /**
     * 不存在
     *
     * @param message
     * @return
     */
    protected static JsonResult notFound(String message) {
        return ok(HttpCodeConstants.SC_NOT_FOUND, message);
    }

}
