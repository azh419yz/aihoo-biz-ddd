package com.aihoo.enums;

public enum MdtTypeEnum {
    DICTCODE("字典表码值", "MDT_TYPE"),
    PERSONAL("单医生会诊", "PERSONAL"),
    TEAM("团队医生", "TEAM"),
    COMBINATION("组合医生会诊", "COMBINATION");

    private String name;
    private String code;

    MdtTypeEnum(String name, String code) {
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