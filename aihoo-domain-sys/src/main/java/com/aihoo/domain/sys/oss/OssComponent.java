package com.aihoo.domain.sys.oss;

import com.aihoo.properties.AliCloudProperties;
import com.aihoo.util.TimeUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Component
public class OssComponent {

    private static final Logger log = LoggerFactory.getLogger(OssComponent.class);

    private final OSS ossClient;
    private final String bucketName;
    private final String imgHeadUrl;

    public OssComponent(AliCloudProperties props) {
        this.bucketName = props.getOss().getBucketName();
        this.imgHeadUrl = props.getOss().getImgHeadUrl();
        this.ossClient = new OSSClientBuilder()
                .build(props.getOss().getEndpoint(), props.getAk(), props.getSk());
    }

    public String getUrl(String key) {
        return imgHeadUrl + key;
    }

    public String generateFileName(String originalFilename, String directory) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String[] split = originalFilename.split("[.]");
        String extension = split[split.length - 1];
        StringBuilder fileName = new StringBuilder();
        fileName.append(directory).append("/");
        if ("message".equals(directory)) {
            fileName.append(TimeUtil.getDayTime(new Date(), "yyyyMMdd")).append("/");
        }
        fileName.append(TimeUtil.getDayTime(new Date(), "yyyyMMddHHmmss"));
        fileName.append(uuid);
        fileName.append(".").append(extension);
        return fileName.toString();
    }

    public String uploadDicom(MultipartFile multipartFile, String fileName) throws OSSException, ClientException, IOException {
        ObjectMetadata om = new ObjectMetadata();
        om.setContentLength(multipartFile.getSize());
        if (!fileName.startsWith("dicom/")) {
            fileName = "dicom/" + fileName;
        }
        ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(multipartFile.getBytes()), om);
        return imgHeadUrl + fileName;
    }

    public String uploadFile(MultipartFile multipartFile, String path) throws OSSException, ClientException, IOException {
        ObjectMetadata om = new ObjectMetadata();
        om.setContentLength(multipartFile.getSize());
        String fileName = generateFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()), path + "/files");
        ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(multipartFile.getBytes()), om);
        return imgHeadUrl + fileName;
    }

    public String uploadFileURL(String fileUrl) {
        ObjectMetadata om = new ObjectMetadata();
        String fileName = generateFileName(fileUrl, "files");
        try (InputStream inputStream = getInputStreamByUrl(fileUrl)) {
            ossClient.putObject(bucketName, fileName, inputStream, om);
            return imgHeadUrl + fileName;
        } catch (Exception e) {
            log.error("Upload file URL failed: {}", e.getMessage(), e);
            throw new RuntimeException("Upload file failed", e);
        }
    }

    public InputStream getInputStreamByUrl(String strUrl) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try (InputStream inputStream = conn.getInputStream()) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, len);
                }
            }
            return new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            log.error("Get input stream by URL failed: {}", e.getMessage(), e);
            throw new RuntimeException("Get input stream failed", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}