package com.aihoo.upload;

import com.aihoo.util.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class UploadUtils {
    private static final Logger log = LoggerFactory.getLogger(UploadUtils.class);

    private static final Properties CONFIG = PropertyReader.getProperties("/config/upload.properties");
    private static final String REAL_PATH = CONFIG.getProperty("RealPath");
    private static final String VIRTUAL_PATH = CONFIG.getProperty("VirtualPath");

    public static String uploadFile(MultipartFile img) {
        String realPath = REAL_PATH;
        String fileName = img.getOriginalFilename();
        if (!"".equals(fileName)) {
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            DateFormat dfT = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            realPath += "/" + dfT.format(calendar.getTime());
            fileName = df.format(calendar.getTime())
                    + ((int) (Math.random() * 9000) + 1000)
                    + fileName.substring(fileName.lastIndexOf("."));
            File targetFile = new File(realPath);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            try {
                File upFile = new File(realPath, fileName);
                img.transferTo(upFile);
                return "/" + VIRTUAL_PATH + "/" + dfT.format(calendar.getTime()) + "/" + fileName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            log.info("删除文件失败:{}不存在", fileName);
            return false;
        }
        return file.isFile() ? deleteFile(fileName) : deleteDirectory(fileName);
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.info("删除单个文件{}成功", fileName);
                return true;
            }
            log.info("删除单个文件{}失败", fileName);
            return false;
        }
        log.info("删除单个文件失败：{}不存在", fileName);
        return false;
    }

    public static boolean batchDeleteFile(List<String> fileName) {
        if (CollectionUtils.isEmpty(fileName)) {
            return true;
        }
        for (String url : fileName) {
            String path = REAL_PATH + url.substring(url.lastIndexOf("/"));
            deleteFile(path);
        }
        return true;
    }

    public static boolean deleteDirectory(String dir) {
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            log.info("删除目录失败：{}不存在", dir);
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        if (files != null && files.length != 0) {
            for (File f : files) {
                if (f.isFile()) {
                    flag = deleteFile(f.getAbsolutePath());
                    if (!flag) break;
                } else if (f.isDirectory()) {
                    flag = deleteDirectory(f.getAbsolutePath());
                    if (!flag) break;
                }
            }
        }
        if (!flag) {
            log.info("删除目录失败！");
            return false;
        }
        return dirFile.delete();
    }
}
