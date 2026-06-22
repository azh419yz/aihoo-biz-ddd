package com.aihoo.api.doctor.controller;

import com.aihoo.api.doctor.request.WxaCodeRequest;
import com.aihoo.api.doctor.vo.WxaCodeVo;
import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import com.aihoo.security.AuthUtil;
import com.aihoo.wechat.WeChatApiService;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序码生成接口
 */
@Tag(name = "wxacode", description = "医生端-小程序码相关接口")
@Slf4j
@RestController
@RequestMapping("/api/wxacode")
public class WxaCodeController {

    @Autowired
    private WeChatApiService weChatApiService;

    /**
     * 生成小程序码接口
     *
     * @return 小程序码的Base64编码
     */
    @PostMapping("/generate")
    public BizResult<WxaCodeVo> generateWxaCode() {
        WxaCodeRequest request = new WxaCodeRequest();
        request.setPage("pages/index/index");
        request.setScene("id=" + AuthUtil.getLoginUserId());
        request.setAutoColor(true);
        request.setIsHyaline(false);
        request.setWidth(430);
        log.info("收到生成小程序码请求，页面路径：{}，参数：{}", request.getPage(), request.getScene());

        try {
            // 获取Access Token
            String accessToken = weChatApiService.getAccessToken();

            // 构建请求参数
            JSONObject requestData = new JSONObject();
            requestData.put("scene", request.getScene() != null ? request.getScene() : "");
            requestData.put("page", request.getPage());
            requestData.put("width", request.getWidth());
            requestData.put("auto_color", request.getAutoColor());
            requestData.put("is_hyaline", request.getIsHyaline());
            requestData.put("check_path", false);  // 不检查page参数

            if (!request.getAutoColor()) {
                requestData.put("line_color", request.getLineColor());
            }

            // 生成小程序码
            String qrcodeBase64 = weChatApiService.generateWxaCode(accessToken, requestData);
            WxaCodeVo WxaCodeVo = new WxaCodeVo();
            WxaCodeVo.setQrcode(qrcodeBase64);

            return BizResult.success(WxaCodeVo);

        } catch (Exception e) {
            log.error("生成小程序码失败", e);
            return BizResult.fail(BizResultCode.INTERNAL_ERROR, e.getMessage());
        }
    }
}
