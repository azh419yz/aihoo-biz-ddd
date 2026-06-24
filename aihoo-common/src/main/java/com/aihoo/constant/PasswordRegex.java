package com.aihoo.constant;

public class PasswordRegex {
    
    public static final String PASSWORD_LOWERCASE = "(?=.*[a-z])(?=.*\\d)(?=.*[#@!~%^&*])[a-z\\d#@!~%^&*]{8,16}";

    
    public static final String PASSWORD_MIXED_CASE = "(?=.*[a-z])(?=.*\\d)(?=.*[#@!~%^&*])[A-Za-z\\d#@!~%^&*]{8,16}";
}
