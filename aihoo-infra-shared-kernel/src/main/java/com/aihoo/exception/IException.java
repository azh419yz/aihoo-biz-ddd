package com.aihoo.exception;

import java.io.Serial;

/**
 * 自定义异常基类
 */
public abstract class IException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1582874427218948396L;
    private Integer code;

    public IException() {
    }

    public IException(String message) {
        super(message);
    }

    public IException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IException iException)) return false;
        if (!super.equals(o)) return false;
        return code != null ? code.equals(iException.code) : iException.code == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }
}