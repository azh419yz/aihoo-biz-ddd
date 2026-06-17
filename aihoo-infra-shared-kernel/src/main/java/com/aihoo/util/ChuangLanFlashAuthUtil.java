package com.aihoo.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChuangLanFlashAuthUtil {

    private static final Logger logger = LoggerFactory.getLogger(ChuangLanFlashAuthUtil.class);

    public static final String privateKey = "";

    private static final String URL_APP_MOBILE_QUERY = "https://wsflash.253.com/open/flashsdk/mobile-query";
    private static final String URL_WEB_MOBILE_QUERY = "https://api.253.com/open/web/mobile-query";
    private static final String URL_APP_MOBILE_VALIDATE = "https://wsflash.253.com/open/flashsdk/mobile-validate";
    private static final String URL_WEB_MOBILE_VALIDATE = "https://api.253.com/open/flashsdk/web-mobile-validate";

    public static final String ENCRYPT_TYPE_AES = "0";
    public static final String ENCRYPT_TYPE_RSA = "1";

    public static Map<String, Object> queryMobileApp(String token, String clientIp, String encryptType
            , String appId, String appKey) {
        Map<String, String> params = Maps.newHashMap();
        params.put("appId", appId);
        params.put("token", token);
        if (StringUtils.isNotEmpty(clientIp)) params.put("clientIp", clientIp);
        if (StringUtils.isNotEmpty(encryptType)) params.put("encryptType", encryptType);

        params.put("sign", ChuangLanSignUtils.getSign(params, appKey));

        String responseJson = sendPostRequest(URL_APP_MOBILE_QUERY, params);
        return processResponse(responseJson, encryptType, appKey);
    }

    public static Map<String, Object> queryMobileWeb(String token, String clientIp, String encryptType
            , String appId, String appKey) {
        Map<String, String> params = Maps.newHashMap();
        params.put("appId", appId);
        params.put("token", token);
        if (clientIp != null && !clientIp.isEmpty()) params.put("clientIp", clientIp);
        if (encryptType != null) params.put("encryptType", encryptType);

        params.put("sign", ChuangLanSignUtils.getSign(params, appKey));

        String responseJson = sendPostRequest(URL_WEB_MOBILE_QUERY, params);
        return processResponse(responseJson, encryptType, appKey);
    }

    public static Map<String, Object> validateMobileApp(String token, String mobile, String appId, String appKey) {
        Map<String, String> params = Maps.newHashMap();
        params.put("appId", appId);
        params.put("token", token);
        params.put("mobile", mobile);

        params.put("sign", ChuangLanSignUtils.getSign(params, appKey));

        String responseJson = sendPostRequest(URL_APP_MOBILE_VALIDATE, params);
        JSONObject json = JSONUtil.parseObj(responseJson);

        Map<String, Object> result = new HashMap<>();
        result.put("code", json.getStr("code"));
        result.put("message", json.getStr("message"));
        if ("200000".equals(json.getStr("code"))) {
            JSONObject data = json.getJSONObject("data");
            result.put("isVerify", data.getStr("isVerify"));
            result.put("tradeNo", data.getStr("tradeNo"));
        }
        return result;
    }

    public static Map<String, Object> validateMobileWeb(String token, String mobile, String appId, String appKey) {
        Map<String, String> params = Maps.newHashMap();
        params.put("appId", appId);
        params.put("token", token);
        params.put("mobile", mobile);

        params.put("sign", ChuangLanSignUtils.getSign(params, appKey));

        String responseJson = sendPostRequest(URL_WEB_MOBILE_VALIDATE, params);
        JSONObject json = JSONUtil.parseObj(responseJson);

        Map<String, Object> result = new HashMap<>();
        result.put("code", json.getStr("code"));
        result.put("message", json.getStr("message"));
        if ("200000".equals(json.getStr("code"))) {
            JSONObject data = json.getJSONObject("data");
            result.put("isVerify", data.getStr("isVerify"));
            result.put("tradeNo", data.getStr("tradeNo"));
        }
        return result;
    }

    private static Map<String, Object> processResponse(String responseJson, String encryptType, String appKey) {
        Map<String, Object> result = new HashMap<>();
        if (responseJson == null) {
            result.put("code", "500000");
            result.put("message", "请求失败，无响应");
            return result;
        }
        logger.info("创蓝请求数据:{}", responseJson);

        JSONObject json = JSONUtil.parseObj(responseJson);
        result.put("code", json.getStr("code"));
        result.put("message", json.getStr("message"));
        result.put("chargeStatus", json.getInt("chargeStatus"));

        if ("200000".equals(json.getStr("code"))) {
            JSONObject data = json.getJSONObject("data");
            String tradeNo = data.getStr("tradeNo");
            String encryptedMobile = data.getStr("mobileName") != null ? data.getStr("mobileName") : data.getStr("mobile");
            result.put("tradeNo", tradeNo);
            try {
                String decryptedMobile = decryptMobile(encryptedMobile, encryptType, appKey);
                result.put("mobile", decryptedMobile);
            } catch (Exception e) {
                result.put("decryptError", e.getMessage());
            }
        }
        return result;
    }

    private static String sendPostRequest(String url, Map<String, String> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();
        httpPost.setConfig(config);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        try {
            List<BasicNameValuePair> nvps = new ArrayList<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));

            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return org.apache.http.util.EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.info("异常:", e);
            return null;
        } finally {
            try {
                httpClient.close();
            } catch (Exception ignored) {
            }
        }
    }

    private static String decryptMobile(String hexCipherText, String encryptType, String appKey) throws Exception {
        logger.info("开始解析:{},{},{}", hexCipherText, encryptType, appKey);
        if (StringUtils.isEmpty(encryptType) || "0".equals(encryptType)) {
            String key = ChuangLanMd5.getMD5Code(appKey);
            return ChuangLanAesUtils.decrypt(hexCipherText, key.substring(0, 16), key.substring(16));
        } else if ("1".equals(encryptType)) {
            return ChuangLanRsaUtils.decryptByPrivateKeyForLongStr(hexCipherText, privateKey);
        }
        return "";
    }
}
