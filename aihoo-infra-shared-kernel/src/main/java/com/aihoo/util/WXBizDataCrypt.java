package com.aihoo.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class WXBizDataCrypt {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode decrypt(String appId, String sessionKey, String encryptedData, String iv) throws Exception {
        // 1. Base64 解码
        byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);
        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);
        byte[] ivBytes = Base64.getDecoder().decode(iv);

        // 2. 创建密钥和 IV
        SecretKeySpec keySpec = new SecretKeySpec(sessionKeyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        // 3. 初始化 Cipher（解密模式）
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        // 4. 执行解密（✅ 关键：用 doFinal，不是 decrypt！）
        byte[] decryptedBytes = cipher.doFinal(encryptedDataBytes);

        // 5. PKCS#7 去填充
        byte[] unpadded = pkcs7Unpad(decryptedBytes);

        // 6. 转 JSON
        String jsonStr = new String(unpadded, StandardCharsets.UTF_8);
        JsonNode jsonNode = objectMapper.readTree(jsonStr);

        // 7. 校验 appid
        String decryptedAppId = jsonNode.path("watermark").path("appid").asText();
        if (!appId.equals(decryptedAppId)) {
            throw new IllegalArgumentException("Invalid Buffer: appid mismatch");
        }

        return jsonNode;
    }

    private static byte[] pkcs7Unpad(byte[] data) {
        if (data == null || data.length == 0) return data;
        int pad = data[data.length - 1] & 0xFF;
        if (pad < 1 || pad > data.length) return data; // 防止非法填充
        return java.util.Arrays.copyOf(data, data.length - pad);
    }

    public static void main(String[] args) throws Exception {
        String appId = "wx0d8528ecab4b7eab";
        String sessionKey = "Lh0S1QoZIiQwywXX3XF9jQ==";
        String encryptedData = "AO+hXuK2esl2T2IleMK33xtjMgSMNqj4/7fpO8S5jfoWxYsI1nl7UgmszH9mV2S0eV4tpZCz0Hb9rwyIXf0mkHLfUQtNpVFtO9S0uKrS8wTL02Y1uBV8+fO/jnPsD49XBldvrEYA79Omsp4SNDFdhWl18yu7uqSylZAtio7KMWkwj6WQ8AKXMvfRJ8wmX47hG/rPG+kPQwjoVOdBok0AdqhLzFcL4K8k0PWOuH7Wg3N8ONzZe4W0A9ZAx5UNphiCKr/w50CsaWQR/qzy+5x3TnKyrFr5uP/6ZIo9hdZ6gKQVwd1LFsITA3HnQBlpeuL3wa1m2KaJdyTQzKI2qHnwrTFUAkinQaPmsSCBA5+1F7sY0k8vHDyurost/62TWgwDqrqyH4XB88EgaKQazDmVv4O6dptrhai6NaRsWRjnhSfOaqT9AmftlpHLlEetRRqVvRUuJHcO/G8+gfsMJWr31Q==";
        String iv = "FQMQe2chSiEcQgZgYEbZsg==";

        JsonNode decrypt = WXBizDataCrypt.decrypt(appId, sessionKey, encryptedData, iv);
        System.out.println(decrypt);
    }
}