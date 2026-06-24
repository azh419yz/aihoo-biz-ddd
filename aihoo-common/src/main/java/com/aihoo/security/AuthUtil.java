package com.aihoo.security;

import java.util.HashMap;

public class AuthUtil {
    private static final ThreadLocal<HashMap<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);
    private static final String LOGIN_KEY = "loginUser";

    public static void clear() {
        THREAD_LOCAL.remove();
    }

    public static void setLoginUser(Object user) {
        THREAD_LOCAL.get().put(LOGIN_KEY, user);
    }

    public static LoginUser getLoginUser() {
        Object value = THREAD_LOCAL.get().get(LOGIN_KEY);
        return value == null ? null : (LoginUser) value;
    }

    public static String getLoginUserId() {
        LoginUser u = getLoginUser();
        return u == null ? null : u.getId();
    }

    public static String getLoginUserName() {
        LoginUser u = getLoginUser();
        return u == null ? null : u.getName();
    }
}
