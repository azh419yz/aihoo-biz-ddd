package com.aihoo.config;

import com.aihoo.util.PropertyReader;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class UploadConfig {

    private static final Properties CONFIG = PropertyReader.getProperties("/config/upload.properties");
    public static final String REAL_PATH = CONFIG.getProperty("RealPath");
    public static final String VIRTUAL_PATH = CONFIG.getProperty("VirtualPath");

}
