package com.aihoo.constant;

/**
 * 密码正则常量。
 *
 * 来自 api-admin 两个 controller 内联常量迁移（#130 P1 R3 audit）。
 * 保留两个不同语义版本，分别对应原 MainController 和 SysUserController。
 */
public class PasswordRegex {
    /**
     * 8-16 位小写字母+数字+特殊字符（MainController 旧规则）
     */
    public static final String PASSWORD_LOWERCASE = "(?=.*[a-z])(?=.*\\d)(?=.*[#@!~%^&*])[a-z\\d#@!~%^&*]{8,16}";

    /**
     * 8-16 位大小写字母+数字+特殊字符（SysUserController 改密规则）
     */
    public static final String PASSWORD_MIXED_CASE = "(?=.*[a-z])(?=.*\\d)(?=.*[#@!~%^&*])[A-Za-z\\d#@!~%^&*]{8,16}";
}
