package com.billy.gmrpc.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertyUtils {
    private volatile static PropertyUtils instance = null;
    private Properties properties;

    private PropertyUtils() {
        initProperty();
    }

    public static PropertyUtils getInstance () {
        if (instance == null) {
            synchronized (PropertyUtils.class) {
                if (instance == null) {
                    instance = new PropertyUtils();
                }
            }
        }
        return instance;
    }

    private void initProperty() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            log.error("call PropertyUtils.initProperty error, error message:{}", e.getMessage(), e);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
