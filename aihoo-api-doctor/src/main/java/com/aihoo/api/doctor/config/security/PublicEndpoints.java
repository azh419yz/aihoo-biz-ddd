package com.aihoo.api.doctor.config.security;

public class PublicEndpoints {
    public static final String[] PUBLIC_URLS = {
            "/error",
            "/api/v1/doctorUser/",
            "/api/v1/doctorUser/**",      // 登录不拦截
            "/api/v1/api/",             // 登录不拦截
            "/api/v1/api/**",             // 登录不拦截
            "/api/v1/hosVisit/imSend",    // IM 发送消息测试接口
            "/swagger-ui.html",
            "/swagger-ui/",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/webjars/",
            "/webjars/**",
            "/webjars/swagger-ui/**",
            "/api/v2/chuanglan/login",
            "/api/v2/doctorUser/sendCode",
            "/api/v2/doctorUser/phoneLogin",
    };
}
