package com.aihoo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    private static HttpUtil instance;
    private static RestTemplate restTemplate;
    private static ObjectMapper objectMapper;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        HttpUtil.restTemplate = restTemplate;
        instance = this;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        HttpUtil.objectMapper = objectMapper;
    }

    public static <T> T getForObject(String url, Class<T> responseType) {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }
        String responseString = restTemplate.getForObject(url, String.class);
        if (responseString == null) return null;
        if (responseType == String.class) return (T) responseString;
        try {
            if (objectMapper == null) objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseString, responseType);
        } catch (Exception e) {
            log.error("HttpUtil deserialization failed for url: {}", url, e);
            throw new RuntimeException("HTTP请求结果解析失败", e);
        }
    }

    public static <T> T post(String url, Object request, Class<T> responseType) {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }
        try {
            String jsonBody = null;
            if (request != null) {
                if (objectMapper == null) objectMapper = new ObjectMapper();
                jsonBody = objectMapper.writeValueAsString(request);
            }
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(jsonBody, headers);
            String responseString = restTemplate.postForObject(url, entity, String.class);
            if (responseString == null) return null;
            if (responseType == String.class) return (T) responseString;
            return objectMapper.readValue(responseString, responseType);
        } catch (Exception e) {
            log.error("HttpUtil post failed for url: {}", url, e);
            throw new RuntimeException("HTTP请求结果解析失败", e);
        }
    }
}