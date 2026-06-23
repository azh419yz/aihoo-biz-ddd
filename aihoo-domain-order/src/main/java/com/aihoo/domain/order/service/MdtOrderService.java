package com.aihoo.domain.order.service;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.order.dto.MdtOrderAdminDto;
import com.aihoo.domain.order.dto.MdtOrderListRequestDto;
import com.aihoo.domain.order.dto.MdtOrderPageRespDto;
import com.aihoo.domain.order.dto.MdtOrderSaveRespDto;
import com.aihoo.domain.order.dto.MdtOrderViewDto;
import com.aihoo.domain.order.dto.SearchMdtOrderRequestDto;
import com.aihoo.domain.order.entity.MdtOrder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface MdtOrderService extends IService<MdtOrder> {
    long countOrderByPatientUserId(String patientUserId);

    PageResult<MdtOrderPageRespDto> pageOrderListByPatientUserId(PageParam<MdtOrder> pageParam);

    MdtOrderSaveRespDto saveOrder(MdtOrder order);

    boolean payOrder(String orderNum);

    MdtOrderViewDto selectMdtOrderViewVo(String mdtOrderNum);

    IPage<MdtOrderViewDto> selectMdtOrderViewList(MdtOrderListRequestDto req);

    PageResult<MdtOrderAdminDto> getPage(PageParam<MdtOrder> pageParam, SearchMdtOrderRequestDto request);

    Long count(List<String> statusList);

    String printPdf(String orderId);

    boolean printExpress(String orderId);

    boolean completeAllocation(String orderId);

    boolean saveRemarkAndPic(String orderId, String remark, List<String> picList);

    void export(SearchMdtOrderRequestDto orderRequest, HttpServletRequest request, HttpServletResponse response);

    void drugExport(SearchMdtOrderRequestDto orderRequest, HttpServletRequest request, HttpServletResponse response);
}