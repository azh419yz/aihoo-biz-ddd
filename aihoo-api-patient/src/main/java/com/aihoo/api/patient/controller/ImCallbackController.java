package com.aihoo.api.patient.controller;

import com.aihoo.api.patient.vo.CallBackVo;
import com.aihoo.properties.TencentProperties;
import com.aihoo.domain.im.dto.ImCallbackReqDto;
import com.aihoo.domain.im.enums.CallbackCommandEnum;
import com.aihoo.domain.im.service.ImCallBackService;
import com.aihoo.util.BeanRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@Slf4j
@RequestMapping("/api/v2/im/call-back")
public class ImCallbackController {

    @Autowired
    private BeanRegistry beanRegistry;
    @Autowired
    private TencentProperties tencentProperties;

    @PostMapping
    public CallBackVo callBack(HttpServletResponse response, HttpServletRequest request) {
        log.info("请求回调接口");
        String sdkAppid = request.getParameter("SdkAppid");
        if (!sdkAppid.equals(tencentProperties.getSdkappid())) {
            log.info("SdkAppid不匹配，非法请求,{},{}", tencentProperties.getSdkappid(), sdkAppid);
            return new CallBackVo("FAIL", "1", "SdkAppid不匹配，非法请求");
        }
        String callbackCommand = request.getParameter("CallbackCommand");
        String contenttype = request.getParameter("contenttype");
        String clientIP = request.getParameter("ClientIP");
        String optPlatform = request.getParameter("OptPlatform");
        ImCallbackReqDto callBackRequest = new ImCallbackReqDto();
        callBackRequest.setSdkAppid(sdkAppid);
        callBackRequest.setContenttype(contenttype);
        callBackRequest.setCallbackCommand(callbackCommand);
        callBackRequest.setClientIP(clientIP);
        callBackRequest.setOptPlatform(optPlatform);

        StringBuilder stringBuilder = new StringBuilder();

        try (InputStream inputStream = request.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            char[] charBuffer = new char[128];
            int bytesRead;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
            callBackRequest.setCallBackBody(stringBuilder.toString());
        } catch (Exception ex) {
            log.info("解析请求参数异常");
            return new CallBackVo("FAIL", "1", "请求参数转换异常");
        }

        CallbackCommandEnum command = CallbackCommandEnum.fromValue(callbackCommand);
        if (command.equals(CallbackCommandEnum.NONE)) {
            log.info("回调异常");
            return new CallBackVo("FAIL", "1", "command不匹配，非法请求");
        }

        ImCallBackService imCallBackService = beanRegistry.getBean(command.getServiceName(), ImCallBackService.class);
        imCallBackService.callBack(callBackRequest);
        return new CallBackVo("OK", "0", "");
    }
}
