package com.remittance.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Currency;
import java.util.Properties;

import org.apache.log4j.Logger;


public class ApplicationUtil {

    private static Properties properties = new Properties();

    static Logger log = Logger.getLogger(ApplicationUtil.class);

    public static void loadConfig(String fileName) {
        if (fileName == null) {
            log.warn("loadConfig: config file name cannot be null");
        } else {
            try {
                log.info("loadConfig(): Loading config file: " + fileName );
                final InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
                properties.load(fis);

            } catch (IOException fne) {
                log.error("loadConfig(): file name not found " + fileName, fne);
            }
        }
    }

    public static String getStringProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            value = System.getProperty(key);
        }
        return value;
    }

    /**
     * @param key:       property key
     * @param defaultVal the default value if the key not present in config file
     * @return string property based on lookup key
     */
    public static String getStringProperty(String key, String defaultVal) {
        String value = getStringProperty(key);
        return value != null ? value : defaultVal;
    }


    public static int getIntegerProperty(String key, int defaultVal) {
        String valueStr = getStringProperty(key);
        if (valueStr == null) {
            return defaultVal;
        } else {
            try {
                return Integer.parseInt(valueStr);
            } catch (Exception e) {
                log.warn("getIntegerProperty(): cannot parse integer from properties file for: " + key + "fail over to default value: " + defaultVal, e);
                return defaultVal;
            }
        }
    }

    //initialise

    static {
        String configFileName = System.getProperty("application.properties");

        if (configFileName == null) {
            configFileName = "application.properties";
        }
        loadConfig(configFileName);
    }
    
    public static boolean validateCcyCode(String inputCcyCode) {
        try {
            Currency instance = Currency.getInstance(inputCcyCode);
            if(log.isDebugEnabled()){
                log.debug("Validate Currency Code: " + instance.getSymbol());
            }
            return instance.getCurrencyCode().equals(inputCcyCode);
        } catch (Exception e) {
            log.warn("Cannot parse the input Currency Code, Validation Failed: ", e);
        }
        return false;
    }
}