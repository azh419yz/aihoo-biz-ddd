package com.aihoo.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class SmsUtils {
    static final Properties CONFIG = PropertyReader.getProperties("/config/sms.properties");

    private static String requesturl = CONFIG.getProperty("chuanglan.requesturl");
    private static String account = CONFIG.getProperty("chuanglan.account");
    private static String pswd = CONFIG.getProperty("chuanglan.pswd");
    private static String url = CONFIG.getProperty("chuanglan.url");
    private static String username = CONFIG.getProperty("chuanglan.username");
    private static String password = CONFIG.getProperty("chuanglan.password");

    public static String randomCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String send(Map<String, Object> map) {
        Map<String, Object> sendMap = new HashMap<>();
        sendMap.put("account", account);
        sendMap.put("password", pswd);
        sendMap.put("msg", map.get("msg"));
        sendMap.put("phone", map.get("mobile"));
        JSONObject js = (JSONObject) JSON.toJSON(sendMap);
        return sendSmsByPost(requesturl, js.toString());
    }

    public static String offlineSend(Map<String, Object> objectHashMap) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("account", username);
        hashMap.put("password", password);
        hashMap.put("msg", objectHashMap.get("msg"));
        hashMap.put("params", objectHashMap.get("mobile"));
        JSONObject js = (JSONObject) JSON.toJSON(hashMap);
        return sendSmsByPost(url, js.toString());
    }

    public static String offlineSend(String model, String... params) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("account", username);
        hashMap.put("password", password);
        hashMap.put("msg", model);
        String join = String.join(",", params);
        hashMap.put("params", join);
        JSONObject js = (JSONObject) JSON.toJSON(hashMap);
        return sendSmsByPost(url, js.toString());
    }

    public static String sendSmsByPost(String path, String postContent) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
            try (OutputStream os = conn.getOutputStream()) {
                os.write(postContent.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
            StringBuilder sb = new StringBuilder();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                }
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"code\":\"100\",\"time\":\"20170410103836\",\"msgId\":\"17041010383624511\",\"errorMsg\":\"\"}";
    }
}
