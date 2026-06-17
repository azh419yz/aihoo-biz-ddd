package com.aihoo.domain.visit.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aihoo.common.BizResultCode;
import com.aihoo.domain.im.service.ImService;
import com.aihoo.domain.visit.dto.VisitChatDto;
import com.aihoo.domain.visit.entity.Bases;
import com.aihoo.domain.visit.entity.HosRevisit;
import com.aihoo.domain.visit.entity.HosVisit;
import com.aihoo.domain.visit.mapper.BasesMapper;
import com.aihoo.domain.visit.mapper.HosRevisitMapper;
import com.aihoo.domain.visit.mapper.HosVisitMapper;
import com.aihoo.domain.visit.service.ChatService;
import com.aihoo.exception.BizException;
import com.aihoo.util.StatusEnumUtil;
import com.aihoo.util.TimeUtil;
import com.aihoo.util.WeekUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final HosVisitMapper hosVisitMapper;
    private final HosRevisitMapper hosRevisitMapper;
    private final BasesMapper basesMapper;
    private final ImService imService;

    private static final String PATIENT = "PATIENT_";
    private static final String DOCTOR = "DOCTOR_";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitChatDto startVisitChatV2(String id) {
        HosVisit hosVisit = hosVisitMapper.selectById(id);
        String status = hosVisit.getStatus();
        if (!StatusEnumUtil.SUBMITTED.equals(status)) {
            throw new BizException(BizResultCode.DOCTOR_VISIT_CHAT_STATUS_ERROR);
        }
        VisitChatDto visitChatDto = new VisitChatDto();
        hosVisit.setStatus(StatusEnumUtil.STARTED);
        String now = DateUtil.now();
        hosVisit.setStartTime(now);
        hosVisit.setHaveTime(now);
        hosVisitMapper.updateById(hosVisit);
        visitChatDto.setMsg("开始在线复诊");
        sendStartVisitIm(hosVisit.getOrderNum());

        String haveTime = hosVisit.getHaveTime();
        Date haveTimeParse = DateUtil.parse(haveTime, "yyyy-MM-dd HH:mm:ss");
        Bases bases = basesMapper.selectById("DOCTOR_VISIT_TIMES");
        String content = bases == null ? "86400" : bases.getContent();
        String betweenTime = TimeUtil.getBetweenTime(DateUtil.date(), haveTimeParse, Integer.parseInt(content));
        visitChatDto.setCutDown(betweenTime);
        visitChatDto.setOrderStatus(hosVisit.getStatus());
        return visitChatDto;
    }

    @Async
    public void sendStartVisitIm(String orderNum) {
        try {
            HosVisit hosVisit = hosVisitMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HosVisit>()
                            .eq(HosVisit::getOrderNum, orderNum));
            if (hosVisit == null) return;
            String doctorUserId = hosVisit.getDoctorUserId();
            log.info("IM 发送[在线复诊开始]: from=DOCTOR_{} to=PATIENT_{} msg=在线复诊已开始，本次在线复诊可持续24小时",
                    doctorUserId, hosVisit.getPatientUserId());
            imService.callTimV1("sendImSystemMsg",
                    "{\"fromAccount\":\"" + DOCTOR + doctorUserId + "\",\"toAccount\":\""
                            + PATIENT + hosVisit.getPatientUserId()
                            + "\",\"msg\":\"在线复诊已开始，本次在线复诊可持续24小时\"}");
        } catch (Exception e) {
            log.error("sendStartVisitIm 异常", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitChatDto stopVisitChatV2(String id) {
        HosVisit hosVisit = hosVisitMapper.selectById(id);
        String status = hosVisit.getStatus();
        if (!StatusEnumUtil.STARTED.equals(status)) {
            throw new BizException(BizResultCode.DOCTOR_VISIT_CHAT_STATUS_ERROR);
        }
        VisitChatDto visitChatDto = new VisitChatDto();
        visitChatDto.setMsg("在线复诊结束成功");

        hosVisit.setEndTime(DateUtil.now());
        hosVisit.setStatus(StatusEnumUtil.ENDED);
        hosVisitMapper.updateById(hosVisit);

        log.info("IM 发送[在线复诊结束]: from=DOCTOR_{} to=PATIENT_{} msg=本次在线复诊已结束",
                hosVisit.getDoctorUserId(), hosVisit.getPatientUserId());
        try {
            imService.callTimV1("sendImSystemMsg",
                    "{\"fromAccount\":\"" + DOCTOR + hosVisit.getDoctorUserId() + "\",\"toAccount\":\""
                            + PATIENT + hosVisit.getPatientUserId()
                            + "\",\"msg\":\"本次在线复诊已结束\"}");
        } catch (Exception e) {
            log.error("stopVisitChatV2 IM 异常", e);
        }
        return visitChatDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitChatDto startRevisitChatV2(String id) {
        HosRevisit hosRevisit = hosRevisitMapper.selectById(id);
        String status = hosRevisit.getStatus();
        if (!(StatusEnumUtil.START.equals(status) || StatusEnumUtil.HAVE.equals(status))) {
            throw new BizException(BizResultCode.DOCTOR_VISIT_CHAT_STATUS_ERROR);
        }
        VisitChatDto visitChatDto = new VisitChatDto();
        visitChatDto.setIsCanChat("1");
        if (StatusEnumUtil.START.equals(status)) {
            visitChatDto.setMsg("复诊已开始");
        } else {
            Date revisitStartTime = DateUtil.parse(hosRevisit.getRevisitStartTime(), "yyyy-MM-dd HH:mm:ss");
            Date revisitEndTime = DateUtil.parse(hosRevisit.getRevisitEndTime(), "yyyy-MM-dd HH:mm:ss");
            if (WeekUtil.inTime(DateUtil.date(), revisitStartTime, revisitEndTime)) {
                hosRevisit.setStatus(StatusEnumUtil.START);
                hosRevisit.setStartTime(DateUtil.now());
                hosRevisitMapper.updateById(hosRevisit);
                visitChatDto.setMsg("开始复诊");
                sendStartRevisitIm(hosRevisit.getOrderNum());
            } else {
                visitChatDto.setMsg("不在复诊预约时间");
                visitChatDto.setIsCanChat("0");
            }
        }
        visitChatDto.setCutDown("0");
        DateTime now = DateUtil.date();
        hosRevisit = hosRevisitMapper.selectById(id);
        if (StrUtil.isNotBlank(hosRevisit.getStartTime())) {
            DateTime startTime = DateUtil.offsetDay(DateUtil.parseDateTime(hosRevisit.getStartTime()), 1);
            if (now.before(startTime)) {
                visitChatDto.setCutDown(String.valueOf(DateUtil.between(startTime, now, DateUnit.SECOND)));
            }
        }
        visitChatDto.setOrderStatus(hosRevisit.getStatus());
        return visitChatDto;
    }

    @Async
    public void sendStartRevisitIm(String orderNum) {
        try {
            HosRevisit hosRevisit = hosRevisitMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HosRevisit>()
                            .eq(HosRevisit::getOrderNum, orderNum));
            if (hosRevisit == null) return;
            log.info("IM 发送[复诊开始]: from=DOCTOR_{} to=PATIENT_{} msg=复诊已开始，本次复诊可持续24小时",
                    hosRevisit.getDoctorUserId(), hosRevisit.getPatientUserId());
            imService.callTimV1("sendImSystemMsg",
                    "{\"fromAccount\":\"" + DOCTOR + hosRevisit.getDoctorUserId() + "\",\"toAccount\":\""
                            + PATIENT + hosRevisit.getPatientUserId()
                            + "\",\"msg\":\"复诊已开始，本次复诊可持续24小时\"}");
        } catch (Exception e) {
            log.error("sendStartRevisitIm 异常", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VisitChatDto stopRevisitChatV2(String id) {
        HosRevisit hosRevisit = hosRevisitMapper.selectById(id);
        if (!StatusEnumUtil.START.equals(hosRevisit.getStatus())) {
            throw new BizException(BizResultCode.DOCTOR_VISIT_CHAT_STATUS_ERROR);
        }
        VisitChatDto visitChatDto = new VisitChatDto();
        visitChatDto.setMsg("复诊结束成功");

        hosRevisit.setStatus(StatusEnumUtil.END);
        hosRevisit.setEndTime(DateUtil.now());
        hosRevisitMapper.updateById(hosRevisit);

        log.info("IM 发送[复诊结束]: from=DOCTOR_{} to=PATIENT_{} msg=本次在线复诊已结束",
                hosRevisit.getDoctorUserId(), hosRevisit.getPatientUserId());
        try {
            imService.callTimV1("sendImSystemMsg",
                    "{\"fromAccount\":\"" + DOCTOR + hosRevisit.getDoctorUserId() + "\",\"toAccount\":\""
                            + PATIENT + hosRevisit.getPatientUserId()
                            + "\",\"msg\":\"本次在线复诊已结束\"}");
        } catch (Exception e) {
            log.error("stopRevisitChatV2 IM 异常", e);
        }
        return visitChatDto;
    }
}
