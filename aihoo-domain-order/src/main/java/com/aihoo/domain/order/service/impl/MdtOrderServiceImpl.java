package com.aihoo.domain.order.service.impl;

import com.aihoo.alicloud.AliCloudComponent;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.drug.entity.Drugstore;
import com.aihoo.domain.drug.service.DrugstoreService;
import com.aihoo.domain.order.dto.MdtOrderListRequestDto;
import com.aihoo.domain.order.dto.MdtOrderPageVo;
import com.aihoo.domain.order.dto.MdtOrderSaveRespVo;
import com.aihoo.domain.order.dto.MdtOrderViewPrescriptionDrugVo;
import com.aihoo.domain.order.dto.MdtOrderViewPrescriptionVo;
import com.aihoo.domain.order.dto.MdtOrderViewVo;
import com.aihoo.domain.order.entity.MdtOrder;
import com.aihoo.domain.order.mapper.MdtOrderMapper;
import com.aihoo.domain.order.service.MdtOrderService;
import com.aihoo.domain.visit.entity.HosPrescription;
import com.aihoo.domain.visit.entity.HosPrescriptionDrug;
import com.aihoo.domain.visit.entity.HosPrescriptionFee;
import com.aihoo.domain.visit.entity.HosPrescriptionInstruction;
import com.aihoo.domain.visit.service.HosPrescriptionDrugService;
import com.aihoo.domain.visit.service.HosPrescriptionService;
import com.aihoo.domain.visit.service.PrescriptionFeeService;
import com.aihoo.domain.visit.service.PrescriptionInstructionService;
import com.aihoo.security.AuthUtil;
import com.aihoo.util.OrderNoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * mdt 订单 service 实现（迁自 patient-api 的 MdtOrderServiceImpl，
 * 仅保留 api-patient controller 实际调用的方法）。
 */
@Slf4j
@Service
public class MdtOrderServiceImpl extends ServiceImpl<MdtOrderMapper, MdtOrder> implements MdtOrderService {

    private final HosPrescriptionService hosPrescriptionService;
    private final PrescriptionFeeService prescriptionFeeService;
    private final HosPrescriptionDrugService hosPrescriptionDrugService;
    private final PrescriptionInstructionService prescriptionInstructionService;
    private final DoctorUserService doctorUserService;
    private final DrugstoreService drugstoreService;
    private final AliCloudComponent aliCloudComponent;

    public MdtOrderServiceImpl(HosPrescriptionService hosPrescriptionService,
                               PrescriptionFeeService prescriptionFeeService,
                               HosPrescriptionDrugService hosPrescriptionDrugService,
                               PrescriptionInstructionService prescriptionInstructionService,
                               DoctorUserService doctorUserService,
                               DrugstoreService drugstoreService,
                               AliCloudComponent aliCloudComponent) {
        this.hosPrescriptionService = hosPrescriptionService;
        this.prescriptionFeeService = prescriptionFeeService;
        this.hosPrescriptionDrugService = hosPrescriptionDrugService;
        this.prescriptionInstructionService = prescriptionInstructionService;
        this.doctorUserService = doctorUserService;
        this.drugstoreService = drugstoreService;
        this.aliCloudComponent = aliCloudComponent;
    }

    @Override
    public long countOrderByPatientUserId(String patientUserId) {
        return count(new LambdaQueryWrapper<MdtOrder>().eq(MdtOrder::getPatientUserId, patientUserId));
    }

    @Override
    public PageResult<MdtOrderPageVo> pageOrderListByPatientUserId(PageParam<MdtOrder> pageParam) {
        LambdaQueryWrapper<MdtOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MdtOrder::getPatientUserId, AuthUtil.getLoginUserId());
        queryWrapper.orderByDesc(MdtOrder::getPayTime);

        Page<MdtOrder> page = page(pageParam, queryWrapper);

