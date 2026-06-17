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

/**
 * java RSA加密工具
 **/
public class RsaUtil {
    /**
     * 加密算法
     */
    private static final String CIPHER_DE = "RSA";
    /**
     * 密度长度 于 原文长度对应 以及越长速度越慢
     */
    private final static int KEY_SIZE = 1024;

    /**
     * 随机生成密钥对
     */
    public static Map<Integer, String> genKeyPair() throws Exception {
        //用于封装随机长生的公钥与私钥
        Map<Integer, String> keyMap = new HashMap<Integer, String>();
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器
        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
        // 生成一个密钥对,保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        PrivateKey privateKey = keyPair.getPrivate();
        // 得到公钥
        PublicKey publicKey = keyPair.getPublic();
        // 得到公钥字符串
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        // 得到私钥字符串
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        // 将公钥 map
        keyMap.put(0, publicKeyString);
        // 私钥 存入map
        keyMap.put(1, privateKeyString);
        return keyMap;
    }

    /**
     * RSA 公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     */
    public static String encryptPublicKey(String str, String publicKey) throws Exception {
        //base64编码的公钥(解码 公钥)
        byte[] decode = Base64.getDecoder().decode(publicKey);
        //加密方法
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(CIPHER_DE).generatePublic(new X509EncodedKeySpec(decode));
        //RSA
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

    /**
     * RSA 私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     */
    public static String decoderPrivateKey(String str, String privateKey) throws Exception {
        // base64解码加密后的字符串
        byte[] inputByte = Base64.getDecoder().decode(str);
        // base64解码的私钥
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(CIPHER_DE).generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA
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

    /**
     * RSA 私钥加密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 密文
     */
    public static String encryptPrivateKey(String str, String privateKey) throws Exception {
        // base64解码的私钥
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(CIPHER_DE).generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA
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

    /**
     * RSA 公钥解密
     *
     * @param str        加密字符串
     * @param privateKey 公钥
     * @return 铭文
     */
    public static String decoderPublicKey(String str, String privateKey) throws Exception {
        // base64解码加密后的字符串
        byte[] inputByte = Base64.getDecoder().decode(str);
        // base64解码的私钥
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(CIPHER_DE).generatePublic(new X509EncodedKeySpec(decoded));
        //RSA
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
