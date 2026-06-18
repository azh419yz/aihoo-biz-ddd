package com.aihoo.domain.im.service.impl;

import cn.hutool.http.HttpRequest;
import com.aihoo.domain.im.dto.ImSendGroupMsgRequestDto;
import com.aihoo.domain.im.dto.ImSendMsgRequest;
import com.aihoo.domain.im.dto.ImSendMsgRespVo;
import com.aihoo.domain.im.dto.ImWithdrawMsgRequestDto;
import com.aihoo.domain.im.entity.ImMsg;
import com.aihoo.domain.im.entity.ImMsgContent;
import com.aihoo.domain.im.enums.ImServiceApiEnum;
import com.aihoo.domain.im.service.ImMsgContentService;
import com.aihoo.domain.im.service.ImMsgService;
import com.aihoo.domain.im.service.ImService;
import com.aihoo.domain.im.util.TencentImGroupUtil;
import com.aihoo.domain.visit.entity.HosVisit;
import com.aihoo.domain.visit.service.HosVisitService;
import com.aihoo.properties.TencentProperties;
import com.aihoo.util.ImUtils;
import com.aihoo.util.UUIDUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PropertyPlaceholderHelper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Properties;

@Slf4j
public abstract class AbstractImService implements ImService {

    @Resource
    private TencentProperties tencentProperties;
    @Resource
    private HosVisitService hosVisitService;
    @Resource
    private TencentImGroupUtil tencentImGroupUtil;
    @Resource
    private ImMsgService imMsgService;
    @Resource
    private ImMsgContentService imMsgContentService;

    @Override
    public ImSendMsgRespVo callTim(ImServiceApiEnum api, ImSendMsgRequest imSendMsgRequest) {
        String random = UUIDUtil.randomUUID32();
        String host = "https://console.tim.qq.com/" + api.getApiName() + "?sdkappid=" + tencentProperties.getSdkappid()
                + "&identifier=" + tencentProperties.getAdminidentifier() + "&usersig=" + genAdminUserSign()
                + "&random=" + random + "&contenttype=json";

        Properties props = new Properties();
        imSendMsgRequest.setCloudCustomData(imSendMsgRequest.getVisitNo());
        props.putAll((JSONObject) JSON.toJSON(imSendMsgRequest));
        String payload = buildPayLoad(api, props);

        String result = HttpRequest.post(host)
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(payload)
                .execute()
                .body();

        log.info("\n\n{}\n{}\n{}\n\n", host, payload, result);
        ImSendMsgRespVo imSendMsgResponse = buildResponse(result);
        try {
            if (imSendMsgResponse != null && "OK".equals(imSendMsgResponse.getActionStatus())) {
                QueryWrapper<HosVisit> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("order_num", imSendMsgRequest.getVisitNo());
                HosVisit hosVisit = hosVisitService.getOne(queryWrapper);
                if (hosVisit != null) {
                    hosVisit.setMsg(imSendMsgRequest.getMsgContent());
                    hosVisitService.updateById(hosVisit);
                }
            }
        } catch (Exception e) {
            log.info("发送消息更新订单异常：", e);
        }

        return imSendMsgResponse;
    }

    @Override
    public String callTimV1(String api, String payload) {
        String random = UUIDUtil.randomUUID32();
        String host = "https://console.tim.qq.com/" + api + "?sdkappid=" + tencentProperties.getSdkappid()
                + "&identifier=" + tencentProperties.getAdminidentifier() + "&usersig=" + genAdminUserSign()
                + "&random=" + random + "&contenttype=json";

        String result = HttpRequest.post(host).header("Content-Type", "application/json; charset=UTF-8").body(payload)
                .execute().body();

        log.info("\n\n{}\n{}\n\n", host, result);
        return result;
    }

    @Override
    public boolean sendGroupMsg(ImSendGroupMsgRequestDto req) {
        log.info("sendGroupMsg request: {}", JSONObject.toJSONString(req));
        com.aihoo.domain.im.dto.ImSendGroupMsgRespVo resp = tencentImGroupUtil.sendGroupMessage(req);
        log.info("sendGroupMsg response: actionStatus={}, errorCode={}, errorInfo={}, msgTime={}, msgSeq={}",
                resp.getActionStatus(), resp.getErrorCode(), resp.getErrorInfo(), resp.getMsgTime(), resp.getMsgSeq());
        if (resp.isSuccess()) {
            try {
                ImMsg imMsg = new ImMsg();
                imMsg.setOrderNum(req.getVisitNo());
                imMsg.setPeerReadStatus(0);
                imMsg.setMsgRandom(req.getRandom().toString());
                imMsg.setFromAccount(req.getFromAccount());
                imMsg.setToAccount(req.getToAccount());
                imMsg.setErrorInfo(resp.getErrorInfo());
                imMsg.setSendMsgResult("0");
                imMsg.setLoadParam(req.getLoadParam());
                imMsg.setMsgSeq(resp.getMsgSeq().toString());
                imMsg.setMsgType(req.getMsgType());
                imMsg.setMsgTime(resp.getMsgTime().toString());
                imMsg.setCreateTimeStr(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) + "");
                String fromAccount = req.getFromAccount();
                if (fromAccount != null && fromAccount.startsWith("PATIENT_")) {
                    imMsg.setSickPeerReadStatus(1);
                    imMsg.setDoctorPeerReadStatus(0);
                } else {
                    imMsg.setSickPeerReadStatus(0);
                    imMsg.setDoctorPeerReadStatus(1);
                }
                boolean msg = imMsgService.save(imMsg);
                if (msg) {
                    String msgContent = null;
                    for (int i = 0; i < req.getMsgBody().size(); i++) {
                        ImMsgContent imMsgContent = new ImMsgContent();
                        imMsgContent.setImMsgId(imMsg.getId());
                        ImSendGroupMsgRequestDto.MessageBody reqMsgContent = req.getMsgBody().get(i);
                        String msgType = req.getMsgBody().get(i).getMsgType();
                        imMsgContent.setMsgType(msgType);
                        if ("TIMTextElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("文本消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        } else if ("TIMLocationElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("位置消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        } else if ("TIMFaceElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("表情消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        } else if ("TIMCustomElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("自定义消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        } else if ("TIMSoundElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("语音消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        } else if ("TIMImageElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("图像消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        } else if ("TIMFileElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("文件消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        } else if ("TIMVideoFileElem".equals(msgType)) {
                            imMsgContent.setMsgTypeName("视频消息");
                            msgContent = JSONObject.toJSONString(reqMsgContent.getMsgContent());
                        }
                        imMsgContent.setMsgContent(msgContent);
                        imMsgContent.setCreateTimeStr(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) + "");
                        imMsgContentService.save(imMsgContent);
                    }
                }
            } catch (Exception e) {
                log.info("异常:", e);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean withdrawMsg(ImWithdrawMsgRequestDto req) {
        return imMsgService.withdrawMsg(req);
    }

    private String genAdminUserSign() {
        return ImUtils.genUserSig(tencentProperties.getAdminidentifier(), null,
                tencentProperties.getSdkappid(), tencentProperties.getPrivstr());
    }

    private String buildPayLoad(ImServiceApiEnum api, Properties props) {
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");
        return helper.replacePlaceholders(api.getApiTemplate(), props::getProperty);
    }

    ImSendMsgRespVo buildResponse(String apiResponse) {
        return JSON.parseObject(apiResponse, ImSendMsgRespVo.class);
    }
}
