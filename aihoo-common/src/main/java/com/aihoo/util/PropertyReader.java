package com.aihoo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

public class PropertyReader {

    private static final Logger log = LoggerFactory.getLogger(PropertyReader.class);
    private static final Hashtable<String, Properties> pptContainer = new Hashtable<>();

    public static String getValue(String propertyFilePath, String key) {
        Properties ppts = getProperties(propertyFilePath);
        return ppts == null ? null : ppts.getProperty(key);
    }

    public static String getValue(String propertyFilePath, String key, boolean isAbsolutePath) {
        if (isAbsolutePath) {
            Properties ppts = getPropertiesByFs(propertyFilePath);
            return ppts == null ? null : ppts.getProperty(key);
        }
        return getValue(propertyFilePath, key);
    }

    public static Properties getProperties(String propertyFilePath) {
        if (propertyFilePath == null) {
            log.error("propertyFilePath is null!");
            return null;
        }
        Properties ppts = pptContainer.get(propertyFilePath);
        if (ppts == null) {
            ppts = loadPropertyFile(propertyFilePath);
            if (ppts != null) {
                pptContainer.put(propertyFilePath, ppts);
            }
        }
        return ppts;
    }

    public static Properties getPropertiesByFs(String propertyFilePath) {
        if (propertyFilePath == null) {
            log.error("propertyFilePath is null!");
            return null;
        }
        Properties ppts = pptContainer.get(propertyFilePath);
        if (ppts == null) {
            ppts = loadPropertyFileByFileSystem(propertyFilePath);
            if (ppts != null) {
                pptContainer.put(propertyFilePath, ppts);
            }
        }
        return ppts;
    }

    private static Properties loadPropertyFile(String propertyFilePath) {
        java.io.InputStream is = PropertyReader.class.getResourceAsStream(propertyFilePath);
        if (is == null) {
            return loadPropertyFileByFileSystem(propertyFilePath);
        }
        Properties ppts = new Properties();
        try {
            ppts.load(is);
            return ppts;
        } catch (Exception e) {
            log.debug("加载属性文件出错:" + propertyFilePath, e);
            return null;
        }
    }

    private static Properties loadPropertyFileByFileSystem(String propertyFilePath) {
        try {
            Properties ppts = new Properties();
            ppts.load(new java.io.FileInputStream(propertyFilePath));
            return ppts;
        } catch (FileNotFoundException e) {
            log.error("FileInputStream({})! FileNotFoundException", propertyFilePath, e);
            return null;
        } catch (IOException e) {
            log.error("ConfigUtil.load(InputStream)! IOException", e);
            return null;
        }
    }

    public static boolean setValueAndStore(String propertyFilePath, Hashtable<String, String> htKeyValue) {
        return setValueAndStore(propertyFilePath, htKeyValue, null);
    }

    public static boolean setValueAndStore(String propertyFilePath, Hashtable<String, String> htKeyValue, String storeMsg) {
        Properties ppts = getProperties(propertyFilePath);
        if (ppts == null || htKeyValue == null) {
            return false;
        }
        ppts.putAll(htKeyValue);
        java.io.OutputStream stream = null;
        try {
            stream = new java.io.FileOutputStream(propertyFilePath);
        } catch (FileNotFoundException e) {
            log.debug("propertyFilePath = {}", propertyFilePath);
            try {
                String path = PropertyReader.class.getResource(propertyFilePath).getPath();
                stream = new java.io.FileOutputStream(path);
            } catch (Exception ex) {
                log.error("FileNotFoundException! path={}", propertyFilePath, ex);
                return false;
            }
        }
        if (stream == null) {
            return false;
        }
        try {
            ppts.store(stream, storeMsg != null ? storeMsg : "set value and store.");
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (stream != null) {
                try { stream.close(); } catch (IOException ignored) { }
            }
        }
    }

    public static boolean createPropertiesFile(String propertyFilePath, Hashtable<String, String> htKeyValue) {
        java.io.File file = new java.io.File(propertyFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) { }
        }
        return setValueAndStore(propertyFilePath, htKeyValue, "create properties file:" + file.getName());
    }

    public static boolean setValue(String propertyFilePath, String key, String value) {
        Properties ppts = getProperties(propertyFilePath);
        if (ppts == null) {
            return false;
        }
        ppts.put(key, value);
        return true;
    }

    public static void store(Properties properties, String propertyFilePath, String msg) {
        try {
            properties.store(new java.io.FileOutputStream(propertyFilePath), msg);
        } catch (FileNotFoundException e) {
            log.error("FileOutputStream({})", propertyFilePath, e);
        } catch (IOException e) {
            log.error("store exception", e);
        }
    }

    public static String removeValue(String propertyFilePath, String key) {
        Properties ppts = getProperties(propertyFilePath);
        if (ppts == null) {
            return null;
        }
        return (String) ppts.remove(key);
    }

    public static Properties removeValue(String propertyFilePath, String[] key) {
        if (key == null) {
            log.error("key[] is null!");
            return null;
        }
        Properties ppts = getProperties(propertyFilePath);
        if (ppts == null) {
            return null;
        }
        for (String k : key) {
            ppts.remove(k);
        }
        return ppts;
    }

    public static boolean removeValueAndStore(String propertyFilePath, String[] key) {
        Properties ppts = removeValue(propertyFilePath, key);
        if (ppts == null) {
            return false;
        }
        store(ppts, propertyFilePath, "batch remove key value!");
        return true;
    }

    public static boolean updateValue(String propertyFilePath, String key, String newValue) {
        if (key == null || newValue == null) {
            log.error("key or newValue is null!");
            return false;
        }
        Hashtable<String, String> ht = new Hashtable<>();
        ht.put(key, newValue);
        return setValueAndStore(propertyFilePath, ht, "update " + key + "'s value!");
    }

    public static boolean batchUpdateValue(String propertyFilePath, Hashtable<String, String> htKeyValue) {
        if (propertyFilePath == null || htKeyValue == null) {
            return false;
        }
        return setValueAndStore(propertyFilePath, htKeyValue, "batch update key value!");
    }

    public static Properties removePropertyFile(String propertyFilePath) {
        return pptContainer.remove(propertyFilePath);
    }

    public static void reloadPropertyFile(String propertyFilePath) {
        pptContainer.remove(propertyFilePath);
        loadPropertyFile(propertyFilePath);
    }

    public static String getPpropertyFilePath(String pkg, String propertyFileName) {
        pkg = pkg == null ? "" : pkg.replaceAll("\\.", "/");
        propertyFileName = propertyFileName.endsWith(".properties") ? propertyFileName : (propertyFileName + ".properties");
        return "/" + pkg + "/" + propertyFileName;
    }
}
