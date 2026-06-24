package com.aihoo.util;

public class StatusEnumUtil {
                                public final static String UNSUBMITTED = "UNSUBMITTED";
public final static String SUBMITTED = "SUBMITTED";
public final static String STARTED = "STARTED";
public final static String REFUND_PROCESSING = "REFUND_PROCESSING";
public final static String REFUNDED = "REFUNDED";
public final static String MANUALAUDIT = "MANUALAUDIT";
public final static String ENDED = "ENDED";
public final static String DONE = "DONE";
    public final static String CANCEL = "CANCEL";
    public final static String DECLINE = "DECLINE";
    public final static String WAIT = "WAIT";
    public final static String PAY = "PAY";
    public final static String START = "START";
    public final static String HAVE = "HAVE";
    public final static String END = "END";
    public final static String REFUNDWAIT = "REFUNDWAIT";
    public final static String REFUNDSUCCESS = "REFUNDSUCCESS";
    public final static String CHANGE = "CHANGE";
    public final static String REFUNDCLOSE = "REFUNDCLOSE";
    public final static String REG_PAY = "REG_PAY";
    public final static String ILLNESS_EXAMINE = "ILLNESS_EXAMINE";
    public final static String ILLNESS_EXAMINE_PASS = "ILLNESS_EXAMINE_PASS";
    public final static String CONSULTATION_CONFIRM = "CONSULTATION_CONFIRM";
    public final static String CONSULTATION = "CONSULTATION";
    public final static String CONSULTATION_END = "CONSULTATION_END";
    public final static String PASS = "PASS";
    
    public final static String NOTSIGN = "NOTSIGN";
    
    public final static String SIGN = "SIGN";
    
    public final static String REJECT = "REJECT";

    public static String getPrescriptionCheckStatus(String status) {
        if (status == null) {
            return "未知状态";
        }
        switch (status) {
            case NOTSIGN:
                return "未认证";
            case SIGN:
                return "认证通过";
            case REJECT:
                return "审核驳回";
            case WAIT:
                return "待审核";
            case PASS:
                return "审核通过";
            default:
                return "未知状态";
        }
    }

    public final static String ALIPAY = "ALIPAY";

    public final static String WECHAT = "WECHAT";

    public final static String IMAGE = "IMAGE";

    public final static String VOICE = "VOICE";

    public final static String VIDEO = "VIDEO";

    public static String getType(String type) {
        switch (type) {
            case IMAGE:
                return "图文";
            case VOICE:
                return "语音";
            case VIDEO:
                return "视频";
            default:
                return "未知状态";
        }
    }

    public static String getPayType(String type) {
        switch (type) {
            case ALIPAY:
                return "支付宝";
            case WECHAT:
                return "微信";
            default:
                return "未知状态";
        }
    }

    public static String getMdtOrderStatus(String status) {
        switch (status) {
            case DONE:
                return "订单已关闭";
            case CANCEL:
                return "订单已取消";

            case WAIT:
                return "待付款";
            case PAY:
                return "已付款";
            case HAVE:
                return "病史资料上传";
            case ILLNESS_EXAMINE:
                return "病史资料审核中";
            case ILLNESS_EXAMINE_PASS:
                return "会诊安排确认";
            case CONSULTATION_CONFIRM:
                return "待会诊";
            case CONSULTATION:
                return "会诊中";
            case CONSULTATION_END:
                return "已完成";

            default:
                return "未知状态";
        }
    }

    public static String getVisitStatus(String status) {
        switch (status) {
            case DONE:
                return "订单关闭";
            case CANCEL:
                return "取消订单";
            case WAIT:
                return "待付款";
            case PAY:
                return "已付款";
            case START:
                return "已接单";
            case HAVE:
                return "已接单";
            case END:
                return "订单完成";
            case REFUNDWAIT:
                return "退款进行中";
            case REFUNDSUCCESS:
                return "退款成功";
            case CHANGE:
                return "退款异常";
            case REFUNDCLOSE:
                return "退款关闭";
            default:
                return "未知状态";
        }
    }

    public static String getRevisitStatus(String status) {
        switch (status) {
            case DONE:
                return "订单关闭";
            case CANCEL:
                return "取消订单";
            case DECLINE:
                return "拒单";
            case WAIT:
                return "待付款";
            case PAY:
                return "已付款";
            case HAVE:
                return "已接单";
            case START:
                return "复诊进行中";
            case END:
                return "订单完成";
            case REFUNDWAIT:
                return "退款进行中";
            case REFUNDSUCCESS:
                return "退款成功";
            case CHANGE:
                return "退款异常";
            case REFUNDCLOSE:
                return "退款关闭";
            default:
                return "未知状态";
        }
    }

public static String getStatus(String status) {
        if (status == null) {
            return "未知状态";
        }
        switch (status) {
            case "START":
                return "进行中";
            case "PAY":
                return "待接单";
            case "HAVE":
                return "进行中";
            case "END":
                return "已完成";
            case "WAIT":
                return "待付款";
            case "DONE":
                return "订单已关闭";
            case "CANCEL":
                return "订单已取消";
            case "DECLINE":
                return "已拒单";
            default:
                return "未知状态";
        }
    }

public static String getVisitTypeName(String type) {
        if (type == null) {
            return "";
        }
        return "在线复诊";
    }
}
