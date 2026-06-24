package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.dto.ImWithdrawMsgRequestDto;
import com.aihoo.domain.im.entity.ImCustomerMsg;
import com.aihoo.domain.im.entity.ImMsg;
import com.aihoo.domain.im.entity.ImMsgContent;
import com.aihoo.domain.im.entity.ImMsgCustomerContent;
import com.aihoo.domain.im.mapper.ImCustomerMsgMapper;
import com.aihoo.domain.im.mapper.ImMsgContentMapper;
import com.aihoo.domain.im.mapper.ImMsgCustomerContentMapper;
import com.aihoo.domain.im.mapper.ImMsgMapper;
import com.aihoo.domain.im.service.ImMsgService;
import com.aihoo.util.StringHandler;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImMsgServiceImpl extends ServiceImpl<ImMsgMapper, ImMsg> implements ImMsgService {

    private final ImMsgMapper imMsgMapper;
    private final ImMsgContentMapper imMsgContentMapper;
    private final ImCustomerMsgMapper imCustomerMsgMapper;
    private final ImMsgCustomerContentMapper imMsgCustomerContentMapper;

    @Override
    @Async(value = "asyncExecutor")
    public void imMsgSave(Map<String, Object> map) {
        if (map.get("From_Account").toString().startsWith("ADMIN")
                || (StringUtils.isNotEmpty(MapUtils.getString(map, "To_Account"))
                && MapUtils.getString(map, "To_Account").startsWith("ADMIN"))) {
            insertImCustomerMsg(map);
        } else {
            insertImMsg(map);
        }
    }

    private void insertImMsg(Map<String, Object> map) {
        if (0 != (Integer) map.get("SendMsgResult")) {
            return;
        }
        ImMsg imMsg = new ImMsg();
        imMsg.setFromAccount(MapUtils.getString(map, "From_Account"));
        imMsg.setToAccount(MapUtils.getString(map, "To_Account"));
        imMsg.setMsgSeq(MapUtils.getString(map, "MsgSeq"));
        imMsg.setMsgRandom(MapUtils.getString(map, "MsgRandom"));
        imMsg.setMsgTime(MapUtils.getString(map, "MsgTime"));
        imMsg.setMsgKey(MapUtils.getString(map, "MsgKey"));
        imMsg.setSendMsgResult(MapUtils.getString(map, "SendMsgResult"));
        imMsg.setErrorInfo(MapUtils.getString(map, "ErrorInfo"));
        imMsg.setOrderNum(MapUtils.getString(map, "visitNo"));
        imMsg.setCreateTimeStr(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) + "");
        String fromAccount = MapUtils.getString(map, "From_Account");
        if (fromAccount != null && fromAccount.startsWith("PATIENT_")) {
            imMsg.setSickPeerReadStatus(1);
            imMsg.setDoctorPeerReadStatus(0);
        } else {
            imMsg.setSickPeerReadStatus(0);
            imMsg.setDoctorPeerReadStatus(1);
        }
        List<Map<String, Object>> imMsgBody = (List<Map<String, Object>>) map.get("MsgBody");
        imMsgMapper.insert(imMsg);
        String msgContent = null;
        if (CollectionUtils.isNotEmpty(imMsgBody)) {
            for (Map<String, Object> stringObjectMap : imMsgBody) {
                ImMsgContent imMsgContent = new ImMsgContent();
                imMsgContent.setImMsgId(imMsg.getId());
                String msgType = stringObjectMap.get("MsgType").toString();
                imMsgContent.setMsgType(msgType);
                if ("TIMTextElem".equals(msgType)) {
                    imMsgContent.setMsgTypeName("文本消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                } else if ("TIMLocationElem".equals(msgType)) {
                    imMsgContent.setMsgTypeName("位置消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                } else if ("TIMFaceElem".equals(msgType)) {
                    imMsgContent.setMsgTypeName("表情消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                } else if ("TIMCustomElem".equals(msgType)) {
                    imMsgContent.setMsgTypeName("自定义消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                } else if ("TIMSoundElem".equals(msgType)) {
                    imMsgContent.setMsgTypeName("语音消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                } else if ("TIMImageElem".equals(msgType)) {
                    imMsgContent.setMsgTypeName("图像消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                } else if ("TIMFileElem".equals(msgType)) {
                    imMsgContent.setMsgTypeName("文件消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                } else if ("TIMVideoFileElem".equals(msgType)) {
                    imMsgContent.setMsgTypeName("视频消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                }
                imMsgContent.setMsgContent(msgContent);
                imMsgContent.setCreateTimeStr(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) + "");
                imMsgContentMapper.insert(imMsgContent);
            }
        }
    }

    private void insertImCustomerMsg(Map<String, Object> map) {
        if (0 != (Integer) map.get("SendMsgResult")) {
            return;
        }
        ImCustomerMsg imCustomerMsg = new ImCustomerMsg();
        imCustomerMsg.setFromAccount(map.get("From_Account").toString());
        imCustomerMsg.setToAccount(map.get("To_Account").toString());
        if (map.get("From_Account").toString().startsWith("ADMIN")) {
            imCustomerMsg.setAdminId(StringHandler.rightSubStr(map.get("From_Account").toString(), "_"));
            imCustomerMsg.setPatientId(StringHandler.rightSubStr(map.get("To_Account").toString(), "_"));
        } else {
            imCustomerMsg.setAdminId(StringHandler.rightSubStr(map.get("To_Account").toString(), "_"));
            imCustomerMsg.setPatientId(StringHandler.rightSubStr(map.get("From_Account").toString(), "_"));
        }
        imCustomerMsg.setMsgSeq(map.get("MsgSeq").toString());
        imCustomerMsg.setMsgRandom(map.get("MsgRandom").toString());
        imCustomerMsg.setMsgTime(map.get("MsgTime").toString());
        imCustomerMsg.setMsgKey(map.get("MsgKey").toString());
        imCustomerMsg.setSendMsgResult(map.get("SendMsgResult").toString());
        imCustomerMsg.setErrorInfo(map.get("ErrorInfo").toString());
        imCustomerMsg.setCreateTimeStr(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) + "");
        List<Map<String, Object>> imMsgBody = (List<Map<String, Object>>) map.get("MsgBody");
        imCustomerMsgMapper.insert(imCustomerMsg);
        String msgContent = null;
        if (CollectionUtils.isNotEmpty(imMsgBody)) {
            for (Map<String, Object> stringObjectMap : imMsgBody) {
                ImMsgCustomerContent imMsgCustomerContent = new ImMsgCustomerContent();
                imMsgCustomerContent.setImMsgId(imCustomerMsg.getId());
                String msgType = stringObjectMap.get("MsgType").toString();
                imMsgCustomerContent.setMsgType(msgType);
                if ("TIMTextElem".equals(msgType)) {
                    imMsgCustomerContent.setMsgTypeName("文本消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                } else if ("TIMLocationElem".equals(msgType)) {
                    imMsgCustomerContent.setMsgTypeName("位置消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                } else if ("TIMFaceElem".equals(msgType)) {
                    imMsgCustomerContent.setMsgTypeName("表情消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                } else if ("TIMCustomElem".equals(msgType)) {
                    imMsgCustomerContent.setMsgTypeName("自定义消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                } else if ("TIMSoundElem".equals(msgType)) {
                    imMsgCustomerContent.setMsgTypeName("语音消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                } else if ("TIMImageElem".equals(msgType)) {
                    imMsgCustomerContent.setMsgTypeName("图像消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                } else if ("TIMFileElem".equals(msgType)) {
                    imMsgCustomerContent.setMsgTypeName("文件消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                } else if ("TIMVideoFileElem".equals(msgType)) {
                    imMsgCustomerContent.setMsgTypeName("视频消息");
                    msgContent = JSONObject.toJSONString(stringObjectMap.get("MsgContent"));
                }
                imMsgCustomerContent.setMsgContent(msgContent);
                imMsgCustomerContent.setCreateTimeStr(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) + "");
                imMsgCustomerContentMapper.insert(imMsgCustomerContent);
            }
        }
    }

    @Override
    public boolean withdrawMsg(ImWithdrawMsgRequestDto req) {
        String visitNo = req.getMsgReq().split("_")[1];
        ImMsg msg = getOne(new LambdaQueryWrapper<ImMsg>()
                .eq(ImMsg::getMsgSeq, req.getMsgReq())
                .eq(ImMsg::getOrderNum, visitNo)
                .last("limit 1"));
        if (msg != null) {
            msg.setMsgStatus("WITHDRAW");
            return updateById(msg);
        }
        return false;
    }

}