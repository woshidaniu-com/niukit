package com.woshidaniu.niuca.tp.cas.util;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.oro.util.CacheLRU;

public class TestProperties {
    public static CacheLRU cache = new CacheLRU();

    public TestProperties() {
    }

    public static String readValue(String filePath, String key) {
        String getValue = (String)cache.getElement(key);
        if (getValue != null) {
            return getValue;
        } else {
            Properties props = new Properties();

            try {
                InputStream in = new BufferedInputStream(new FileInputStream(filePath));
                props.load(in);
                String value = props.getProperty(key);
                cache.addElement(key, value);
                return value;
            } catch (Exception var6) {
                var6.printStackTrace();
                return null;
            }
        }
    }

    public static void readProperties(String filePath) {
        Properties props = new Properties();

        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            props.load(in);
            Enumeration en = props.propertyNames();

            while(en.hasMoreElements()) {
                String key = (String)en.nextElement();
                String Property = props.getProperty(key);
                System.out.println(key + Property);
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public static void writeProperties(String filePath, String parameterName, String parameterValue) {
        Properties prop = new Properties();

        try {
            InputStream fis = new FileInputStream(filePath);
            prop.load(fis);
            OutputStream fos = new FileOutputStream(filePath);
            prop.setProperty(parameterName, parameterValue);
            prop.store(fos, "Update '" + parameterName + "' value");
        } catch (IOException var6) {
            System.err.println("Visit " + filePath + " for updating " + parameterName + " value error");
        }

    }

    public static void main(String[] args) {
        readValue("info.properties", "url");
        writeProperties("info.properties", "age", "21");
        readProperties("info.properties");
        System.out.println("OK");
    }
}