        List<MdtOrderPageVo> voList = new ArrayList<>();
        for (MdtOrder order : page.getRecords()) {
            MdtOrderPageVo vo = new MdtOrderPageVo();
            BeanUtils.copyProperties(order, vo);
            voList.add(vo);
        }
        return new PageResult<>(voList, page.getTotal());
    }

    @Override
    public MdtOrderSaveRespVo saveOrder(MdtOrder order) {
        try {
            HosPrescription prescription = hosPrescriptionService.getById(order.getPreId());
            if (prescription == null) {
                return new MdtOrderSaveRespVo(Boolean.FALSE, "没有查询到该处方!", null);
            }
            HosPrescriptionFee fee = prescriptionFeeService.getOne(
                    new LambdaQueryWrapper<HosPrescriptionFee>()
                            .eq(HosPrescriptionFee::getHosPrescriptionId, prescription.getId()));
            if (fee == null) {
                return new MdtOrderSaveRespVo(Boolean.FALSE, "该处方金额有误!", null);
            }
            List<MdtOrder> orders = list(new LambdaQueryWrapper<MdtOrder>()
                    .eq(MdtOrder::getPreId, order.getPreId()));
            if (!orders.isEmpty()) {
                return new MdtOrderSaveRespVo(Boolean.FALSE, "已存在订单，请勿重新下单!", null);
            }
            DoctorUser doctor = doctorUserService.getById(order.getDoctorUserId());
            if (doctor != null) {
                order.setDoctorUserName(doctor.getName());
            }
            order.setOrderNum("M" + OrderNoUtil.getOrderNo());
            order.setIdCard(prescription.getIdCard());
            order.setStatus("WAIT");
            boolean saved = save(order);
            MdtOrderSaveRespVo resp = new MdtOrderSaveRespVo();
            resp.setResult(saved);
            resp.setMsg("创建订单成功");
            resp.setOrder(new MdtOrderSaveRespVo.MdtOrderSaveView(
                    Long.valueOf(order.getId()), order.getOrderNum(), Long.valueOf(order.getPreId())));
            return resp;
        } catch (Exception e) {
            log.info("创建订单异常", e);
            return new MdtOrderSaveRespVo(Boolean.FALSE,
                    String.format("系统异常,请检查。%s", e.getMessage()), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payOrder(String orderNum) {
        String userId = AuthUtil.getLoginUserId();
        MdtOrder order = getOne(new LambdaQueryWrapper<MdtOrder>()
                .eq(MdtOrder::getOrderNum, orderNum)
                .eq(MdtOrder::getPatientUserId, userId)
                .last("limit 1"));
        if (order == null) {
            log.info("没有查询到处方关联订单，参数:{},用户:{}", orderNum, userId);
            return false;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        order.setPayTime(LocalDateTime.now().format(formatter));
        order.setPayType("WECHAT");
        order.setStatus("UNALLOCATED");
        updateById(order);

        HosPrescription prescription = hosPrescriptionService.getById(order.getPreId());
        if (prescription != null) {
            prescription.setIsPay("0");
            prescription.setPayType(order.getPayType());
            prescription.setPayTime(order.getPayTime());
            hosPrescriptionService.updateById(prescription);

            DoctorUser doctor = doctorUserService.getById(prescription.getDoctorUserId());
            String doctorName = doctor != null ? doctor.getName() : "医生";
            aliCloudComponent.sendSms(order.getMobile(),
                    "【像数云医】您已成功购买" + doctorName + "医生的处方，药师将尽快审核处方并进行调配，您可点击链接查看订单详情：链接。如有问题可拨打客服电话：400-888-6890。");
        }

        return Boolean.TRUE;
    }

    @Override
    public MdtOrderViewVo selectMdtOrderViewVo(String mdtOrderNum) {
        MdtOrder order = getOne(new LambdaQueryWrapper<MdtOrder>()
                .eq(MdtOrder::getOrderNum, mdtOrderNum)
                .last("limit 1"));
        if (order == null) {
            return null;
        }
        return toOrderViewVo(order);
    }

    @Override
    public IPage<MdtOrderViewVo> selectMdtOrderViewList(MdtOrderListRequestDto req) {
        IPage<MdtOrder> orders = page(new Page<>(req.getPage(), req.getLimit()),
                new LambdaQueryWrapper<MdtOrder>()
                        .eq(MdtOrder::getPatientUserId, AuthUtil.getLoginUserId())
                        .orderByDesc(MdtOrder::getPayTime));
        return orders.convert(entity -> {
            MdtOrderViewVo vo = new MdtOrderViewVo();
            BeanUtils.copyProperties(entity, vo);
            if (StringUtils.hasText(entity.getDrugstoreId())) {
                Drugstore drugstore = drugstoreService.getById(entity.getDrugstoreId());
                if (drugstore != null) {
                    vo.setDrugstoreName(drugstore.getName());
                }
            }
            return vo;
        });
    }

    private MdtOrderViewVo toOrderViewVo(MdtOrder order) {
        MdtOrderViewVo vo = new MdtOrderViewVo();
        BeanUtils.copyProperties(order, vo);

        HosPrescription prescription = hosPrescriptionService.getById(order.getPreId());
        if (prescription == null) {
            return vo;
        }

        MdtOrderViewPrescriptionVo prescriptionVo = new MdtOrderViewPrescriptionVo();
        BeanUtils.copyProperties(prescription, prescriptionVo);

        List<HosPrescriptionDrug> drugs = hosPrescriptionDrugService.list(
                new LambdaQueryWrapper<HosPrescriptionDrug>()
                        .eq(HosPrescriptionDrug::getHosPrescriptionId, prescription.getId()));

        List<MdtOrderViewPrescriptionDrugVo> drugVos = new ArrayList<>();
        for (HosPrescriptionDrug drug : drugs) {
            MdtOrderViewPrescriptionDrugVo drugVo = new MdtOrderViewPrescriptionDrugVo();
            BeanUtils.copyProperties(drug, drugVo);
            drugVos.add(drugVo);
        }

        prescriptionVo.setDrugs(drugVos);

        HosPrescriptionInstruction instruction = prescriptionInstructionService.getOne(
                new LambdaQueryWrapper<HosPrescriptionInstruction>()
                        .eq(HosPrescriptionInstruction::getHosPrescriptionId, prescription.getId()));
        prescriptionVo.setInstruction(instruction);

        vo.setPrescription(prescriptionVo);
        return vo;
    }
}