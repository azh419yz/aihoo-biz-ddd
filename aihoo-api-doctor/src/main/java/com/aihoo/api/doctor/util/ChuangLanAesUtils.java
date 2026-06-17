package com.aihoo.api.doctor.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ChuangLanAesUtils {

    private static final String charset = "UTF-8";

    public static String decrypt(String sSrc, String sKey, String siv) throws Exception {
        try {
            if (sSrc == null || sSrc.length() == 0) {
                return null;
            }
            if (sKey == null) {
                throw new Exception("decrypt key is null");
            }
            if (sKey.length() != 16) {
                throw new Exception("decrypt key length error");
            }
            byte[] Decrypt = ChuangLanByteFormat.hexToBytes(sSrc);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(charset), "AES");
            IvParameterSpec iv = new IvParameterSpec(siv.getBytes(charset));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            return new String(cipher.doFinal(Decrypt), charset);
        } catch (Exception ex) {
            throw new Exception("decrypt errot", ex);
        }
    }
}
