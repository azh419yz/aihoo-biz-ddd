package com.aihoo.util;

import cn.hutool.core.codec.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

public class SecurityUtil {

    public static final String SIGN_ALGORITHMS = "SHA256WithRSA";

    public static String signature(String privateKey, String data) {
        try {
            byte[] signatureBytes = signature(privateKey, data.getBytes("UTF-8"));
            if (signatureBytes != null && signatureBytes.length > 0) {
                return Base64.encode(signatureBytes);
            }
        } catch (Exception e) {

        }
        return null;
    }

    private static byte[] signature(String privateKey, byte[] bData) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyFactory.generatePrivate(keySpec);
            Signature sig = Signature.getInstance(SIGN_ALGORITHMS);
            sig.initSign(priKey);
            sig.update(bData);
            return sig.sign();
        } catch (Exception e) {
            return null;
        }
    }
}