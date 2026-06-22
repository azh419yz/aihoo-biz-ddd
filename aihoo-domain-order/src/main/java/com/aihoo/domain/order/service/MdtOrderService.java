package com.aihoo.domain.order.service;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.order.dto.MdtOrderAdminVo;
import com.aihoo.domain.order.dto.MdtOrderListRequestDto;
import com.aihoo.domain.order.dto.MdtOrderPageVo;
import com.aihoo.domain.order.dto.MdtOrderSaveRespVo;
import com.aihoo.domain.order.dto.MdtOrderViewVo;
import com.aihoo.domain.order.dto.SearchMdtOrderRequestDto;
import com.aihoo.domain.order.entity.MdtOrder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface MdtOrderService extends IService<MdtOrder> {
    long countOrderByPatientUserId(String patientUserId);

    PageResult<MdtOrderPageVo> pageOrderListByPatientUserId(PageParam<MdtOrder> pageParam);

    MdtOrderSaveRespVo saveOrder(MdtOrder order);

    boolean payOrder(String orderNum);

    MdtOrderViewVo selectMdtOrderViewVo(String mdtOrderNum);

    IPage<MdtOrderViewVo> selectMdtOrderViewList(MdtOrderListRequestDto req);

    PageResult<MdtOrderAdminVo> getPage(PageParam<MdtOrder> pageParam, SearchMdtOrderRequestDto request);

    Long count(List<String> statusList);

    String printPdf(String orderId);

    boolean printExpress(String orderId);

    boolean completeAllocation(String orderId);

    boolean saveRemarkAndPic(String orderId, String remark, List<String> picList);

    void export(SearchMdtOrderRequestDto orderRequest, HttpServletRequest request, HttpServletResponse response);

    void drugExport(SearchMdtOrderRequestDto orderRequest, HttpServletRequest request, HttpServletResponse response);
}