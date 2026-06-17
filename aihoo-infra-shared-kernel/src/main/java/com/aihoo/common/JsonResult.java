package com.aihoo.common;

import jakarta.servlet.http.HttpServletResponse;

import java.io.Serial;
import java.util.HashMap;

/**
 * 返回结果对象
 * Created by wangfan on 2017-6-10 上午10:10
 */
public class JsonResult extends HashMap<String, Object> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 返回成功
     */
    public static JsonResult ok(HttpServletResponse response) {
        return ok("操作成功");
    }


    /**
     * 返回成功
     */
    public static JsonResult ok(String message) {
        return ok(200, message);
    }

    /**
     * 返回成功
     */
    public static JsonResult ok() {
        return ok(200, "请求成功");
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
     * 返回成功
     */
    public static JsonResult LogisticsOk(int code, String message, boolean success) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("code", code);
        jsonResult.put("msg", message);
        jsonResult.put("success", success);
        return jsonResult;
    }

    public static JsonResult ok(int code, String message, String count) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("code", code);
        jsonResult.put("count", count);
        jsonResult.put("msg", message);
        return jsonResult;
    }

    /**
     * 返回成功
     */
    public static JsonResult ok(Object obj) {
        return ok(200, "短信验证码已经发送");
    }

    /**
     * 登录成功
     */
    public static JsonResult loginSuccess() {
        return ok(200, "登录成功");
    }

    /**
     * 返回失败
     */
    public static JsonResult error() {
        return error("操作失败");
    }

    /**
     * 返回失败
     */
    public static JsonResult error(String message) {
        return error(500, message);
    }

    /**
     * 返回失败
     */
    public static JsonResult LogisticsError(int code, String message, boolean success) {
        return LogisticsOk(code, message, success);
    }

    /**
     * 返回失败
     */
    public static JsonResult error(int code, String message) {
        return ok(code, message);
    }

    /**
     * 验证码有误
     */
    public static JsonResult captchaError() {
        return error(500, "验证码有误");
    }

    /**
     * 返回登录失败
     */
    public static JsonResult loginFail() {
        return error(500, "登录失败");
    }

    /**
     * 返回登录失败
     */
    public static JsonResult loginMessage() {
        return error(250, "此用户未绑定手机号，请绑定手机号");
    }

    /**
     * 设置code
     */
    public JsonResult setCode(int code) {
        super.put("code", code);
        return this;
    }

    /**
     * 设置message
     */
    public JsonResult setMessage(String message) {
        super.put("msg", message);
        return this;
    }

    /**
     * 放入object
     */
    @Override
    public JsonResult put(String key, Object object) {
        super.put(key, object);
        return this;
    }

    /**
     * 放入object
     */
    public JsonResult putData(Object object) {
        this.put("data", object);
        return this;
    }
}