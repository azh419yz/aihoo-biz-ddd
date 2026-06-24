package com.aihoo.api.admin.config.security;

public class PublicEndpoints {
    public static final String[] PUBLIC_URLS = {
            "/api/v1/file",

            "/error",
            "/api/v1/login",
            "/api/v1/phone/login",

            "/api/v1/base/setPrivacyPolicy",
            "/api/v1/base/setPrivacyPolicyHtml",
            "/api/v1/base/setPrivacyPolicyWord",
            "/api/v1/base/clauseAll",

            "/api/v1/getCode",

            "/api/v1/doctor/doctorCA",
            "/api/v1/patientUser/patientApprove",

            "/api/v1/doctor/**",
            "/api/v1/mkey/login",
            "/api/v1/mkey/qrcode",
            "/api/v1/mkey/status",

            "/api/v1/order/getOrder",
            "/api/v1/dicom/saveDicom",
            "/api/v1/dicom/uploadZip",
            "/api/v1/dicom/uploadReport",

            "/api/v1/OfflineAppointment/payment",

            "/api/v1/order/visitRecord",
            "/logout",

            "/swagger-ui.html",
            "/swagger-ui/",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/webjars/",
            "/webjars/**",
            "/webjars/swagger-ui/**",
            "/api/inner/upload/oss",
            "/api/inner/prescription"
    };
}
