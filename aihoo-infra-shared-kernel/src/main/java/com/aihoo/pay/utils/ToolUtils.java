package com.aihoo.pay.utils;


import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

/**
 * Created by john on 14-5-7.
 */
@Slf4j
public class ToolUtils {

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成md5加密后的字符串
     *
     * @param str 得加密的字符串
     * @return
     * @auther john 新增日期:2014/7/8
     */
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            //log.error("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuilder md5StrBuff = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    /**
     * 获取文件后缀
     *
     * @param filename
     * @return
     * @auther john 新增日期:2014/7/8
     */
    public static String getSuffix(String filename) {
        String suffix = "";
        int pos = filename.lastIndexOf('.');
        if (pos > 0 && pos < filename.length() - 1) {
            suffix = filename.substring(pos + 1);
        }
        return suffix;
    }

    public static String getMimeType(File file) {
        String mimetype = "";
        if (file.exists()) {
            if (getSuffix(file.getName()).equalsIgnoreCase("png")) {
                mimetype = "image/png";
            } else if (getSuffix(file.getName()).equalsIgnoreCase("jpg")) {
                mimetype = "image/jpg";
            } else if (getSuffix(file.getName()).equalsIgnoreCase("jpeg")) {
                mimetype = "image/jpeg";
            } else if (getSuffix(file.getName()).equalsIgnoreCase("gif")) {
                mimetype = "image/gif";
            } else {
                try {
                    mimetype = java.nio.file.Files.probeContentType(file.toPath());
                } catch (java.io.IOException e) {
                    mimetype = "";
                }
            }
        }
        return mimetype;
    }

    /**
     * 获取新文件名
     *
     * @param fileName 旧文件名
     * @return
     * @auther john 新增日期:2014/7/9
     */
    public static String getNewFileName(String fileName) {
        return getUuid() + "." + getSuffix(fileName);
    }


    /**
     * @param str 待MD5加密的字符串
     * @return
     * @auther :john
     * @description :将要加密的字符中追加混淆码后进行md5加密
     * @create :2015/3/26
     */
    public static String getMd5EncryptAppend(String str, String encrypt) {
        return getMD5Str(str + encrypt);
    }


    public static String getMd516(String source) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result.substring(8, 24);
    }

    /**
     * 生成验证码
     *
     * @return
     */
    public static String generateMessageCode() {
        //生成随机验证码
        String code = "";
        for (int i = 0; i < 6; i++) {
            code += SignConstants.SMS_CODE_ARR.charAt(new Random().nextInt(10));
        }
        return code;
    }

}
