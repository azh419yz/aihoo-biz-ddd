package com.aihoo.enums;

/**
 * @Classname BrandTypeEnum
 * @Description hf
 * @Date 2020/9/16 9:54
 * @Created by ad
 */
public enum BrandTypeEnum {
    NONE("无跳转", "NONE"),
    DOCKER("医生详情", "DOCKER"),
    DISEASE("疾病详情", "DISEASE"),
    TEXTAREA("富文本", "TEXTAREA"),
    MDTDOCTOR("团队医生", "MDTDOCTOR"),
    MDTTEAM("团队", "MDTTEAM");

    private String name;
    private String code;

    BrandTypeEnum(String name, String code) {
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
