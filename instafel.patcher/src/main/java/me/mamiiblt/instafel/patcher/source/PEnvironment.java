package me.mamiiblt.instafel.patcher.source;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.PropertyManager;
import me.mamiiblt.instafel.patcher.utils.Utils;

public class PEnvironment {
    
    public static enum Keys {
        API_BASE,
        INSTAGRAM_VERSION,
        INSTAGRAM_VERSION_CODE,
        GENID,
        INSTAFEL_VERSION,
        APPLIED_PATCHES
    }

    private static String SAVE_STR = "Update Environment File";
    File file; 
    PropertyManager propertyManager;

    public PEnvironment(File envFile) throws IOException {
        this.file = envFile;
        this.propertyManager = new PropertyManager(file);
    }

    public void saveEnv() {
        propertyManager.save(SAVE_STR);
    }

    public String getString(Keys key, String defaultValue) {
        String value = propertyManager.getString(key.toString(), defaultValue);
        return value;
    }

    public int getInteger(Keys key, int defaultValue) {
        try {
            return Integer.parseInt(propertyManager.getString(key.toString(), String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(Keys key, boolean defaultValue) {
        return Boolean.parseBoolean(propertyManager.getString(key.toString(), String.valueOf(defaultValue)));
    }

    public void setString(Keys key, String value) {
        propertyManager.addString(key.toString(), value, SAVE_STR);
    }

    public void setInteger(Keys key, int value) {
        propertyManager.addInteger(key.toString(), value, SAVE_STR);
    }

    public void setBoolean(Keys key, boolean value) {
        propertyManager.addBoolean(key.toString(), value, SAVE_STR);
    }

    public void createDefaultEnvFile() throws StreamReadException, DatabindException, IOException {
        propertyManager.add(Keys.API_BASE.toString(), "api.mamiiblt.me/ifl", SAVE_STR);
        propertyManager.add(Keys.APPLIED_PATCHES.toString(), "", SAVE_STR);
        setIgVerCodeAndVersion();
    }

    private void setIgVerCodeAndVersion() throws StreamReadException, DatabindException, IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        
        JsonNode root = mapper.readTree(new File(
            Utils.mergePaths(Environment.PROJECT_DIR, "sources", "apktool.yml")
        )).get("versionInfo");
        
        propertyManager.add(Keys.INSTAGRAM_VERSION.toString(), root.get("versionName").asText(), SAVE_STR);
        propertyManager.add(Keys.INSTAGRAM_VERSION_CODE.toString(), root.get("versionCode").asText(), SAVE_STR);
    }
}
