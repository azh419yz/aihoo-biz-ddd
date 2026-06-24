package com.aihoo.domain.visit.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.aihoo.common.BizResultCode;
import com.aihoo.domain.hospital.entity.Drugstore;
import com.aihoo.domain.hospital.service.DrugstoreService;
import com.aihoo.domain.logistics.service.SFService;
import com.aihoo.domain.visit.dto.*;
import com.aihoo.domain.visit.entity.HosPrescription;
import com.aihoo.domain.visit.entity.HosPrescriptionDrug;
import com.aihoo.domain.visit.entity.HosPrescriptionFee;
import com.aihoo.domain.visit.entity.HosPrescriptionInstruction;
import com.aihoo.domain.visit.mapper.HosPrescriptionMapper;
import com.aihoo.domain.visit.service.HosPrescriptionDrugService;
import com.aihoo.domain.visit.service.HosPrescriptionService;
import com.aihoo.domain.visit.service.PrescriptionFeeService;
import com.aihoo.domain.visit.service.PrescriptionInstructionService;
import com.aihoo.exception.BizException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@Log4j2
public class HosPrescriptionServiceImpl extends ServiceImpl<HosPrescriptionMapper, HosPrescription> implements HosPrescriptionService {

    private final HosPrescriptionDrugService hosPrescriptionDrugService;
    private final PrescriptionInstructionService prescriptionInstructionService;
    private final PrescriptionFeeService prescriptionFeeService;
    private final DrugstoreService drugstoreService;
    private final SFService sfService;

    public HosPrescriptionServiceImpl(HosPrescriptionDrugService hosPrescriptionDrugService,
                                      PrescriptionInstructionService prescriptionInstructionService,
                                      PrescriptionFeeService prescriptionFeeService,
                                      DrugstoreService drugstoreService,
                                      SFService sfService) {
        this.hosPrescriptionDrugService = hosPrescriptionDrugService;
        this.prescriptionInstructionService = prescriptionInstructionService;
        this.prescriptionFeeService = prescriptionFeeService;
        this.drugstoreService = drugstoreService;
        this.sfService = sfService;
    }

    @Override
    public List<HosPrescription> listByVisitMdtNum(String visitMdtNum) {
        if (visitMdtNum == null) {
            return Collections.emptyList();
        }
        return list(new LambdaQueryWrapper<HosPrescription>()
                .eq(HosPrescription::getVisitMdtNum, visitMdtNum));
    }

    @Override
    public IPage<HosPrescription> getHosPrescriptionList(PrescriptionSelectDto request) {
        return baseMapper.selectPage(new Page<>(request.getPage(), request.getLimit()),
                new LambdaQueryWrapper<HosPrescription>()
                        .eq(HosPrescription::getHosSickId, request.getHosSickId())
                        .orderByDesc(HosPrescription::getCreateTime));
    }

    public RecentPreDto getRecentPreByEntity(HosPrescription hosPrescription, String toProvince) {
        if (hosPrescription == null) {
            return null;
        }
        List<PrescriptionDrugDto> drugList = hosPrescriptionDrugService
                .list(new LambdaQueryWrapper<HosPrescriptionDrug>()
                        .eq(HosPrescriptionDrug::getHosPrescriptionId, hosPrescription.getId()))
                .stream()
                .map(prescriptionDrug -> {
                    PrescriptionDrugDto dto = new PrescriptionDrugDto();
                    BeanUtil.copyProperties(prescriptionDrug, dto);
                    return dto;
                })
                .toList();

        HosPrescriptionInstruction prescriptionInstruction = prescriptionInstructionService.getOne(
                new LambdaQueryWrapper<HosPrescriptionInstruction>()
                        .eq(HosPrescriptionInstruction::getHosPrescriptionId, hosPrescription.getId()));
        HosPrescriptionFee prescriptionFee = prescriptionFeeService.getOne(
                new LambdaQueryWrapper<HosPrescriptionFee>()
                        .eq(HosPrescriptionFee::getHosPrescriptionId, hosPrescription.getId()));
        return convert2Dto(hosPrescription, drugList, prescriptionInstruction, prescriptionFee, toProvince);
    }

