package ru.zhadaev.config;

import ru.zhadaev.exception.FileReadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    private static final Properties properties = new Properties();

    public PropertiesReader(String resourcePath) {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourcePath);
            properties.load(inputStream);
        } catch (IOException e) {
            throw new FileReadException("Couldn't load properties");
        }
    }

    /**
     * @param key -> property key
     * @return not empty string property value
     * @throws IllegalArgumentException if key is null
     */
    public String getProperty(String key) {
        String property = properties.getProperty(key);
        if (property != null && !property.isEmpty()) return property;
        else throw new IllegalArgumentException("Not found property " + key);
    }
}

