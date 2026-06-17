package com.aihoo.domain.logistics.util;

import com.aihoo.domain.logistics.dto.sf.AddressRequest;
import com.aihoo.domain.logistics.dto.sf.CommonRequest;
import com.aihoo.domain.logistics.dto.sf.CreateOrderRequest;
import com.aihoo.domain.logistics.dto.sf.OrderPriceRequest;
import com.aihoo.domain.logistics.dto.sl.CommonRespVo;
import com.aihoo.domain.logistics.dto.sl.CreateOrderRespVo;
import com.aihoo.domain.logistics.dto.sl.OrderPriceRespWrapperVo;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 顺丰开放平台 API 工具类。
 *
 * <p>当前支持：价格查询 {@link #queryPrice}、下单 {@link #createOrder}。
 */
public class SFUtil {
    private static final Logger log = LoggerFactory.getLogger(SFUtil.class);

    private static final String PARTNER_ID = "HOPFBKLRE0SV";
    private static final String SECRET = "MU4Y1Z4y4OLJ1M1fZyFZLFPeJcIRFjU6";
    private static final String SERVICE_CODE = "EXP_RECE_QUERY_DELIVERTM";

    // 沙箱环境
    private static final String URL = "https://sfapi-sbox.sf-express.com/std/service";
    // 正式环境: https://bspgw.sf-express.com/std/service

    public static BigDecimal queryPrice(OrderPriceRequest priceRequest) throws Exception {

        CommonRequest commonReq = new CommonRequest();
        String requestId = UUID.randomUUID().toString().replace("-", "");
        long timestamp = System.currentTimeMillis();

        commonReq.setPartnerID(PARTNER_ID);
        commonReq.setRequestID(requestId);
        commonReq.setServiceCode(SERVICE_CODE);
        commonReq.setTimestamp(timestamp);

        String bizJson = JSONObject.toJSONString(priceRequest);
        commonReq.setMsgData(bizJson);
        Map<String, String> formParams = Maps.newHashMap();
        formParams.put("partnerID", commonReq.getPartnerID());
        formParams.put("requestID", commonReq.getRequestID());
        formParams.put("serviceCode", commonReq.getServiceCode());
        formParams.put("timestamp", String.valueOf(commonReq.getTimestamp()));
        formParams.put("accessToken", getToken());
        formParams.put("msgData", commonReq.getMsgData());
        String responseJson = sendPostRequest(URL, formParams);
        JSONObject resp = JSONObject.parseObject(responseJson);
        if (!"A1000".equals(resp.getString("apiResultCode"))) {
            log.info("接口请求失败，结果:{}", responseJson);
            return new BigDecimal("0.0");
        }
        CommonRespVo commonResp = JSONObject.parseObject(JSONObject.parseObject(responseJson).getString("apiResultData"), CommonRespVo.class);
        boolean isSuccess = false;
        if ("S0000".equals(commonResp.getErrorCode())) {
            isSuccess = true;
        } else if ("true".equalsIgnoreCase(commonResp.getSuccess())) {
            isSuccess = true;
        }

        if (isSuccess) {
            String msgDataStr = commonResp.getMsgData();

            if (msgDataStr != null && !msgDataStr.trim().isEmpty()) {
                try {
                    OrderPriceRespWrapperVo wrapper = JSONObject.parseObject(msgDataStr, OrderPriceRespWrapperVo.class);

                    if (wrapper != null && wrapper.getDeliverTmDto() != null) {
                        return wrapper.getDeliverTmDto().get(0).getFee();
                    } else {
                        log.info("顺丰接口返回数据不正确,结果数据:{},参数:{}",
                                JSONObject.toJSONString(commonResp), JSONObject.toJSONString(commonReq));
                    }
                } catch (Exception e) {
                    log.info("顺丰接口查询异常", e);
                }
            }
        }
        return new BigDecimal("0.0");
    }

    private static String getToken() {
        String url = "https://sfapi-sbox.sf-express.com/oauth2/accessToken";
        Map<String, String> param = Maps.newHashMap();
        param.put("partnerID", PARTNER_ID);
        param.put("secret", SECRET);
        param.put("grantType", "password");
        try {
            JSONObject resp = JSONObject.parseObject(sendPostRequest(url, param));
            System.out.println(resp.getString("accessToken"));
            return resp.getString("accessToken");
        } catch (Exception e) {
        }
        return "";
    }

    public static String createOrder(CreateOrderRequest bizRequest) throws Exception {
        String requestId = UUID.randomUUID().toString().replace("-", "");
        long timestamp = System.currentTimeMillis();
        String bizJson = JSONObject.toJSONString(bizRequest);
        Map<String, String> params = Maps.newHashMap();
        params.put("partnerID", PARTNER_ID);
        params.put("requestID", requestId);
        params.put("serviceCode", SERVICE_CODE);
        params.put("timestamp", String.valueOf(timestamp));
        params.put("accessToken", getToken());
        params.put("msgData", bizJson);
        String responseJson = sendPostRequest(URL, params);
        JSONObject resp = JSONObject.parseObject(responseJson);
        if (!"A1000".equals(resp.getString("apiResultCode"))) {
            log.info("接口请求失败，结果:{}", responseJson);
            return "";
        }
        CommonRespVo commonResp = JSONObject.parseObject(JSONObject.parseObject(responseJson).getString("apiResultData"), CommonRespVo.class);
        if (commonResp == null) {
            throw new RuntimeException("下单失败: apiResultData 为空");
        }
        if (!"S0000".equals(commonResp.getErrorCode())) {
            throw new RuntimeException("下单业务处理失败: " + commonResp.getErrorMsg() + " (" + commonResp.getErrorCode() + ")");
        }
        CreateOrderRespVo respVo = JSONObject.parseObject(commonResp.getMsgData(), CreateOrderRespVo.class);
        List<CreateOrderRespVo.WaybillNoInfo> waybill = respVo.getWaybillNoInfoList();
        if (CollectionUtils.isEmpty(waybill)) {
            log.info("请求参数:{}请求回参:{}", bizRequest.getOrderId(), responseJson);
            return "";
        }
        return waybill.get(0).getWaybillNo();
    }

    private static String sendPostRequest(String urlStr, Map<String, String> params) throws IOException {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (!postData.isEmpty()) {
                postData.append('&');
            }
            postData.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
            postData.append('=');
            postData.append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8));
        }

        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(postDataBytes);
        }

        int status = conn.getResponseCode();
        InputStream inputStream = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();
        conn.disconnect();

        if (status != 200) {
            throw new IOException("HTTP Error: " + status + " Response: " + result.toString());
        }

        return result.toString();
    }
}