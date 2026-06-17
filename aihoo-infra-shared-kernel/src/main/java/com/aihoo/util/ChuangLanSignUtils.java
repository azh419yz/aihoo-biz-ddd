package com.aihoo.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Map;

public class ChuangLanSignUtils {

    public static String getSign(Map<String, String> requestMap, String appKey) {
        return hmacSHA256Encrypt(requestMap2Str(requestMap), appKey);
    }

    private static String hmacSHA256Encrypt(String encryptText, String encryptKey) {
        byte[] result = null;
        try {
            SecretKeySpec signinKey = new SecretKeySpec(encryptKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signinKey);
            byte[] rawHmac = mac.doFinal(encryptText.getBytes("UTF-8"));
            return ChuangLanByteFormat.bytesToHexString(rawHmac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String requestMap2Str(Map<String, String> requestMap) {
        String[] keys = requestMap.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : keys) {
            if (!str.equals("sign")) {
                stringBuilder.append(str).append(requestMap.get(str));
            }
        }
        return stringBuilder.toString();
    }
}
