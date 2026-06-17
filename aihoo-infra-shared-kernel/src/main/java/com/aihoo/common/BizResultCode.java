package com.aihoo.common;

/**
 * 响应状态码枚举
 *
 * @author heou
 */
public enum BizResultCode {

    SUCCESS(200, "成功"),

    BAD_REQUEST(400, "请求错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    CONFLICT(409, "资源冲突"),
    UNPROCESSABLE_ENTITY(422, "无法处理的实体"),

    INTERNAL_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    PARAM_ERROR(1001, "参数错误"),
    DATA_NOT_FOUND(1002, "数据不存在"),
    DATA_ALREADY_EXISTS(1003, "数据已存在"),
    OPERATION_FAILED(1004, "操作失败"),

    LOGIN_FAILED(2001, "登录失败"),
    TOKEN_EXPIRED(2002, "Token 已过期"),
    TOKEN_INVALID(2003, "Token 无效"),
    USER_NOT_FOUND(2004, "用户不存在"),
    PASSWORD_ERROR(2005, "密码错误"),
    ACCOUNT_DISABLED(2006, "账号已禁用"),
    SMS_CODE_EXPIRED(2007, "验证码已过期"),
    SMS_CODE_ERROR(2008, "验证码错误"),

    WECHAT_OPENID_ERROR(3001, "微信临时凭证无效"),
    WECHAT_ACCESS_TOKEN_ERROR(3002, "微信获取Access Token失败"),
    WECHAT_MOBILE_ERROR(3003, "微信获取用户手机号失败"),
    SMS_SEND_ERROR(3009, "验证码发送失败"),
    OSS_UPLOAD_ERROR(3010, "OSS文件上传失败"),

    DOCTOR_MOBILE_NOT_BOUND(10001, "手机号码暂未绑定"),
    DOCTOR_ACCOUNT_DISABLED(10002, "账号已被禁用"),
    DOCTOR_ACCOUNT_NO_AUTH(10002, "此账号未进行CA认证"),
    DOCTOR_VISIT_CHAT_STATUS_ERROR(10003, "订单状态错误"),
    DOCTOR_VISIT_CHAT_TIME_ERROR(10004, "不在复诊预约时间"),

    PATIENT_MOBILE_NOT_BOUND(11001, "手机号码暂未绑定"),
    PATIENT_MOBILE_EXIST(11002, "手机号码已被占用"),
    PATIENT_HOS_SICK_NOT_FOUND(11003, "就诊人不存在"),
    PATIENT_DOCTOR_NOT_FOUND(11004, "医生不存在"),
    PATIENT_DOCTOR_ACCOUNT_ERROR(11005, "医生账号异常"),
    PATIENT_DOCTOR_IS_NOT_IMG(11006, "该医生已关闭问诊服务"),
    PATIENT_DOCTOR_IS_DISTURB(11007, "当前为医生免打扰时间，请稍后再试"),
    PATIENT_HOS_VISIT_NOT_FOUND(11008, "问诊单不存在"),
    PATIENT_HOS_VISIT_CAN_NOT_CANCEL(11009, "问诊单不支持取消"),
    PATIENT_HOS_VISIT_WAIT_STATUS(11010, "存在待付款的问诊单，请先付款"),
    PATIENT_HOS_VISIT_PAY_STATUS(11011, "存在正在问诊中的订单"),
    PATIENT_HOS_VISIT_HAVE_STATUS(11012, "存在正在问诊中的订单"),
    PATIENT_DOCTOR_OVERSTEP_LIMIT(11013, "医生接诊过多，请稍后再试"),
    PATIENT_DOCTOR_PRICE_CHANGE(11014, "问诊价格发生变化"),

    USER_MOBILE_EXIST(10001, "手机号已存在");

    private final int code;
    private final String msg;

    BizResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}