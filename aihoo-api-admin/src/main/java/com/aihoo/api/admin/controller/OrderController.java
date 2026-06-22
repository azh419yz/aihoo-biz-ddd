package com.aihoo.api.admin.controller;

import com.aihoo.api.admin.request.SaveRemarkAndPicRequest;
import com.aihoo.api.admin.request.SearchMdtOrderRequest;
import com.aihoo.api.admin.vo.MdtOrderVo;
import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.order.dto.MdtOrderAdminVo;
import com.aihoo.domain.order.dto.SearchMdtOrderRequestDto;
import com.aihoo.domain.order.entity.MdtOrder;
import com.aihoo.domain.order.service.MdtOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname OrderController
 * @Description 订单管理表
 * @Date 2020/9/22 16:28
 * @Created by ad
 */
@Tag(name = "Order", description = "运营端-订单相关接口")
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final MdtOrderService mdtOrderService;


    @GetMapping("/unallocatedList")
    @Operation(summary = "待调配订单列表")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, PageResult.class, MdtOrderVo.class},
                            description = "药房列表"
                    )
            )
    )
    public BizResult<PageResult<MdtOrderVo>> unallocatedList(@ParameterObject PageParam<MdtOrder> pageParam,
                                                             @ParameterObject SearchMdtOrderRequest request) {
        if (request == null) {
            request = new SearchMdtOrderRequest();
        }
        request.setStatusList(List.of("UNALLOCATED", "ALLOCATING"));
        PageResult<MdtOrderAdminVo> page = mdtOrderService.getPage(pageParam, toDto(request));
        return BizResult.success(toVoPage(page));
    }

    @GetMapping("/allocatedList")
    @Operation(summary = "已调配订单列表")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, PageResult.class, MdtOrderVo.class},
                            description = "药房列表"
                    )
            )
    )
    public BizResult<PageResult<MdtOrderVo>> allocatedList(@ParameterObject PageParam<MdtOrder> pageParam,
                                                           @ParameterObject SearchMdtOrderRequest request) {
        if (request == null) {
            request = new SearchMdtOrderRequest();
        }
        request.setStatusList(List.of("PENDING_SHIPMENT", "IN_TRANSIT", "COMPLETED", "REFUNDING", "REFUNDED"));
        PageResult<MdtOrderAdminVo> page = mdtOrderService.getPage(pageParam, toDto(request));
        return BizResult.success(toVoPage(page));
    }

    @GetMapping("/unallocatedCount")
    @Operation(summary = "待调配数量")
    public BizResult<Long> unallocatedCount() {
        return BizResult.success(mdtOrderService.count(List.of("UNALLOCATED", "ALLOCATING")));
    }

    @GetMapping("/allocatedCount")
    @Operation(summary = "已调配数量")
    public BizResult<Long> allocatedCount() {
        return BizResult.success(mdtOrderService.count(List.of("PENDING_SHIPMENT", "IN_TRANSIT", "COMPLETED", "REFUNDING", "REFUNDED")));
    }

    @GetMapping("/printPdf")
    @Operation(summary = "打印PDF")
    public BizResult<String> printPdf(@ParameterObject String orderId) {
        return BizResult.success("打印成功", mdtOrderService.printPdf(orderId));
    }

    @GetMapping("/printExpress")
    @Operation(summary = "打印快递标签")
    public BizResult<Void> printExpress(@ParameterObject String orderId) {
        boolean result = mdtOrderService.printExpress(orderId);
        return result ? BizResult.success("打印成功") : BizResult.fail(BizResultCode.INTERNAL_ERROR, "打印失败");
    }

    @GetMapping("/completeAllocation")
    @Operation(summary = "完成调配")
    public BizResult<Void> completeAllocation(@ParameterObject String orderId) {
        boolean result = mdtOrderService.completeAllocation(orderId);
        return result ? BizResult.success("操作成功") : BizResult.fail(BizResultCode.INTERNAL_ERROR, "操作失败");
    }

    @PostMapping("/saveRemarkAndPic")
    @Operation(summary = "保存备注和图片")
    public BizResult<Void> saveRemarkAndPic(@Validated @RequestBody SaveRemarkAndPicRequest request) {
        boolean result = mdtOrderService.saveRemarkAndPic(request.getOrderId(), request.getRemark(), request.getPicList());
        return result ? BizResult.success("保存成功") : BizResult.fail(BizResultCode.INTERNAL_ERROR, "保存失败");
    }

    @GetMapping("/export")
    public void export(@ParameterObject SearchMdtOrderRequest orderRequest,
                       HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        if (orderRequest == null) {
            orderRequest = new SearchMdtOrderRequest();
        }
        orderRequest.setStatusList(List.of("PENDING_SHIPMENT", "IN_TRANSIT", "COMPLETED", "REFUNDING", "REFUNDED"));
        mdtOrderService.export(toDto(orderRequest), request, response);

    }

    @GetMapping("/drugExport")
    public void drugExport(@ParameterObject SearchMdtOrderRequest orderRequest,
                           HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        if (orderRequest == null) {
            orderRequest = new SearchMdtOrderRequest();
        }
        orderRequest.setStatusList(List.of("PENDING_SHIPMENT", "IN_TRANSIT", "COMPLETED", "REFUNDING", "REFUNDED"));
        mdtOrderService.drugExport(toDto(orderRequest), request, response);
    }

    private SearchMdtOrderRequestDto toDto(SearchMdtOrderRequest request) {
        SearchMdtOrderRequestDto dto = new SearchMdtOrderRequestDto();
        BeanUtils.copyProperties(request, dto);
        return dto;
    }

    private PageResult<MdtOrderVo> toVoPage(PageResult<MdtOrderAdminVo> page) {
        if (page == null || page.getData() == null) {
            return new PageResult<>();
        }
        List<MdtOrderVo> voList = page.getData().stream().map(admin -> {
            MdtOrderVo vo = new MdtOrderVo();
            BeanUtils.copyProperties(admin, vo);
            return vo;
        }).toList();
        return new PageResult<>(voList, page.getCount());
    }

}