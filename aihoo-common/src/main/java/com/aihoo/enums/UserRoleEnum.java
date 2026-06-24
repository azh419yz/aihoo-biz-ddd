package com.aihoo.enums;

public enum UserRoleEnum {
    YLGJ("医疗管家", "17"),
    HZZLYS("会诊/助理医生", "18"),

    ASSISTANT("助理医生", "ASSISTANT"),
    CONSULTANT("会诊医生", "CONSULTANT"),
    ADMIN("医疗管家", "ADMIN");

    private String name;
    private String code;

    UserRoleEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}