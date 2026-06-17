package com.aihoo.enums;

/**
 * @Classname LoginErrorCodeEnum
 * @Description hf   前端需要提示不同的位置，要返回不同code
 * @Date 2020/11/23 16:43
 * @Created by ad
 */
public interface LoginErrorCodeEnum {
    // 密码错误code
    int PASSWORD_CODE = 9000;
    // 账号错误code
    int ACCOUNT_NONENTITY = 9001;
    // 验证码错误 code
    int CODE_ERROR = 9002;
    // 手机号错误 code
    int PHONE_NONENTITY = 9003;
    // 扫码登录错误 code
    int QRCODE_ERROR = 9004;
    // 扫码成功通知 code
    int QRCODE_CONFIRM = 9005;
    // 扫码登录等待 code
    int QRCODE_WAIT = 9006;
    // 扫码登录超时 cod
    int QRCODE_TIMEOUT = 9007;
}
