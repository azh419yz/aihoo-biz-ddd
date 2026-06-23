package com.aihoo.domain.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aihoo.alicloud.AliCloudComponent;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.drug.entity.Drugstore;
import com.aihoo.domain.drug.service.DrugstoreService;
import com.aihoo.domain.order.dto.MdtOrderAdminDto;
import com.aihoo.domain.order.dto.MdtOrderDrugExportEntity;
import com.aihoo.domain.order.dto.MdtOrderExportEntity;
import com.aihoo.domain.order.dto.MdtOrderListRequestDto;
import com.aihoo.domain.order.dto.MdtOrderPageRespDto;
import com.aihoo.domain.order.dto.MdtOrderSaveRespDto;
import com.aihoo.domain.order.dto.MdtOrderViewPrescriptionDrugDto;
import com.aihoo.domain.order.dto.MdtOrderViewPrescriptionDto;
import com.aihoo.domain.order.dto.MdtOrderViewDto;
import com.aihoo.domain.order.dto.PrescriptionDrugDTO;
import com.aihoo.domain.order.dto.SearchMdtOrderRequestDto;
import com.aihoo.domain.order.entity.MdtOrder;
import com.aihoo.domain.order.mapper.MdtOrderMapper;
import com.aihoo.domain.order.service.MdtOrderService;
import com.aihoo.domain.sys.excel.ExcelUtils;
import com.aihoo.domain.visit.entity.HosPrescription;
import com.aihoo.domain.visit.entity.HosPrescriptionDrug;
import com.aihoo.domain.visit.entity.HosPrescriptionFee;
import com.aihoo.domain.visit.entity.HosPrescriptionInstruction;
import com.aihoo.domain.visit.service.HosPrescriptionDrugService;
import com.aihoo.domain.visit.service.HosPrescriptionService;
import com.aihoo.domain.visit.service.PrescriptionFeeService;
import com.aihoo.domain.visit.service.PrescriptionInstructionService;
import com.aihoo.exception.BizException;
import com.aihoo.security.AuthUtil;
import com.aihoo.util.DateUtil;
import com.aihoo.util.HttpUtil;
import com.aihoo.util.JSONUtil;
import com.aihoo.util.OrderNoUtil;
import com.aihoo.util.StatusEnumUtil;
import com.aihoo.util.StringHandler;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * mdt 订单 service 实现（按 controller 合并：patient-api + admin-api）。
 *
 * <p>patient 阶段提供：countOrderByPatientUserId / pageOrderListByPatientUserId / saveOrder /
 * payOrder / selectMdtOrderViewVo / selectMdtOrderViewList。</p>
 * <p>admin 阶段提供：getPage / count / printPdf / printExpress / completeAllocation /
 * saveRemarkAndPic / export / drugExport。</p>
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

    @Value("${pst.endpoint:pst.heouai.com}")
    private String pstEndpoint;

    @Value("${pst.api.generate-pdf:/api/print/async}")
    private String pstGeneratePdfApi;

    @Value("${pst.parameter.generate-pdf-url:https://manage.heouai.com/chufang?id=}")
    private String pstGeneratePdfUrl;

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
    public PageResult<MdtOrderPageRespDto> pageOrderListByPatientUserId(PageParam<MdtOrder> pageParam) {
        LambdaQueryWrapper<MdtOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MdtOrder::getPatientUserId, AuthUtil.getLoginUserId());
        queryWrapper.orderByDesc(MdtOrder::getPayTime);

        Page<MdtOrder> page = page(pageParam, queryWrapper);

        List<MdtOrderPageRespDto> voList = new ArrayList<>();
        for (MdtOrder order : page.getRecords()) {
            MdtOrderPageRespDto vo = new MdtOrderPageRespDto();
            BeanUtils.copyProperties(order, vo);
            voList.add(vo);
        }
        return new PageResult<>(voList, page.getTotal());
    }

    @Override
    public MdtOrderSaveRespDto saveOrder(MdtOrder order) {
        try {
            HosPrescription prescription = hosPrescriptionService.getById(order.getPreId());
            if (prescription == null) {
                return new MdtOrderSaveRespDto(Boolean.FALSE, "没有查询到该处方!", null);
            }
            HosPrescriptionFee fee = prescriptionFeeService.getOne(
                    new LambdaQueryWrapper<HosPrescriptionFee>()
                            .eq(HosPrescriptionFee::getHosPrescriptionId, prescription.getId()));
            if (fee == null) {
                return new MdtOrderSaveRespDto(Boolean.FALSE, "该处方金额有误!", null);
            }
            List<MdtOrder> orders = list(new LambdaQueryWrapper<MdtOrder>()
                    .eq(MdtOrder::getPreId, order.getPreId()));
            if (!orders.isEmpty()) {
                return new MdtOrderSaveRespDto(Boolean.FALSE, "已存在订单，请勿重新下单!", null);
            }
            DoctorUser doctor = doctorUserService.getById(order.getDoctorUserId());
            if (doctor != null) {
                order.setDoctorUserName(doctor.getName());
            }
            order.setOrderNum("M" + OrderNoUtil.getOrderNo());
            order.setIdCard(prescription.getIdCard());
            order.setStatus("WAIT");
            boolean saved = save(order);
            MdtOrderSaveRespDto resp = new MdtOrderSaveRespDto();
            resp.setResult(saved);
            resp.setMsg("创建订单成功");
            MdtOrderSaveRespDto.MdtOrderSaveView view = new MdtOrderSaveRespDto.MdtOrderSaveView();
            view.setId(order.getId());
            view.setOrderNum(order.getOrderNum());
            view.setPreId(order.getPreId());
            resp.setOrder(view);
            return resp;
        } catch (Exception e) {
            log.info("创建订单异常", e);
            return new MdtOrderSaveRespDto(Boolean.FALSE,
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
    public MdtOrderViewDto selectMdtOrderViewVo(String mdtOrderNum) {
        MdtOrder order = getOne(new LambdaQueryWrapper<MdtOrder>()
                .eq(MdtOrder::getOrderNum, mdtOrderNum)
                .last("limit 1"));
        if (order == null) {
            return null;
        }
        return toOrderViewVo(order);
    }

    @Override
    public IPage<MdtOrderViewDto> selectMdtOrderViewList(MdtOrderListRequestDto req) {
        IPage<MdtOrder> orders = page(new Page<>(req.getPage(), req.getLimit()),
                new LambdaQueryWrapper<MdtOrder>()
                        .eq(MdtOrder::getPatientUserId, AuthUtil.getLoginUserId())
                        .orderByDesc(MdtOrder::getPayTime));
        return orders.convert(entity -> {
            MdtOrderViewDto vo = new MdtOrderViewDto();
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

    private MdtOrderViewDto toOrderViewVo(MdtOrder order) {
        MdtOrderViewDto vo = new MdtOrderViewDto();
        BeanUtils.copyProperties(order, vo);

        HosPrescription prescription = hosPrescriptionService.getById(order.getPreId());
        if (prescription == null) {
            return vo;
        }

        MdtOrderViewPrescriptionDto prescriptionVo = new MdtOrderViewPrescriptionDto();
        BeanUtils.copyProperties(prescription, prescriptionVo);

        List<HosPrescriptionDrug> drugs = hosPrescriptionDrugService.list(
                new LambdaQueryWrapper<HosPrescriptionDrug>()
                        .eq(HosPrescriptionDrug::getHosPrescriptionId, prescription.getId()));

        List<MdtOrderViewPrescriptionDrugDto> drugVos = new ArrayList<>();
        for (HosPrescriptionDrug drug : drugs) {
            MdtOrderViewPrescriptionDrugDto drugVo = new MdtOrderViewPrescriptionDrugDto();
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

    // ============================== admin 阶段方法 ==============================

    @Override
    public PageResult<MdtOrderAdminDto> getPage(PageParam<MdtOrder> pageParam, SearchMdtOrderRequestDto request) {
        LambdaQueryWrapper<MdtOrder> queryWrapper = buildMdtOrderQueryWrapper(request);

        Page<MdtOrder> page = baseMapper.selectPage(pageParam, queryWrapper);

        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new PageResult<>();
        }

        List<MdtOrderAdminDto> voList = new ArrayList<>();
        List<String> preIdList = page.getRecords().stream().map(MdtOrder::getPreId).toList();
        Map<String, HosPrescription> prescriptionMap = hosPrescriptionService.listByIds(preIdList)
                .stream().collect(Collectors.toMap(HosPrescription::getId, s -> s));

        for (MdtOrder mdtOrder : page.getRecords()) {
            MdtOrderAdminDto vo = new MdtOrderAdminDto();
            BeanUtils.copyProperties(mdtOrder, vo);
            if (StringHandler.isNotBlank(mdtOrder.getPic())) {
                vo.setPicList(Arrays.asList(mdtOrder.getPic().split(",")));
            }

            HosPrescription prescription = prescriptionMap.get(mdtOrder.getPreId());
            if (prescription != null) {
                List<HosPrescriptionDrug> drugList = hosPrescriptionDrugService.list(new LambdaQueryWrapper<HosPrescriptionDrug>()
                        .eq(HosPrescriptionDrug::getHosPrescriptionId, prescription.getId()));
                List<PrescriptionDrugDTO> drugDTOList = drugList.stream().map(prescriptionDrug -> {
                    PrescriptionDrugDTO dto = new PrescriptionDrugDTO();
                    BeanUtil.copyProperties(prescriptionDrug, dto);
                    return dto;
                }).toList();
                vo.setDrugList(drugDTOList);

                HosPrescriptionInstruction prescriptionInstruction = prescriptionInstructionService.getOne(new LambdaQueryWrapper<HosPrescriptionInstruction>()
                        .eq(HosPrescriptionInstruction::getHosPrescriptionId, prescription.getId()));
                vo.setDoseNumber(prescriptionInstruction != null ? prescriptionInstruction.getDoseNumber() : null);
            }

            Drugstore drugstore = drugstoreService.getById(mdtOrder.getDrugstoreId());
            vo.setDrugstoreName(drugstore != null ? drugstore.getName() : null);

            voList.add(vo);
        }
        return new PageResult<>(voList, page.getTotal());
    }

    private static LambdaQueryWrapper<MdtOrder> buildMdtOrderQueryWrapper(SearchMdtOrderRequestDto request) {
        LambdaQueryWrapper<MdtOrder> queryWrapper = new LambdaQueryWrapper<MdtOrder>()
                .in(MdtOrder::getStatus, request.getStatusList())
                .like(StringUtil.isNotBlank(request.getHosSickName()), MdtOrder::getName, request.getHosSickName())
                .like(StringUtil.isNotBlank(request.getReceiveName()), MdtOrder::getReceiveName, request.getReceiveName())
                .eq(StringUtil.isNotBlank(request.getReceivePhone()), MdtOrder::getReceivePhone, request.getReceivePhone())
                .eq(StringUtil.isNotBlank(request.getOrderId()), MdtOrder::getId, request.getOrderId())
                .eq(StringUtil.isNotBlank(request.getPreId()), MdtOrder::getPreId, request.getPreId())
                .eq(StringUtil.isNotBlank(request.getDrugstoreId()), MdtOrder::getDrugstoreId, request.getDrugstoreId())
                .eq(StringUtil.isNotBlank(request.getStatus()), MdtOrder::getStatus, request.getStatus())
                .eq(request.getMedicineStatus() != null, MdtOrder::getMedicineStatusCode, request.getMedicineStatus())
                .ge(StringUtil.isNotBlank(request.getPayStartTime()), MdtOrder::getPayTime, request.getPayStartTime())
                .le(StringUtil.isNotBlank(request.getPayEndTime()), MdtOrder::getPayTime, request.getPayEndTime())
                .orderByDesc(MdtOrder::getPayTime);
        if (request.getHavePic() != null) {
            if (request.getHavePic()) {
                queryWrapper.isNotNull(MdtOrder::getPic);
            } else {
                queryWrapper.isNull(MdtOrder::getPic);
            }
        }
        return queryWrapper;
    }

    @Override
    public Long count(List<String> statusList) {
        return count(new LambdaQueryWrapper<MdtOrder>().in(MdtOrder::getStatus, statusList));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String printPdf(String orderId) {
        MdtOrder order = getById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        if (!"UNALLOCATED".equals(order.getStatus()) && !"ALLOCATING".equals(order.getStatus())) {
            throw new BizException("当前订单已完成调配，无需重复调配");
        }
        if ("UNALLOCATED".equals(order.getStatus())) {
            order.setStatus("ALLOCATING");
        }
        order.setPdfFlag("1");
        updateById(order);

        String prescriptionId = order.getPreId();
        if (prescriptionId == null || prescriptionId.isEmpty()) {
            throw new BizException("处方ID不存在");
        }

        String pdfUrl = pstGeneratePdfUrl + prescriptionId + "&type=pdf";
        String apiUrl = "https://" + pstEndpoint + pstGeneratePdfApi + "?url=" + pdfUrl;

        log.info("调用PST接口生成PDF, url: {}", apiUrl);
        String response = HttpUtil.getForObject(apiUrl, String.class);
        log.info("PST接口响应: {}", response);

        if (response == null || response.isEmpty()) {
            throw new BizException("PDF生成失败，PST接口无响应");
        }

        try {
            int code = JSONUtil.getIntValue(response, "code");
            if (code != 200) {
                String msg = JSONUtil.getString(response, "msg");
                throw new BizException(msg.isEmpty() ? "PDF生成失败" : msg);
            }
            String dataStr = JSONUtil.getString(response, "data");
            if (dataStr == null || dataStr.isEmpty()) {
                throw new BizException("PDF生成失败，未获取到数据");
            }
            String filePath = JSONUtil.getString(dataStr, "filePath");
            if (filePath == null || filePath.isEmpty()) {
                throw new BizException("PDF生成失败，未获取到文件路径");
            }
            return filePath;
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.error("PDF生成响应解析失败", e);
            throw new BizException("PDF生成失败，响应解析异常");
        }
    }

    @Override
    public boolean printExpress(String orderId) {
        MdtOrder order = getById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        if ("UNALLOCATED".equals(order.getStatus())) {
            order.setStatus("ALLOCATING");
        }
        order.setExpressFlag("1");
        return updateById(order);
    }

    @Override
    public boolean completeAllocation(String orderId) {
        MdtOrder order = getById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        if ("PENDING_SHIPMENT".equals(order.getStatus()) || "IN_TRANSIT".equals(order.getStatus()) || "COMPLETED".equals(order.getStatus())) {
            throw new BizException("当前订单已完成调配，无需重复操作。");
        }
        if ("REFUNDING".equals(order.getStatus()) || "REFUNDED".equals(order.getStatus())) {
            throw new BizException("当前订单已经发起退款，无法进行调配工作。");
        }
        order.setStatus("PENDING_SHIPMENT");
        return updateById(order);
    }

    @Override
    public boolean saveRemarkAndPic(String orderId, String remark, List<String> picList) {
        MdtOrder order = getById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        if (StringHandler.isNotBlank(remark)) {
            order.setRemark(remark);
        }
        if (picList != null && !picList.isEmpty()) {
            order.setPic(String.join(",", picList));
        }
        return updateById(order);
    }

    @Override
    public void export(SearchMdtOrderRequestDto orderRequest, HttpServletRequest request, HttpServletResponse response) {
        LambdaQueryWrapper<MdtOrder> queryWrapper = buildMdtOrderQueryWrapper(orderRequest);
        List<MdtOrder> orderList = baseMapper.selectList(queryWrapper);
        List<MdtOrderExportEntity> entityList = new ArrayList<>();

        List<String> preIdList = orderList.stream().map(MdtOrder::getPreId).toList();
        Map<String, HosPrescription> prescriptionMap = hosPrescriptionService.listByIds(preIdList)
                .stream().collect(Collectors.toMap(HosPrescription::getId, s -> s));

        for (MdtOrder order : orderList) {
            MdtOrderExportEntity exportEntity = new MdtOrderExportEntity();
            BeanUtils.copyProperties(order, exportEntity);
            exportEntity.setOrderId(order.getId());

            HosPrescription prescription = prescriptionMap.get(order.getPreId());
            if (prescription != null) {
                HosPrescriptionInstruction prescriptionInstruction = prescriptionInstructionService.getOne(new LambdaQueryWrapper<HosPrescriptionInstruction>()
                        .eq(HosPrescriptionInstruction::getHosPrescriptionId, prescription.getId()));
                exportEntity.setDoseNumber(prescriptionInstruction != null ? prescriptionInstruction.getDoseNumber() : null);
                exportEntity.setPrice(prescription.getTotalPrice());
            }

            entityList.add(exportEntity);
        }

        ExcelUtils.writeExcel(request, response, entityList, MdtOrderExportEntity.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")) + "订单数据表格.xlsx");
    }

    @Override
    public void drugExport(SearchMdtOrderRequestDto orderRequest, HttpServletRequest request, HttpServletResponse response) {
        LambdaQueryWrapper<MdtOrder> queryWrapper = buildMdtOrderQueryWrapper(orderRequest);
        List<MdtOrder> orderList = baseMapper.selectList(queryWrapper);

        List<String> preIdList = orderList.stream().map(MdtOrder::getPreId).toList();
        Map<String, HosPrescription> prescriptionMap = hosPrescriptionService.listByIds(preIdList)
                .stream().collect(Collectors.toMap(HosPrescription::getId, s -> s));

        List<MdtOrderDrugExportEntity> mdtOrderDrugExportEntityList = new ArrayList<>();
        for (MdtOrder order : orderList) {
            HosPrescription prescription = prescriptionMap.get(order.getPreId());
            if (prescription == null) {
                continue;
            }
            List<HosPrescriptionDrug> drugList = hosPrescriptionDrugService.list(new LambdaQueryWrapper<HosPrescriptionDrug>()
                    .eq(HosPrescriptionDrug::getHosPrescriptionId, prescription.getId()));
            mdtOrderDrugExportEntityList.addAll(drugList.stream().map(prescriptionDrug -> {
                MdtOrderDrugExportEntity entity = new MdtOrderDrugExportEntity();
                BeanUtil.copyProperties(prescriptionDrug, entity);
                entity.setUnit("g");
                return entity;
            }).toList());
        }
        ExcelUtils.writeExcel(request, response, mdtOrderDrugExportEntityList, MdtOrderDrugExportEntity.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")) + "订单用量数据表格.xlsx");
    }
}