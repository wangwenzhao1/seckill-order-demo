package com.seckill.demo.demo.config;

import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class ConfigHelper {

	@Autowired
    private AccountConfigProperties aap;

    private static Properties properties = null;

    public static String getConfig(final String name, final String defaultValue) {

        return properties.getProperty(name, defaultValue);
    }

    public static long getConfig(final String name, final long defaultValue) {

        String value = getConfig(name, String.valueOf(defaultValue));
        long v = Long.parseLong(value);

        return v;
    }

    @PostConstruct
    private void loadConfig() {
    	properties = new Properties();
    	Map<String, String> pp = aap.getProperty();
    	properties.putAll(pp);
    }

}