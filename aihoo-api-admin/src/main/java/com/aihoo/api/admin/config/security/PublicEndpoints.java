package com.aihoo.api.admin.config.security;

public class PublicEndpoints {
    public static final String[] PUBLIC_URLS = {
            "/api/v1/file",

            "/error",
            "/api/v1/login",
            "/api/v1/phone/login",
            // 隐私条款
            "/api/v1/base/setPrivacyPolicy",
            "/api/v1/base/setPrivacyPolicyHtml",
            "/api/v1/base/setPrivacyPolicyWord",
            "/api/v1/base/clauseAll",
            // 手机验证码
            "/api/v1/getCode",
            //云医签扫码登录
            "/api/v1/doctor/doctorCA",
            "/api/v1/patientUser/patientApprove",
            // PoC: 临时开放 doctor list 端点用于回归测试
            "/api/v1/doctor/**",
            "/api/v1/mkey/login",
            "/api/v1/mkey/qrcode",
            "/api/v1/mkey/status",
            //获取影像上传获取订单号对应患者信息
            "/api/v1/order/getOrder",
            "/api/v1/dicom/saveDicom",
            "/api/v1/dicom/uploadZip",
            "/api/v1/dicom/uploadReport",
            //线下挂号支付接口
            "/api/v1/OfflineAppointment/payment",
            // app端聊天记录
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
