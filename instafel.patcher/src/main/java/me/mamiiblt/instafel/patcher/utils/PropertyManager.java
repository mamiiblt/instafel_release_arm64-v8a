package me.mamiiblt.instafel.patcher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {

    private final File propertyFile;
    private Properties properties;

    public PropertyManager(File propertyFile) throws IOException {
        this.propertyFile = propertyFile;
        if (propertyFile != null) {
            parseProperty();
        } else {
            properties = new Properties();
        }
    }

    private void parseProperty() throws IOException {
        properties = new Properties();
        InputStream inputStream = new FileInputStream(propertyFile);
        properties.load(inputStream);
    }

    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getInteger(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(key, String.valueOf(defaultValue)));
    }

    public void addString(String key, String value) {
        properties.setProperty(key, value);
    }

    public void addInteger(String key, int value) {
        properties.setProperty(key, String.valueOf(value));
    }

    public void addBoolean(String key, boolean value) {
        properties.setProperty(key, String.valueOf(value));
    }

    public void save(String comment) {
        try (FileOutputStream output = new FileOutputStream(propertyFile)) {
            properties.store(output, comment);
        } catch (IOException e) {
            Log.severe("Error while saving property file " + propertyFile.getName());
            Log.severe(e.getMessage());
            System.exit(-1);
        }
    }

}