    private RecentPreDto convert2Dto(HosPrescription hosPrescription,
                                     List<PrescriptionDrugDto> drugList,
                                     HosPrescriptionInstruction prescriptionInstruction,
                                     HosPrescriptionFee prescriptionFee,
                                     String toProvince) {
        RecentPreDto dto = new RecentPreDto();
        BeanUtils.copyProperties(hosPrescription, dto);

        Drugstore drugstore = drugstoreService.getById(hosPrescription.getDrugstoreId());
        if (drugstore != null) {
            dto.setDrugList(drugList);
        }

        if (prescriptionInstruction != null) {
            PrescriptionInstructionDto instruction = new PrescriptionInstructionDto();
            BeanUtils.copyProperties(prescriptionInstruction, instruction);
            dto.setInstruction(instruction);
        }

        if (prescriptionFee != null) {
            PrescriptionConsultationFeeDto consultationFee = new PrescriptionConsultationFeeDto();
            BeanUtils.copyProperties(prescriptionFee, consultationFee);
            dto.setConsultationFee(consultationFee);

            if (new BigDecimal(prescriptionFee.getDrugAmount()).compareTo(new BigDecimal("80.0")) > 0) {
                dto.setExpressPrice(new BigDecimal("0.0"));
            } else {
                if (StrUtil.isNotBlank(toProvince)) {
                    dto.setExpressPrice(sfService.getExpressPrice("北京市", toProvince));
                }
            }
        }

        return dto;
    }

    @Override
    public RecentPreDto getRecentPreById(Long id, String toProvince) {
        HosPrescription hosPrescription = getById(id);
        return getRecentPreByEntity(hosPrescription, toProvince);
    }

    @Override
    public Boolean confirmed(ConfirmedPrescriptionDto req) {
        HosPrescription prescription = getById(req.getPrescriptionId());
        if (prescription == null) {
            log.info("没有查询到该处方，参数:{}", req.getPrescriptionId());
            return Boolean.FALSE;
        }
        prescription.setStatus("WAIT");
        prescription.setConfirmedStatus(1);
        return updateById(prescription);
    }

    @Override
    public HosPrescriptionInnerDto getPrescriptionInnerVo(String id) {
        HosPrescription prescription = getById(id);
        if (prescription == null) {
            throw new BizException(BizResultCode.DATA_NOT_FOUND);
        }
        HosPrescriptionInnerDto vo = new HosPrescriptionInnerDto();
        BeanUtils.copyProperties(prescription, vo);
        vo.setDiseaseSyndrome(prescription.getDisease() + "; " + prescription.getSyndrome());
        vo.setSex("1".equals(prescription.getSex()) ? "男" : "女");

        HosPrescriptionInstruction instruction = prescriptionInstructionService.getOne(
                new LambdaQueryWrapper<HosPrescriptionInstruction>()
                        .eq(HosPrescriptionInstruction::getHosPrescriptionId, prescription.getId()));
        if (instruction != null) {
            vo.setAdvice(instruction.getAdvice());
            if (prescription.getMedicineStatusCode() != null) {
                vo.setMedicineStatusCode(prescription.getMedicineStatusCode());
            }
            String method = "共%s剂，每日%s剂，分%s次服用";
            vo.setMethod(String.format(method,
                    instruction.getDoseNumber(), instruction.getDose(), instruction.getTimes()));
        }

        List<HosPrescriptionDrug> drugList = hosPrescriptionDrugService.list(
                new LambdaQueryWrapper<HosPrescriptionDrug>()
                        .eq(HosPrescriptionDrug::getHosPrescriptionId, prescription.getId()));
        vo.setDrugVoList(drugList.stream().map(d -> {
            PrescriptionDrugInnerDto item = new PrescriptionDrugInnerDto();
            item.setName(d.getName());
            item.setNumber(d.getNumber());
            return item;
        }).toList());

        Drugstore drugstore = drugstoreService.getById(prescription.getDrugstoreId());
        if (drugstore != null) {
            vo.setDrugstoreName(drugstore.getName());
        }

        return vo;
    }
}