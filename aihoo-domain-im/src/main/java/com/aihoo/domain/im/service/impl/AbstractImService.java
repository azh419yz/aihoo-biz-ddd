package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.dto.ImSendMsgRequest;
import com.aihoo.domain.im.dto.ImSendMsgRespVo;
import com.aihoo.domain.im.enums.ImServiceApiEnum;
import com.aihoo.domain.im.service.ImService;
import com.aihoo.properties.TencentProperties;
import com.aihoo.util.ImUtils;
import com.aihoo.util.UUIDUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.aihoo.domain.visit.entity.HosVisit;
import com.aihoo.domain.visit.service.HosVisitService;
import cn.hutool.http.HttpRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;

@Slf4j
public abstract class AbstractImService implements ImService {

    @Resource
    private TencentProperties tencentProperties;
    @Resource
    private HosVisitService hosVisitService;

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

        String result = HttpRequest.post(host).header("Content-Type", "application/json; charset=UTF-8").body(payload)
                .execute().body();

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
}
