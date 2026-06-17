package com.aihoo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsModelSendUtil {

    @Async(value = "asyncExecutor")
    public void tenMinNotPay(String mobile, String name, String type) {
        try {
            String model = "您的{$var}请求已提交，请尽快完成支付。";
            log.info(SmsUtils.offlineSend(model, mobile, name, type));
        } catch (Exception e) {
            log.error("短信发送失败", e);
        }
    }

    @Async(value = "asyncExecutor")
    public void visitStartRemindPatient(String mobile, String name) {
        try {
            String model = "{$var}已经接受您的问诊邀请，请尽快前往查看与医生交流病情。";
            log.info(SmsUtils.offlineSend(model, mobile, name));
        } catch (Exception ignored) { }
    }

    @Async(value = "asyncExecutor")
    public void revisitStartRemindPatient(String mobile, String depart, String name) {
        try {
            String model = "您预约的{$var}{$var}医生复诊时间已到，请尽快前往查看与医生沟通。";
            log.info(SmsUtils.offlineSend(model, mobile, depart, name));
        } catch (Exception ignored) { }
    }

    @Async(value = "asyncExecutor")
    public void revisitStartRemindDoctor(String mobile, String name) {
        try {
            String model = "{$var}向您提交的复诊时间已到，请尽快前往处理。";
            log.info(SmsUtils.offlineSend(model, mobile, name));
        } catch (Exception ignored) { }
    }

    @Async(value = "asyncExecutor")
    public void revisitWaitHaveRemindDoctor(String mobile, String name) {
        try {
            String model = "患者{$var}提交的复诊预约您尚未处理，请尽快确认是否接单。";
            log.info(SmsUtils.offlineSend(model, mobile, name));
        } catch (Exception ignored) { }
    }

    @Async(value = "asyncExecutor")
    public void preWaitPayRemindPatient(String mobile, String name) {
        try {
            String model = "{$var}开具的处方已审核通过，请尽快完成支付。";
            log.info(SmsUtils.offlineSend(model, mobile, name));
        } catch (Exception ignored) { }
    }

    @Async(value = "asyncExecutor")
    public void confirmMdtTimeRemindAll(String mobile, String name, String time) {
        try {
            String model = "{$var}已确认会诊安排，会诊时间为{$var}，请提前十分钟进入会诊室调试设备。";
            log.info(SmsUtils.offlineSend(model, mobile, name, time));
        } catch (Exception ignored) { }
    }

    @Async(value = "asyncExecutor")
    public void oneDayMdtRemindPatient(String mobile, String name, String time) {
        try {
            String model = "{$var}已确认会诊安排，会诊时间为{$var}，请提前十分钟进入会诊室调试设备。";
            log.info(SmsUtils.offlineSend(model, mobile, name, time));
        } catch (Exception ignored) { }
    }

    @Async(value = "asyncExecutor")
    public void oneDayMdtRemindDoctor(String mobile, String name, String time, String mdt) {
        try {
            String model = "{$var}预约的{$var}的{$var}会诊将于明天开始，请提前做好准备。";
            log.info(SmsUtils.offlineSend(model, mobile, name, time, mdt));
        } catch (Exception ignored) { }
    }

    @Async(value = "asyncExecutor")
    public void tenMinMdtRemindAll(String mobile, String name, String time, String mdt) {
        try {
            String model = "{$var}预约的{$var}的{$var}会诊即将开始，请提前进入会诊室。";
            log.info(SmsUtils.offlineSend(model, mobile, name, time, mdt));
        } catch (Exception ignored) { }
    }

    @Async(value = "asyncExecutor")
    public void mdtReportAutographRemindDoctor(String mobile, String name) {
        try {
            String model = "{$var}的会诊报告已发送，请尽快前往审核。";
            log.info(SmsUtils.offlineSend(model, mobile, name));
        } catch (Exception ignored) { }
    }
}
