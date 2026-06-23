package com.aihoo.api.patient.controller;

import com.aihoo.api.patient.request.ImContactRequest;
import com.aihoo.api.patient.request.ImSendMsgRequest;
import com.aihoo.api.patient.request.ImWithdrawMsgRequest;
import com.aihoo.common.BaseController;
import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import com.aihoo.domain.doctor.entity.DoctorUser;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.im.dto.*;
import com.aihoo.domain.im.entity.ImMsg;
import com.aihoo.domain.im.entity.ImMsgContent;
import com.aihoo.domain.im.enums.ImServiceApiEnum;
import com.aihoo.domain.im.service.ImMsgContentService;
import com.aihoo.domain.im.service.ImMsgService;
import com.aihoo.domain.im.service.ImService;
import com.aihoo.domain.patient.entity.PatientUser;
import com.aihoo.domain.patient.service.PatientUserService;
import com.aihoo.domain.visit.entity.HosPrescription;
import com.aihoo.domain.visit.service.HosPrescriptionService;
import com.aihoo.domain.visit.service.HosVisitService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "ImV2", description = "患者端-im相关接口")
@Slf4j
@RestController
@RequestMapping("/api/v2/im")
public class ImController extends BaseController {

    @Resource
    private ImService imService;

    @Resource
    private DoctorUserService doctorUserService;

    @Resource
    private PatientUserService patientUserService;

    @Autowired
    private ImMsgService imMsgService;

    @Autowired
    private ImMsgContentService imMsgContentService;
    @Autowired
    private HosPrescriptionService hosPrescriptionService;
    @Autowired
    private HosVisitService hosVisitService;

