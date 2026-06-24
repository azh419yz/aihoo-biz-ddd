package com.aihoo.util;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RsaUtil {
    
    private static final String CIPHER_DE = "RSA";
    
    private final static int KEY_SIZE = 1024;

    
    public static Map<Integer, String> genKeyPair() throws Exception {

        Map<Integer, String> keyMap = new HashMap<Integer, String>();

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

        keyPairGen.initialize(KEY_SIZE, new SecureRandom());

        KeyPair keyPair = keyPairGen.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();

        PublicKey publicKey = keyPair.getPublic();

        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        keyMap.put(0, publicKeyString);

        keyMap.put(1, privateKeyString);
        return keyMap;
    }

    
    public static String encryptPublicKey(String str, String publicKey) throws Exception {

        byte[] decode = Base64.getDecoder().decode(publicKey);

        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(CIPHER_DE).generatePublic(new X509EncodedKeySpec(decode));

        Cipher cipher = Cipher.getInstance(CIPHER_DE);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] bytes = str.getBytes();
        int inputLength = bytes.length;
        System.out.println("加密字节书" + inputLength);
        int MAX_ENCRYPT_BLOCK = 117;
        int offSet = 0;
        byte[] resultBytes = {};
        byte[] cache = {};
        while (inputLength - offSet > 0) {
            if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(bytes, offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(bytes, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        return Base64.getEncoder().encodeToString(resultBytes);
    }

    
    public static String decoderPrivateKey(String str, String privateKey) throws Exception {

        byte[] inputByte = Base64.getDecoder().decode(str);

        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(CIPHER_DE).generatePrivate(new PKCS8EncodedKeySpec(decoded));

        Cipher cipher = Cipher.getInstance(CIPHER_DE);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        int inputLength = inputByte.length;
        System.out.println("加密字节书" + inputLength);
        int MAX_ENCRYPT_BLOCK = 128;
        int offSet = 0;
        byte[] resultBytes = {};
        byte[] cache = {};
        while (inputLength - offSet > 0) {
            if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(inputByte, offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(inputByte, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        String s = new String(resultBytes, "UTF-8");
        return s;
    }

    
    public static String encryptPrivateKey(String str, String privateKey) throws Exception {

        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(CIPHER_DE).generatePrivate(new PKCS8EncodedKeySpec(decoded));

        Cipher cipher = Cipher.getInstance(CIPHER_DE);
        cipher.init(Cipher.ENCRYPT_MODE, priKey);
        byte[] bytes = str.getBytes();
        int inputLength = bytes.length;
        System.out.println("加密字节书" + inputLength);
        int MAX_ENCRYPT_BLOCK = 117;
        int offSet = 0;
        byte[] resultBytes = {};
        byte[] cache = {};
        while (inputLength - offSet > 0) {
            if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(bytes, offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(bytes, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        return Base64.getEncoder().encodeToString(resultBytes);
    }

    
    public static String decoderPublicKey(String str, String privateKey) throws Exception {

        byte[] inputByte = Base64.getDecoder().decode(str);

        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(CIPHER_DE).generatePublic(new X509EncodedKeySpec(decoded));

        Cipher cipher = Cipher.getInstance(CIPHER_DE);
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        int inputLength = inputByte.length;
        System.out.println("加密字节书" + inputLength);
        int MAX_ENCRYPT_BLOCK = 128;
        int offSet = 0;
        byte[] resultBytes = {};
        byte[] cache = {};
        while (inputLength - offSet > 0) {
            if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(inputByte, offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(inputByte, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        String s = new String(resultBytes, "UTF-8");
        return s;
    }
}
