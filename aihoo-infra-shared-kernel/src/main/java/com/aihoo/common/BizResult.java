package com.aihoo.common;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

@Schema(name = "BizResult", description = "通用返回结果")
public class BizResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "状态码")
    private int code;

    @Schema(description = "状态信息")
    private String msg;

    @Schema(description = "响应数据", anyOf = Object.class)
    private T data;

    @Schema(description = "时间戳")
    private long timestamp;

    public BizResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public BizResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public static <T> BizResult<T> success(T data) {
        return new BizResult<>(BizResultCode.SUCCESS.getCode(), BizResultCode.SUCCESS.getMsg(), data);
    }

    public static <T> BizResult<T> success() {
        return success(null);
    }

    public static <T> BizResult<T> success(String msg) {
        return new BizResult<>(BizResultCode.SUCCESS.getCode(), msg, null);
    }

    public static <T> BizResult<T> success(String msg, T data) {
        return new BizResult<>(BizResultCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> BizResult<T> fail(BizResultCode BizResultCode) {
        return new BizResult<>(BizResultCode.getCode(), BizResultCode.getMsg(), null);
    }

    public static <T> BizResult<T> fail(BizResultCode BizResultCode, String msg) {
        return new BizResult<>(BizResultCode.getCode(), msg, null);
    }

    public static <T> BizResult<T> fail(int code, String msg) {
        return new BizResult<>(code, msg, null);
    }

    public boolean isSuccess() {
        return this.code == BizResultCode.SUCCESS.getCode();
    }
}