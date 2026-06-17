package com.aihoo.api.patient.config.security;

public class PublicEndpoints {
    public static final String[] PUBLIC_URLS = {
            "/error",
            "/api/v1/patientUser/",
            "/api/v1/patientUser/**",      // 登录不拦截
            "/api/v1/login/**",            // 登录相关白名单
            "/api/v1/wxCallback/**",       // 微信支付回调
            "/api/v1/aliCallback/**",      // 支付宝回调
            "/api/v1/pay/**",              // 支付回调
            "/api/v1/imCallback/**",       // IM 回调
            "/api/v1/thirdPartyCall/**",   // 第三方回调

            "/swagger-ui.html",
            "/swagger-ui/",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/webjars/",
            "/webjars/**",
            "/webjars/swagger-ui/**",

            "/api/v2/patientUser/sendCode",
            "/api/v2/patientUser/phoneLogin",
    };
}
