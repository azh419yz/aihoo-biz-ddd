package com.aihoo.common;

import com.aihoo.common.utils.HttpCodeConstants;

public class BaseController {

    
    public static JsonResult ok() {
        return ok(HttpCodeConstants.SC_OK, null);
    }

    
    public static JsonResult ok(String message) {
        return ok(HttpCodeConstants.SC_OK, message);
    }

    
    public static JsonResult ok(int code, String message) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("code", code);
        jsonResult.put("msg", message);
        return jsonResult;
    }

    
    public static JsonResult error() {
        return ok(HttpCodeConstants.SC_SERVER_ERROR, null);
    }

    
    public static JsonResult error(String message) {
        return ok(HttpCodeConstants.SC_SERVER_ERROR, message);
    }

    
    public static JsonResult error(int code, String message) {
        return ok(code, message);
    }

    
    protected static JsonResult forbidden() {
        return ok(HttpCodeConstants.SC_FORBIDDEN, null);
    }

    
    protected static JsonResult forbidden(String message) {
        return ok(HttpCodeConstants.SC_FORBIDDEN, message);
    }

    
    protected static JsonResult notFound() {
        return ok(HttpCodeConstants.SC_NOT_FOUND, null);
    }

    
    protected static JsonResult notFound(String message) {
        return ok(HttpCodeConstants.SC_NOT_FOUND, message);
    }

}
