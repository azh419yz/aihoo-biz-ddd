package com.aihoo.api.patient.config.security;

public class PublicEndpoints {
    public static final String[] PUBLIC_URLS = {
            "/error",
            "/api/v1/patientUser/",
            "/api/v1/patientUser/**",
            "/api/v1/login/**",
            "/api/v1/wxCallback/**",
            "/api/v1/aliCallback/**",
            "/api/v1/pay/**",
            "/api/v1/imCallback/**",
            "/api/v1/thirdPartyCall/**",

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
            "/api/v2/patientUser/weChatLogin",
    };
}
