package ru.zhadaev;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    private final Properties properties = new Properties();

    private PropertiesReader() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new FileReadException("FileReadException");
        }
    }

    private static class Lazy {
        private static final PropertiesReader instance = new PropertiesReader();
    }

    public static PropertiesReader getInstance() {
        return Lazy.instance;
    }

    String getProperty(String key) {
        return properties.getProperty(key);
    }
}

