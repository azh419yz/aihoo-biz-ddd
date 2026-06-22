package com.aihoo.domain.visit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.aihoo.common.BizResultCode;
import com.aihoo.domain.im.service.ImService;
import com.aihoo.domain.visit.dto.VisitChatDto;
import com.aihoo.domain.visit.entity.Bases;
import com.aihoo.domain.visit.entity.HosVisit;
import com.aihoo.domain.visit.mapper.BasesMapper;
import com.aihoo.domain.visit.mapper.HosVisitMapper;
import com.aihoo.domain.visit.service.ChatService;
import com.aihoo.exception.BizException;
import com.aihoo.util.StatusEnumUtil;
import com.aihoo.util.TimeUtil;
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

}
