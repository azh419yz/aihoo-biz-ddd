package com.aihoo.api.doctor.request;

import lombok.Data;

@Data
public class WxaCodeRequest {

    /**
     * 页面路径，例如：pages/index/index
     * 根路径前不要加 /
     */
    private String page;

    /**
     * 页面参数，例如：id=123&category=book
     */
    private String scene;

    /**
     * 小程序码宽度，默认430，最小280，最大1280
     */
    private Integer width = 430;

    /**
     * 是否自动配置线条颜色，默认true
     */
    private Boolean autoColor = true;

    /**
     * 线条颜色（十六进制），仅在autoColor=false时有效
     * 格式：{"r":0,"g":0,"b":0}
     */
    private Object lineColor;

    /**
     * 是否需要透明底色，默认false
     */
    private Boolean isHyaline = false;
}
