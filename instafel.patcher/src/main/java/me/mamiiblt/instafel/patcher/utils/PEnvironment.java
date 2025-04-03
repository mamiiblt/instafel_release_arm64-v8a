package me.mamiiblt.instafel.patcher.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class PEnvironment {
    
    public static enum Keys {
        PRODUCTION_MODE,
        API_BASE,
        MANAGER_TOKEN,
        INSTAGRAM_VERSION,
        INSTAGRAM_VERSION_CODE,
        GENERATION_ID,
        IFL_VERSION,
    }

    File file; 
    PropertyManager propertyManager;
    Properties properties;

    public PEnvironment(File envFile) throws IOException {
        this.file = envFile;
        this.propertyManager = new PropertyManager(file);
        this.properties = propertyManager.getProperties();
    }

    public String getString(Keys key, String defaultValue) {
        return properties.getProperty(key.toString(), defaultValue);
    }

    public int getInteger(Keys key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key.toString(), String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(Keys key, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(key.toString(), String.valueOf(defaultValue)));
    }

    public void setString(Keys key, String value) {
        properties.setProperty(key.toString(), value);
        propertyManager.save("Update Environment File");
    }

    public void setInteger(Keys key, int value) {
        properties.setProperty(key.toString(), String.valueOf(value));
        propertyManager.save("Update Environment File");
    }

    public void setBoolean(Keys key, boolean value) {
        properties.setProperty(key.toString(), String.valueOf(value));
        propertyManager.save("Update Environment File");
    }

    public void createDefaultEnvFile() throws StreamReadException, DatabindException, IOException {
        propertyManager.add(Keys.PRODUCTION_MODE.toString(), "false");
        propertyManager.add(Keys.API_BASE.toString(), "api.mamiiblt.me/ifl");
        propertyManager.add(Keys.MANAGER_TOKEN.toString(), "none");        
        setIgVerCodeAndVersion();
        propertyManager.save("Update Environment File");
    }

    private void setIgVerCodeAndVersion() throws StreamReadException, DatabindException, IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        
        JsonNode root = mapper.readTree(new File(
            Utils.mergePaths(Environment.PROJECT_DIR, "sources", "apktool.yml")
        )).get("versionInfo");
        
        propertyManager.add(Keys.INSTAGRAM_VERSION.toString(), root.get("versionName").asText());
        propertyManager.add(Keys.INSTAGRAM_VERSION_CODE.toString(), root.get("versionCode").asText());
    }
}
