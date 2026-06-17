package com.aihoo.wechat;

import com.aihoo.common.BizResultCode;
import com.aihoo.exception.BizException;
import com.aihoo.properties.WechatProperties;
import com.aihoo.util.HttpUtil;
import com.aihoo.wechat.dto.WeChatAccessTokenDTO;
import com.aihoo.wechat.dto.WeChatMobileDTO;
import com.aihoo.wechat.dto.WeChatSessionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 微信工具类。
 */
@Slf4j
@Component
public class WeChatComponent {

    @Autowired
    private WechatProperties wechatProperties;

    public WeChatSessionDTO getWeChatSession(String code) {
        String url = wechatProperties.getApiBaseUrl() + "/sns/jscode2session?appid=" + wechatProperties.getAppId()
                + "&secret=" + wechatProperties.getSecret() + "&js_code=" + code + "&grant_type=authorization_code";
        WeChatSessionDTO chatSessionDTO = HttpUtil.getForObject(url, WeChatSessionDTO.class);
        if (Objects.isNull(chatSessionDTO) || !chatSessionDTO.isSuccess()) {
            throw new BizException(BizResultCode.WECHAT_ACCESS_TOKEN_ERROR);
        }
        return chatSessionDTO;
    }

    public WeChatAccessTokenDTO getWeChatAccessToken() {
        String url = wechatProperties.getApiBaseUrl() + "/cgi-bin/token?appid=" + wechatProperties.getAppId()
                + "&secret=" + wechatProperties.getSecret() + "&grant_type=client_credential";
        WeChatAccessTokenDTO weChatAccessToken = HttpUtil.getForObject(url, WeChatAccessTokenDTO.class);

        if (Objects.isNull(weChatAccessToken)) {
            throw new BizException(BizResultCode.WECHAT_ACCESS_TOKEN_ERROR);
        }
        return weChatAccessToken;
    }

    public WeChatMobileDTO getWeCHatMobile(String accessToken, String code) {
        String url = wechatProperties.getApiBaseUrl() + "/wxa/business/getuserphonenumber?access_token=" + accessToken;
        Map<String, String> param = new HashMap<>();
        param.put("code", code);
        WeChatMobileDTO weChatMobileDTO = HttpUtil.post(url, param, WeChatMobileDTO.class);

        if (Objects.isNull(weChatMobileDTO) || !weChatMobileDTO.isSuccess()) {
            log.error("get user phone number failed: {}", Objects.isNull(weChatMobileDTO) ? "null" : weChatMobileDTO.getErrMsg());
            throw new BizException(BizResultCode.WECHAT_MOBILE_ERROR);
        }
        return weChatMobileDTO;
    }
}