package com.aihoo.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnValues<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer code;
    private String msg;
    private T data;

    public static <T> ReturnValues<T> ok() {
        return new ReturnValues<>(0, "ok", null);
    }
    public static <T> ReturnValues<T> ok(T data) {
        return new ReturnValues<>(0, "ok", data);
    }
    public static <T> ReturnValues<T> fail(String msg) {
        return new ReturnValues<>(1, msg, null);
    }
}
