package com.aihoo.util;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.Deflater;

public class ImUtils {

    private static final long EXPIRE = 86400 * 365 * 50;

    public static String genUserSig(String userId, byte[] userbuf, String sdkappid, String privstr) {
        long currTime = System.currentTimeMillis() / 1000;
        JSONObject sigDoc = new JSONObject();
        sigDoc.put("TLS.ver", "2.0");
        sigDoc.put("TLS.identifier", userId);
        sigDoc.put("TLS.sdkappid", sdkappid);
        sigDoc.put("TLS.expire", EXPIRE);
        sigDoc.put("TLS.time", currTime);
        String base64UserBuf = null;
        if (null != userbuf) {
            base64UserBuf = Base64.encodeBase64String(userbuf);
            sigDoc.put("TLS.userbuf", base64UserBuf);
        }
        String sig = hmacsha256(userId, currTime, base64UserBuf, sdkappid, privstr);
        if (sig.isEmpty()) {
            return "";
        }
        sigDoc.put("TLS.sig", sig);
        Deflater compressor = new Deflater();
        compressor.setInput(sigDoc.toString().getBytes(StandardCharsets.UTF_8));
        compressor.finish();
        byte[] compressedBytes = new byte[2048];
        int len = compressor.deflate(compressedBytes);
        compressor.end();
        return new String(Base64URL.base64EncodeUrl(Arrays.copyOfRange(compressedBytes, 0, len)))
                .replaceAll("\\s*", "");
    }

    private static String hmacsha256(String identifier, long currTime, String base64Userbuf,
                                     String sdkappid, String privstr) {
        String contentToBeSigned = "TLS.identifier:" + identifier + "\n"
                + "TLS.sdkappid:" + sdkappid + "\n"
                + "TLS.time:" + currTime + "\n"
                + "TLS.expire:" + ImUtils.EXPIRE + "\n";
        if (null != base64Userbuf) {
            contentToBeSigned += "TLS.userbuf:" + base64Userbuf + "\n";
        }
        try {
            byte[] byteKey = privstr.getBytes(StandardCharsets.UTF_8);
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, "HmacSHA256");
            hmac.init(keySpec);
            byte[] byteSig = hmac.doFinal(contentToBeSigned.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(byteSig).replaceAll("\\s*", "");
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            return "";
        }
    }
}
