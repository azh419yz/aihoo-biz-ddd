package com.aihoo.domain.sys.util;

import com.aihoo.util.TimeUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class OssFileUtils {

    private final static String endpoint = System.getenv().getOrDefault("OSS_ENDPOINT", "oss-accelerate.aliyuncs.com");

    private final static String imgUrl = "?x-oss-process=image/auto-orient,1/interlace,1/resize,p_50/quality,q_60/sharpen,90";

    private final static String accessKeyId = System.getenv().getOrDefault("OSS_ACCESS_KEY_ID", "");
    private final static String accessKeySecret = System.getenv().getOrDefault("OSS_ACCESS_KEY_SECRET", "");
    private final static String bucketName = System.getenv().getOrDefault("OSS_BUCKET_NAME", "internet-hospital-prod");
    private final static String imgHeadUrl = System.getenv().getOrDefault("OSS_HEAD_URL",
            "https://internet-hospital-prod.oss-accelerate.aliyuncs.com/");

    private static OSS client;

    static {
        client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    public static String getUrl(String key) {
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, key);
        URL url = client.generatePresignedUrl(req);
        return url.toString();
    }

    public static StringBuffer getFileName(String originalFilename, String fileeName) {
        StringBuffer sb = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String[] split = originalFilename.split("[.]");
        sb.append(fileeName).append("/");
        if ("message".equals(fileeName)) {
            String pattern = "yyyyMMdd";
            sb.append(TimeUtil.getDayTime(new Date(), pattern)).append("/");
        }
        sb.append(TimeUtil.getDayTime(new Date(), "yyyyMMddHHmmss"));
        sb.append(uuid);
        sb.append(".").append(split[split.length - 1]);
        return sb;
    }

    public static String uploadFile(MultipartFile multipartFile)
            throws OSSException, ClientException, FileNotFoundException {
        ObjectMetadata om = new ObjectMetadata();
        om.setContentLength(multipartFile.getSize());
        StringBuffer fileName = getFileName(
                Objects.requireNonNull(multipartFile.getOriginalFilename()), "files");
        try {
            client.putObject(bucketName, fileName.toString(),
                    new ByteArrayInputStream(multipartFile.getBytes()), om);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgHeadUrl + fileName;
    }

    public static String uploadFileURL(String fileUrl) {
        ObjectMetadata om = new ObjectMetadata();
        StringBuffer fileName = getFileName(fileUrl, "files");
        InputStream inputStreamByUrl = getInputStreamByUrl(fileUrl);
        client.putObject(bucketName, fileName.toString(), inputStreamByUrl, om);
        return imgHeadUrl + fileName;
    }

    public static InputStream getInputStreamByUrl(String strUrl) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try (InputStream is = conn.getInputStream()) {
                byte[] buf = new byte[4096];
                int len;
                while ((len = is.read(buf)) != -1) {
                    output.write(buf, 0, len);
                }
            }
            return new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.disconnect(); } catch (Exception ignored) { }
            }
        }
        return null;
    }
}
