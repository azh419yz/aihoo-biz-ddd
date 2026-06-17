package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.controller.vo.ImMsgContentVo;
import com.aihoo.api.doctor.controller.vo.ImMsgVo;
import com.aihoo.common.BizResult;
import com.aihoo.domain.im.dto.ImSendGroupMsgRequest;
import com.aihoo.domain.im.dto.ImWithdrawMsgRequest;
import com.aihoo.domain.im.model.entity.ImMsg;
import com.aihoo.domain.im.model.entity.ImMsgContent;
import com.aihoo.domain.im.service.ImMsgContentService;
import com.aihoo.domain.im.service.ImMsgService;
import com.aihoo.domain.im.service.ImService;
import com.aihoo.domain.prescription.model.entity.HosPrescription;
import com.aihoo.domain.prescription.service.HosPrescriptionService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/12 22:11
 */
@Tag(name = "ImV2", description = "医生端-im相关接口")
@Slf4j
@RestController
@RequestMapping("/api/v2/im")
public class ImController {
    private final ImMsgService imMsgService;
    private final ImMsgContentService imMsgContentService;
    private final HosPrescriptionService hosPrescriptionService;
    private final ImService iMService;

    public ImController(ImMsgService imMsgService, ImMsgContentService imMsgContentService, HosPrescriptionService hosPrescriptionService, ImService iMService) {
        this.imMsgService = imMsgService;
        this.imMsgContentService = imMsgContentService;
        this.hosPrescriptionService = hosPrescriptionService;
        this.iMService = iMService;
    }

    @GetMapping("/visit")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, ImMsgVo.class},
                            description = "获取聊天记录"
                    )
            )
    )
    public BizResult<List<ImMsgVo>> findImMsgByVisitNo(
            @Parameter(name = "visitNo", description = "问诊卡no", example = "V20210106182014643")
            String visitNo) {
        List<ImMsg> msgList = imMsgService.list(new LambdaQueryWrapper<ImMsg>()
                .eq(ImMsg::getOrderNum, visitNo)
                .and(m -> m.eq(
                                ImMsg::getToAccount, "DOCTOR_" + AuthUtil.getLoginUserId())
                        .or()
                        .isNull(ImMsg::getToAccount))
                .orderByDesc(ImMsg::getCreateTime));
        List<ImMsgVo> msgVos = msgList.stream().map(msg -> {
            ImMsgVo msgVo = new ImMsgVo();
            msgVo.setMsgRandom(msg.getMsgRandom());
            msgVo.setMsgKey(msg.getMsgKey());
            msgVo.setOrderNum(msg.getOrderNum());
            msgVo.setMsgSeq(msg.getMsgSeq());
            msgVo.setMsgTime(msg.getMsgTime());
            msgVo.setMsgType(msg.getMsgType());
            msgVo.setOrderType(msg.getOrderType());
            msgVo.setErrorInfo(msg.getErrorInfo());
            msgVo.setToAccount(msg.getToAccount());
            msgVo.setFromAccount(msg.getFromAccount());
            msgVo.setSendMsgResult(msg.getSendMsgResult());
            msgVo.setCreateTimeStr(msg.getCreateTimeStr());
            List<ImMsgContent> msgContent = imMsgContentService.list(new LambdaQueryWrapper<ImMsgContent>()
                    .eq(ImMsgContent::getImMsgId, msg.getId()));
            if (CollectionUtils.isNotEmpty(msgContent)) {
                List<ImMsgContentVo> contentVos = Lists.newArrayList();
                for (ImMsgContent imMsgContent : msgContent) {
                    ImMsgContentVo imMsgContentVo = new ImMsgContentVo();
                    imMsgContentVo.setImMsgId(imMsgContent.getImMsgId());
                    imMsgContentVo.setMsgType(imMsgContent.getMsgType());
                    JSONObject content = JSONObject.parseObject(imMsgContent.getMsgContent());
                    //后续拆分出去， 读取开方状态
                    if (msg.getLoadParam().equals(1) && imMsgContent.getMsgType().equals("TIMCustomElem")) {
                        JSONObject dataJson = content.getJSONObject("Data");
                        if ("savePrescription".equals(dataJson.getString("type"))) {
                            String hosPrescriptionId = dataJson.getJSONObject("data").getString("hosPrescriptionId");
                            HosPrescription prescription = hosPrescriptionService.getById(hosPrescriptionId);
                            dataJson.getJSONObject("data").put("prescriptionStatus", prescription.getStatus());
                            content.put("Data", dataJson.toJSONString());
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
                .eq(ImMsg::getDoctorPeerReadStatus, "0"));
        list.forEach(msg -> msg.setDoctorPeerReadStatus("1"));
        imMsgService.updateBatchById(list);
        return BizResult.success(Boolean.TRUE);
    }

    @PostMapping("/withdraw")
    @Operation(summary = "撤回消息")
    public BizResult<Boolean> withdrawMsg(ImWithdrawMsgRequest imWithdrawMsgRequest) {
        return BizResult.success(iMService.withdrawMsg(imWithdrawMsgRequest));
    }

    @PostMapping("/msg")
    @Operation(summary = "发送消息")
    public BizResult<Boolean> sendGroupMsg(@RequestBody ImSendGroupMsgRequest req) {
        req.setMsgType(1);
        return BizResult.success(iMService.sendGroupMsg(req));
    }
}
