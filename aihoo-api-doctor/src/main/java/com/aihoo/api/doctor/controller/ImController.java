package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.request.ImSendGroupMsgRequest;
import com.aihoo.api.doctor.request.ImWithdrawMsgRequest;
import com.aihoo.common.BizResult;
import com.aihoo.domain.im.dto.ImMsgContentDto;
import com.aihoo.domain.im.dto.ImMsgDto;
import com.aihoo.domain.im.dto.ImSendGroupMsgRequestDto;
import com.aihoo.domain.im.dto.ImWithdrawMsgRequestDto;
import com.aihoo.domain.im.entity.ImMsg;
import com.aihoo.domain.im.entity.ImMsgContent;
import com.aihoo.domain.im.service.ImMsgContentService;
import com.aihoo.domain.im.service.ImMsgService;
import com.aihoo.domain.im.service.ImService;
import com.aihoo.domain.visit.entity.HosPrescription;
import com.aihoo.domain.visit.entity.HosVisit;
import com.aihoo.domain.visit.service.HosPrescriptionService;
import com.aihoo.domain.visit.service.HosVisitService;
import com.aihoo.security.AuthUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医生端-im 相关接口（迁自 doctor-api: ImController）。
 */
@Tag(name = "ImV2", description = "医生端-im相关接口")
@Slf4j
@RestController
@RequestMapping("/api/v2/im")
@RequiredArgsConstructor
public class ImController {

    private final ImMsgService imMsgService;
    private final ImMsgContentService imMsgContentService;
    private final HosPrescriptionService hosPrescriptionService;
    private final HosVisitService hosVisitService;
    private final ImService iMService;

    @GetMapping("/visit")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, ImMsgDto.class},
                            description = "获取聊天记录"
                    )
            )
    )
    public BizResult<List<ImMsgDto>> findImMsgByVisitNo(
            @Parameter(name = "visitNo", description = "问诊卡no", example = "V20210106182014643")
            String visitNo) {
        List<ImMsg> msgList = imMsgService.list(new LambdaQueryWrapper<ImMsg>()
                .eq(ImMsg::getOrderNum, visitNo)
                .and(m -> m.eq(ImMsg::getToAccount, "DOCTOR_" + AuthUtil.getLoginUserId())
                        .or()
                        .isNull(ImMsg::getToAccount))
                .orderByDesc(ImMsg::getCreateTime));
        List<ImMsgDto> msgVos = msgList.stream().map(msg -> {
            ImMsgDto msgVo = new ImMsgDto();
            BeanUtils.copyProperties(msg, msgVo);
            List<ImMsgContent> msgContent = imMsgContentService.list(new LambdaQueryWrapper<ImMsgContent>()
                    .eq(ImMsgContent::getImMsgId, msg.getId()));
            if (CollectionUtils.isNotEmpty(msgContent)) {
                List<ImMsgContentDto> contentVos = Lists.newArrayList();
                for (ImMsgContent imMsgContent : msgContent) {
                    ImMsgContentDto imMsgContentVo = new ImMsgContentDto();
                    imMsgContentVo.setImMsgId(imMsgContent.getImMsgId());
                    imMsgContentVo.setMsgType(imMsgContent.getMsgType());
                    JSONObject content = JSONObject.parseObject(imMsgContent.getMsgContent());
                    //后续拆分出去， 读取开方状态
                    if (msg.getLoadParam() != null && msg.getLoadParam().equals(1)
                            && "TIMCustomElem".equals(imMsgContent.getMsgType())) {
                        JSONObject dataJson = content.getJSONObject("Data");
                        if (dataJson != null && "savePrescription".equals(dataJson.getString("type"))) {
                            String hosPrescriptionId = dataJson.getJSONObject("data").getString("hosPrescriptionId");
                            HosPrescription prescription = hosPrescriptionService.getById(hosPrescriptionId);
                            if (prescription != null) {
                                dataJson.getJSONObject("data").put("prescriptionStatus", prescription.getStatus());
                                dataJson.getJSONObject("data").put("confirmedStatus", prescription.getConfirmedStatus());
                                content.put("Data", dataJson.toJSONString());
                            }
                        }
                        // 动态更新问诊订单卡片状态
                        String msgType = dataJson.getString("msgType");
                        if ("VisitOrderCard".equals(msgType) || "RevisitOrderCard".equals(msgType)) {
                            String orderNum = dataJson.getString("orderNum");
                            if (orderNum != null && !orderNum.isEmpty()) {
                                HosVisit visit = hosVisitService.getOne(new LambdaQueryWrapper<HosVisit>()
                                        .eq(HosVisit::getOrderNum, orderNum));
                                if (visit != null) {
                                    dataJson.put("status", visit.getStatus());
                                    content.put("Data", dataJson.toJSONString());
                                }
                            }
                        }
                    }
                    imMsgContentVo.setMsgContent(content.toJSONString());
                    imMsgContentVo.setMsgTypeName(imMsgContent.getMsgTypeName());
                    contentVos.add(imMsgContentVo);
                }
                msgVo.setMsgContents(contentVos);
            }
            return msgVo;
        }).toList();

        return BizResult.success(msgVos);
    }

    @GetMapping("/perrRead")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, Boolean.class},
                            description = "更新已读状态"
                    )
            )
    )
    public BizResult<Boolean> peerRead(@Parameter(name = "visitNo", description = "问诊卡no", example = "V20210106182014643")
                                       @RequestParam String visitNo) {
        List<ImMsg> list = imMsgService.list(new LambdaQueryWrapper<ImMsg>()
                .eq(ImMsg::getOrderNum, visitNo)
                .eq(ImMsg::getDoctorPeerReadStatus, 0));
        list.forEach(msg -> msg.setDoctorPeerReadStatus(1));
        imMsgService.updateBatchById(list);
        return BizResult.success(Boolean.TRUE);
    }

    @PostMapping("/withdraw")
    @Operation(summary = "撤回消息")
    public BizResult<Boolean> withdrawMsg(@RequestBody ImWithdrawMsgRequest imWithdrawMsgRequest) {
        ImWithdrawMsgRequestDto dto = new ImWithdrawMsgRequestDto();
        BeanUtils.copyProperties(imWithdrawMsgRequest, dto);
        return BizResult.success(iMService.withdrawMsg(dto));
    }

    @PostMapping("/msg")
    @Operation(summary = "发送消息")
    public BizResult<Boolean> sendGroupMsg(@RequestBody ImSendGroupMsgRequest req) {
        req.setMsgType(1);
        ImSendGroupMsgRequestDto dto = new ImSendGroupMsgRequestDto();
        BeanUtils.copyProperties(req, dto);
        return BizResult.success(iMService.sendGroupMsg(dto));
    }
}
