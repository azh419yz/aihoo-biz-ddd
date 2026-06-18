package com.aihoo.exception;

import com.aihoo.common.BizResultCode;

import java.io.Serial;

/**
 * 业务异常
 */
public class BizException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6879298763723247455L;

    protected BizResultCode resultCode;
    protected String message;

    public BizException() {
    }

    public BizException(String message) {
        this.message = message;
    }

    public BizException(BizResultCode resultCode) {
        this.resultCode = resultCode;
        this.message = resultCode.getMsg();
    }

    public BizException(BizResultCode resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(Throwable cause, String message) {
        super(cause);
        this.message = message;
    }

    public BizResultCode getResultCode() { return resultCode; }
    public void setResultCode(BizResultCode resultCode) { this.resultCode = resultCode; }
    @Override
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BizException that)) return false;
        if (!super.equals(o)) return false;
        return resultCode != null ? resultCode.equals(that.resultCode) : that.resultCode == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (resultCode != null ? resultCode.hashCode() : 0);
        return result;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}