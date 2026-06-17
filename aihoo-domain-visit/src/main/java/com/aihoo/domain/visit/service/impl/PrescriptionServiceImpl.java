package com.aihoo.domain.visit.service.impl;

import com.aihoo.domain.drug.entity.Drugstore;
import com.aihoo.domain.drug.service.DrugstoreService;
import com.aihoo.domain.im.dto.ImSendGroupMsgRequestDto;
import com.aihoo.domain.im.service.ImService;
import com.aihoo.domain.visit.dto.PrescriptionConsultationFeeDto;
import com.aihoo.domain.visit.dto.PrescriptionDrugDto;
import com.aihoo.domain.visit.dto.PrescriptionInstructionDto;
import com.aihoo.domain.visit.dto.PrescriptionQueryDto;
import com.aihoo.domain.visit.dto.RecentPreDto;
import com.aihoo.domain.visit.dto.SavePrescriptionDto;
import com.aihoo.domain.visit.dto.SearchRecentPreDto;
import com.aihoo.domain.visit.dto.WithdrawPrescriptionDto;
import com.aihoo.domain.visit.entity.HosPrescription;
import com.aihoo.domain.visit.entity.HosPrescriptionDrug;
import com.aihoo.domain.visit.entity.HosPrescriptionFee;
import com.aihoo.domain.visit.entity.HosPrescriptionInstruction;
import com.aihoo.domain.visit.mapper.HosPrescriptionMapper;
import com.aihoo.domain.visit.service.PrescriptionFeeService;
import com.aihoo.domain.visit.service.PrescriptionInstructionService;
import com.aihoo.domain.visit.service.PrescriptionService;
import com.aihoo.domain.visit.service.HosPrescriptionDrugService;
import com.aihoo.security.AuthUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl extends ServiceImpl<HosPrescriptionMapper, HosPrescription> implements PrescriptionService {

    private final HosPrescriptionDrugService hosPrescriptionDrugService;
    private final PrescriptionInstructionService prescriptionInstructionService;
    private final PrescriptionFeeService prescriptionFeeService;
    private final DrugstoreService drugstoreService;
    private final ImService imService;

    @Override
    public long countByDoctorUserId(String doctorUserId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<HosPrescription>()
                .eq(HosPrescription::getDoctorUserId, doctorUserId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RecentPreDto savePrescription(SavePrescriptionDto request) {
        String id = request.getId();
        HosPrescription hosPrescription = getById(id);
        if (hosPrescription == null) {
            request.setId(null);
            hosPrescription = new HosPrescription();
        }
        BeanUtils.copyProperties(request, hosPrescription);
        saveOrUpdate(hosPrescription);

        hosPrescriptionDrugService.remove(new LambdaQueryWrapper<HosPrescriptionDrug>()
                .eq(HosPrescriptionDrug::getHosPrescriptionId, hosPrescription.getId()));
        List<PrescriptionDrugDto> drugList = request.getDrugList();
        HosPrescription finalHosPrescription = hosPrescription;
        List<HosPrescriptionDrug> hosPrescriptionDrugList = drugList.stream().map(drug -> {
            HosPrescriptionDrug prescriptionDrug = new HosPrescriptionDrug();
            BeanUtils.copyProperties(drug, prescriptionDrug);
            prescriptionDrug.setHosPrescriptionId(finalHosPrescription.getId());
            return prescriptionDrug;
        }).toList();
        hosPrescriptionDrugService.saveBatch(hosPrescriptionDrugList);

        PrescriptionInstructionDto instruction = request.getInstruction();
        HosPrescriptionInstruction prescriptionInstruction = null;
        if (instruction != null) {
            prescriptionInstruction = prescriptionInstructionService.getOne(new LambdaQueryWrapper<HosPrescriptionInstruction>()
                    .eq(HosPrescriptionInstruction::getHosPrescriptionId, hosPrescription.getId()));
            if (prescriptionInstruction == null) prescriptionInstruction = new HosPrescriptionInstruction();
            BeanUtils.copyProperties(instruction, prescriptionInstruction);
            prescriptionInstruction.setHosPrescriptionId(hosPrescription.getId());
            prescriptionInstructionService.saveOrUpdate(prescriptionInstruction);
        }

        PrescriptionConsultationFeeDto consultationFee = request.getConsultationFee();
        HosPrescriptionFee prescriptionFee = null;
        if (consultationFee != null) {
            prescriptionFee = prescriptionFeeService.getOne(new LambdaQueryWrapper<HosPrescriptionFee>()
                    .eq(HosPrescriptionFee::getHosPrescriptionId, hosPrescription.getId()));
            if (prescriptionFee == null) prescriptionFee = new HosPrescriptionFee();
            BeanUtils.copyProperties(consultationFee, prescriptionFee);
            prescriptionFee.setHosPrescriptionId(hosPrescription.getId());
            prescriptionFeeService.saveOrUpdate(prescriptionFee);
        }

        try {
            log.info("处方创建成功，开始发送消息");
            ImSendGroupMsgRequestDto systemMsgRequest = buildSystemMsg(request);
            imService.sendGroupMsg(systemMsgRequest);
            log.info("系统消息发送完成,参数值:{}", JSONObject.toJSONString(systemMsgRequest));

            ImSendGroupMsgRequestDto msgRequest = buildSavePrescriptionMsg(request, hosPrescription);
            imService.sendGroupMsg(msgRequest);
            log.info("小卡片消息发送完成,参数值:{}", JSONObject.toJSONString(msgRequest));
        } catch (Exception e) {
            log.info("发消息异常:", e);
        }

        return convert2Vo(hosPrescription, drugList, prescriptionInstruction, prescriptionFee);
    }

    private ImSendGroupMsgRequestDto buildSystemMsg(SavePrescriptionDto request) {
        ImSendGroupMsgRequestDto msgRequest = new ImSendGroupMsgRequestDto();
        msgRequest.setGroupId("GROUP_" + request.getVisitMdtNum());
        msgRequest.setFromAccount("ADMIN");
        msgRequest.setToAccount("PATIENT_" + request.getPatientUserId());
        msgRequest.setVisitNo(request.getVisitMdtNum());
        List<ImSendGroupMsgRequestDto.MessageBody> bodys = Lists.newArrayList();
        ImSendGroupMsgRequestDto.MessageBody body = new ImSendGroupMsgRequestDto.MessageBody();
        body.setMsgType("TIMTextElem");
        ImSendGroupMsgRequestDto.MsgParam msgParam = new ImSendGroupMsgRequestDto.MsgParam();
        msgParam.setText("系统消息：点击卡片查看处方详情并进行药品购买，建议根据处方按时吃药");
        body.setMsgContent(msgParam);
        bodys.add(body);
        msgRequest.setMsgBody(bodys);
        msgRequest.setMsgType(2);
        return msgRequest;
    }

    private ImSendGroupMsgRequestDto buildSavePrescriptionMsg(SavePrescriptionDto request, HosPrescription hosPrescription) {
        ImSendGroupMsgRequestDto msgRequest = new ImSendGroupMsgRequestDto();
        msgRequest.setGroupId("GROUP_" + request.getVisitMdtNum());
        msgRequest.setFromAccount("DOCTOR_" + request.getDoctorUserId());
        msgRequest.setVisitNo(request.getVisitMdtNum());
        List<ImSendGroupMsgRequestDto.MessageBody> bodys = Lists.newArrayList();
        ImSendGroupMsgRequestDto.MessageBody body = new ImSendGroupMsgRequestDto.MessageBody();
        body.setMsgType("TIMCustomElem");
        ImSendGroupMsgRequestDto.MsgParam msgParam = new ImSendGroupMsgRequestDto.MsgParam();
        Map<String, Object> data = Maps.newHashMap();
        data.put("medicineStatusCode", request.getMedicineStatusCode());
        data.put("type", "savePrescription");
        data.put("createTime", hosPrescription.getCreateTime());
        data.put("sickName", hosPrescription.getName());
        data.put("sex", hosPrescription.getSex());
        data.put("age", hosPrescription.getAge());
        data.put("disease", hosPrescription.getDisease());
        data.put("syndrome", hosPrescription.getSyndrome());
        data.put("hosPrescriptionId", hosPrescription.getId());

        Map<String, Object> bigBody = Maps.newHashMap();
        bigBody.put("type", "savePrescription");
        bigBody.put("desc", "处方小卡片");
        bigBody.put("data", data);

        msgParam.setData(JSONObject.toJSONString(bigBody));
        body.setMsgContent(msgParam);
        bodys.add(body);
        msgRequest.setMsgBody(bodys);
        msgRequest.setLoadParam(1);
        msgRequest.setMsgType(1);
        return msgRequest;
    }

    @Override
    public RecentPreDto getRecentPre(SearchRecentPreDto request) {
        HosPrescription hosPrescription = getOne(new LambdaQueryWrapper<HosPrescription>()
                .eq(HosPrescription::getHosSickId, request.getHosSickId())
                .eq(HosPrescription::getDoctorUserId, request.getDoctorUserId())
                .orderByDesc(HosPrescription::getCreateTime)
                .last("limit 1"));
        return getRecentPreByEntity(hosPrescription);
    }

    @Override
    public IPage<HosPrescription> getHosPrescriptionList(PrescriptionQueryDto request) {
        return this.baseMapper.selectPage(new Page<>(request.getPage(), request.getLimit()),
                new LambdaQueryWrapper<HosPrescription>()
                        .eq(HosPrescription::getDoctorUserId, AuthUtil.getLoginUserId())
                        .orderByDesc(HosPrescription::getCreateTime));
    }

    @Override
    public RecentPreDto getRecentPreById(Long id) {
        HosPrescription hosPrescription = getById(id);
        return getRecentPreByEntity(hosPrescription);
    }

    @Override
    public Boolean withdrawPrescription(WithdrawPrescriptionDto req) {
        HosPrescription prescription = getById(req.getPrescriptionId());
        if (prescription == null) {
            log.info("没有查询到该处方,参数:{}", req.getPrescriptionId());
            return Boolean.FALSE;
        }

        if (!Integer.valueOf(prescription.getDoctorUserId()).equals(AuthUtil.getLoginUserId())) {
            log.info("该处方此医生没有操作权限,参数:{},医生:{}", req.getPrescriptionId(), AuthUtil.getLoginUserId());
            return Boolean.FALSE;
        }
        prescription.setStatus("WITHDRAW");
        return updateById(prescription);
    }

    @Override
    public Boolean generateEprescription(Long prescriptionId) {
        HosPrescription hosPrescription = getById(prescriptionId);
        if (hosPrescription == null) {
            log.info("没有查询到该处方,参数:{}", prescriptionId);
            return Boolean.FALSE;
        }
        if (hosPrescription.getImg() != null && !hosPrescription.getImg().isEmpty()) {
            log.info("电子处方已生成,参数:{}", prescriptionId);
            return Boolean.TRUE;
        }
        // JudgeService.generatePrescriptionPdf 在 DDD 阶段未迁移，按 CLAUDE.md 教训 7 保持简单桩：标记为 SIGN 但不生成 PDF。
        hosPrescription.setCheckStatus("SIGN");
        updateById(hosPrescription);
        log.warn("JudgeService 未实现，电子处方 PDF 未生成，仅更新状态");
        return Boolean.TRUE;
    }

    public RecentPreDto getRecentPreByEntity(HosPrescription hosPrescription) {
        if (hosPrescription == null) {
            return null;
        }
        List<PrescriptionDrugDto> drugList = hosPrescriptionDrugService.list(new LambdaQueryWrapper<HosPrescriptionDrug>()
                        .eq(HosPrescriptionDrug::getHosPrescriptionId, hosPrescription.getId()))
                .stream()
                .map(prescriptionDrug -> {
                    PrescriptionDrugDto dto = new PrescriptionDrugDto();
                    BeanUtils.copyProperties(prescriptionDrug, dto);
                    return dto;
                })
                .toList();

        HosPrescriptionInstruction prescriptionInstruction = prescriptionInstructionService.getOne(new LambdaQueryWrapper<HosPrescriptionInstruction>()
                .eq(HosPrescriptionInstruction::getHosPrescriptionId, hosPrescription.getId()));
        HosPrescriptionFee prescriptionFee = prescriptionFeeService.getOne(new LambdaQueryWrapper<HosPrescriptionFee>()
                .eq(HosPrescriptionFee::getHosPrescriptionId, hosPrescription.getId()));
        return convert2Vo(hosPrescription, drugList, prescriptionInstruction, prescriptionFee);
    }

    private RecentPreDto convert2Vo(HosPrescription hosPrescription, List<PrescriptionDrugDto> drugList,
                                   HosPrescriptionInstruction prescriptionInstruction, HosPrescriptionFee prescriptionFee) {
        RecentPreDto recentPreVo = new RecentPreDto();
        BeanUtils.copyProperties(hosPrescription, recentPreVo);
        if (AuthUtil.getLoginUserId() != null) {
            recentPreVo.setDoctorUserName(AuthUtil.getLoginUserId());
        }
        recentPreVo.setDrugList(drugList);

        Drugstore drugstore = drugstoreService.getById(hosPrescription.getDrugstoreId());
        if (drugstore != null) {
            recentPreVo.setDrugstoreId(drugstore.getId());
        }

        if (prescriptionInstruction != null) {
            PrescriptionInstructionDto instruction = new PrescriptionInstructionDto();
            BeanUtils.copyProperties(prescriptionInstruction, instruction);
            recentPreVo.setInstruction(instruction);
        }

        if (prescriptionFee != null) {
            PrescriptionConsultationFeeDto consultationFee = new PrescriptionConsultationFeeDto();
            BeanUtils.copyProperties(prescriptionFee, consultationFee);
            recentPreVo.setConsultationFee(consultationFee);
        }

        return recentPreVo;
    }
}
