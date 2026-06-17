package com.aihoo.api.patient.controller;


import com.aihoo.api.patient.request.MdtOrderListRequest;
import com.aihoo.api.patient.request.MdtOrderPayRequest;
import com.aihoo.api.patient.request.MdtOrderSaveRequest;
import com.aihoo.api.patient.vo.MdtOrderPageVo;
import com.aihoo.api.patient.vo.MdtOrderSaveRespVo;
import com.aihoo.api.patient.vo.MdtOrderViewVo;
import com.aihoo.common.BizResult;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.order.dto.MdtOrderListRequestDto;
import com.aihoo.domain.order.entity.MdtOrder;
import com.aihoo.domain.order.service.MdtOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "MdtOrderV2", description = "患者端-订单相关接口")
@RestController
@RequestMapping("/api/v2/mdtOrder")
public class MdtOrderController {

    @Autowired
    private MdtOrderService mdtOrderService;

    @GetMapping("/orderList")
    @Operation(summary = "查看全部会诊订单")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, MdtOrderPageVo.class},
                            description = "查看全部会诊订单"
                    )
            )
    )
    public BizResult<PageResult<com.aihoo.domain.order.dto.MdtOrderPageVo>> pageOrderListByPatientUserId(@Parameter PageParam<MdtOrder> pageParam) {
        return BizResult.success(mdtOrderService.pageOrderListByPatientUserId(pageParam));
    }

    @PostMapping
    @Operation(summary = "创建订单")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, MdtOrderSaveRespVo.class},
                            description = "创建订单"
                    )
            )
    )
    public BizResult<com.aihoo.domain.order.dto.MdtOrderSaveRespVo> saveOrder(@RequestBody MdtOrderSaveRequest order) {
        MdtOrder mdtOrder = new MdtOrder();
        BeanUtils.copyProperties(order, mdtOrder);
        return BizResult.success(mdtOrderService.saveOrder(mdtOrder));
    }

    @PostMapping("/pay")
    @Operation(summary = "支付订单")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Boolean.class},
                            description = "支付订单"
                    )
            )
    )
    public BizResult<Boolean> payOrder(@ParameterObject MdtOrderPayRequest req) {
        mdtOrderService.payOrder(req.getOrderNum());
        return BizResult.success(Boolean.TRUE);
    }

    @GetMapping("/view")
    @Operation(summary = "查询订单")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, MdtOrderViewVo.class},
                            description = "根据id查询订单"
                    )
            )
    )
    public BizResult<MdtOrderViewVo> selectOrderById(@Parameter(name = "orderNum", description = "订单号", required = true)
                                                     @RequestParam String orderNum) {
        return BizResult.success(toVo(mdtOrderService.selectMdtOrderViewVo(orderNum)));
    }

    @GetMapping
    @Operation(summary = "查看自己的购药订单")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, IPage.class, MdtOrderViewVo.class},
                            description = "查看全部会诊订单"
                    )
            )
    )
    public BizResult<IPage<com.aihoo.domain.order.dto.MdtOrderViewVo>> list(@ParameterObject MdtOrderListRequest req) {
        MdtOrderListRequestDto dto = new MdtOrderListRequestDto();
        BeanUtils.copyProperties(req, dto);
        return BizResult.success(mdtOrderService.selectMdtOrderViewList(dto));
    }

    private MdtOrderViewVo toVo(com.aihoo.domain.order.dto.MdtOrderViewVo dto) {
        if (dto == null) return null;
        MdtOrderViewVo vo = new MdtOrderViewVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }
}