    @PostMapping("/sendMsg")
    @Operation(summary = "发送信息")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, ImSendMsgRespDto.class},
                            description = "返回信息发送相关结果"
                    )
            )
    )
    public BizResult<ImSendMsgRespDto> sendMsg(
            @RequestBody
            @Parameter(
                    description = "发送消息请求参数",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ImSendMsgRequest.class)))
            ImSendMsgRequest imSendMsgRequest) {
        try {
            com.aihoo.domain.im.dto.ImSendMsgReqDto dto = new com.aihoo.domain.im.dto.ImSendMsgReqDto();
            BeanUtils.copyProperties(imSendMsgRequest, dto);
            ImSendMsgRespDto imResponse = imService.callTim(ImServiceApiEnum.SEND_MSG, dto);
            if (imResponse != null && "OK".equals(imResponse.getActionStatus())) {
                hosVisitService.updateMsg(imSendMsgRequest.getVisitNo(), imSendMsgRequest.getMsgContent());
            }

            return BizResult.success(imResponse);
        } catch (Exception e) {
            log.error("{}接口出错", ImServiceApiEnum.SEND_MSG.getApiName(), e);
            return BizResult.fail(BizResultCode.INTERNAL_ERROR, ImServiceApiEnum.SEND_MSG.getApiName() + "接口出错");
        }
    }

    @PostMapping("/modifyMsg")
    @Operation(summary = "修改信息")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, ImSendMsgRespDto.class},
                            description = "修改信息"
                    )
            )
    )
    public BizResult<ImSendMsgRespDto> modifyMsg(@RequestBody ImSendMsgRequest imSendMsgRequest) {
        try {
            com.aihoo.domain.im.dto.ImSendMsgReqDto dto = new com.aihoo.domain.im.dto.ImSendMsgReqDto();
            BeanUtils.copyProperties(imSendMsgRequest, dto);
            ImSendMsgRespDto imResponse = imService.callTim(ImServiceApiEnum.SEND_MSG, dto);
            if (imResponse != null && "OK".equals(imResponse.getActionStatus())) {
                hosVisitService.updateMsg(imSendMsgRequest.getVisitNo(), imSendMsgRequest.getMsgContent());
            }
            return BizResult.success(imResponse);
        } catch (Exception e) {
            log.error("{}接口出错", ImServiceApiEnum.MODIFY_C2C_MSG.getApiName(), e);
            return BizResult.fail(BizResultCode.INTERNAL_ERROR, ImServiceApiEnum.MODIFY_C2C_MSG.getApiName() + "接口出错");
        }
    }

    @PostMapping("/recent")
    @Operation(summary = "患者的会话列表")
    @ApiResponse(
            responseCode = "200",
            description = "成功",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            oneOf = {BizResult.class, ImRecentContactDto.class},
                            description = "患者的会话列表"
                    )
            )
    )
    public BizResult<ImRecentContactDto> recentcontact(@Validated @RequestBody ImContactRequest imContactRequest) {
        String apiName = "v4/recentcontact/get_list";
        try {
            ImContactRequestDto dto = new ImContactRequestDto();
            BeanUtils.copyProperties(imContactRequest, dto);
            String payLoad = JSON.toJSONString(dto);
            String apiResponse = imService.callTimV1(apiName, payLoad);
            ImRecentContactDto recentContact = buildApiResponse(apiResponse);
            fillNickNameAndHeadImg(recentContact);
            log.info("\n\n{}\n{}\n{}\n\n", apiName, payLoad, apiResponse);
            return BizResult.success(recentContact);
        } catch (Exception e) {
            log.error(apiName + "接口出错", e);
            return BizResult.fail(BizResultCode.INTERNAL_ERROR, apiName + "接口出错");
        }
    }

    private ImRecentContactDto buildApiResponse(String apiResponse) {
        return JSON.parseObject(apiResponse, ImRecentContactDto.class);
    }

    private void fillNickNameAndHeadImg(ImRecentContactDto recentContact) {
        List<ImSessionItemDto> sessionItemList = recentContact.getSessionItem();
        if (sessionItemList == null || sessionItemList.isEmpty()) {
            return;
        }

        List<String> doctorIds = new ArrayList<String>();
        List<String> patientIds = new ArrayList<String>();
        for (ImSessionItemDto sessionItem : sessionItemList) {
            String userId = sessionItem.getTo_Account();
            if (StringUtils.startsWith(userId, "DOCTOR_")) {
                doctorIds.add(StringUtils.substringAfter(userId, "DOCTOR_"));
            } else if (StringUtils.startsWith(userId, "PATIENT_")) {
                patientIds.add(StringUtils.substringAfter(userId, "PATIENT_"));
            }
        }

        if (!doctorIds.isEmpty()) {
            List<DoctorUser> doctorUserList = doctorUserService.listByIds(doctorIds);
            for (ImSessionItemDto sessionItem : sessionItemList) {
                fillSessionItemByDoctor(sessionItem, doctorUserList);
            }
        }
        if (!patientIds.isEmpty()) {
            List<PatientUser> patientUserList = patientUserService.listByIds(patientIds);
            for (ImSessionItemDto sessionItem : sessionItemList) {
                fillSessionItemByPatient(sessionItem, patientUserList);
            }
        }
    }

    private void fillSessionItemByDoctor(ImSessionItemDto sessionItem, List<DoctorUser> doctorUserList) {
        for (DoctorUser doctorUser : doctorUserList) {
            if (StringUtils.equals(sessionItem.getTo_Account(), "DOCTOR_" + doctorUser.getId())) {
                sessionItem.setUserName(doctorUser.getName());
                sessionItem.setHeadImg(doctorUser.getHeadImg());
            }
        }
    }

    private void fillSessionItemByPatient(ImSessionItemDto sessionItem, List<PatientUser> patientUserList) {
        for (PatientUser patientUser : patientUserList) {
            if (StringUtils.equals(sessionItem.getTo_Account(), "PATIENT_" + patientUser.getId())) {
                sessionItem.setUserName(patientUser.getName());
                sessionItem.setHeadImg(patientUser.getHeadImg());
            }
        }
    }

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
                .orderByDesc(ImMsg::getCreateTime));
        List<ImMsgDto> msgVos = msgList.stream().map(msg -> {
            ImMsgDto msgVo = new ImMsgDto();
            msgVo.setMsgRandom(msg.getMsgRandom());
            msgVo.setMsgKey(msg.getMsgKey());
            msgVo.setOrderNum(msg.getOrderNum());
            msgVo.setMsgSeq(msg.getMsgSeq());
            msgVo.setMsgType(msg.getMsgType());
            msgVo.setMsgTime(msg.getMsgTime());
            msgVo.setOrderType(msg.getOrderType());
            msgVo.setErrorInfo(msg.getErrorInfo());
            msgVo.setToAccount(msg.getToAccount());
            msgVo.setFromAccount(msg.getFromAccount());
            msgVo.setSendMsgResult(msg.getSendMsgResult());
            msgVo.setCreateTimeStr(msg.getCreateTimeStr());
            QueryWrapper<ImMsgContent> msgContentQueryWrapper = new QueryWrapper<>();
            msgContentQueryWrapper.eq("im_msg_id", msg.getId());
            List<ImMsgContent> msgContent = imMsgContentService.list(msgContentQueryWrapper);
            if (CollectionUtils.isNotEmpty(msgContent)) {
                List<ImMsgContentDto> contentVos = Lists.newArrayList();
                for (ImMsgContent imMsgContent : msgContent) {
                    ImMsgContentDto imMsgContentVo = new ImMsgContentDto();
                    imMsgContentVo.setImMsgId(imMsgContent.getImMsgId());
                    imMsgContentVo.setMsgType(imMsgContent.getMsgType());
                    imMsgContentVo.setMsgContent(imMsgContent.getMsgContent());
                    JSONObject content = JSONObject.parseObject(imMsgContent.getMsgContent());
                    if (msg.getLoadParam() != null && msg.getLoadParam().equals(1)
                            && "TIMCustomElem".equals(imMsgContent.getMsgType())) {
                        JSONObject dataJson = content.getJSONObject("Data");
                        if (dataJson != null && "savePrescription".equals(dataJson.getString("type"))) {
                            String hosPrescriptionId = dataJson.getJSONObject("data").getString("hosPrescriptionId");
                            HosPrescription prescription = hosPrescriptionService.getById(hosPrescriptionId);
                            if (prescription != null) {
                                dataJson.getJSONObject("data").put("prescriptionStatus", prescription.getStatus());
                                content.put("Data", dataJson.toJSONString());
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
                .eq(ImMsg::getSickPeerReadStatus, 0));
        list.forEach(msg -> msg.setSickPeerReadStatus(1));
        imMsgService.updateBatchById(list);
        return BizResult.success(Boolean.TRUE);
    }

    @PostMapping("/withdraw")
    @Operation(summary = "撤回消息")
    public BizResult<Boolean> withdrawMsg(@RequestBody ImWithdrawMsgRequest imWithdrawMsgRequest) {
        ImWithdrawMsgRequestDto dto = new ImWithdrawMsgRequestDto();
        BeanUtils.copyProperties(imWithdrawMsgRequest, dto);
        return BizResult.success(imMsgService.withdrawMsg(dto));
    }
}
