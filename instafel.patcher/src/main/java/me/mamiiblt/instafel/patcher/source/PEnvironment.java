package me.mamiiblt.instafel.patcher.source;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
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

    public static String SAVE_STR = "Update Environment File";
    public static File file; 
    public static PropertyManager propertyManager;

    public static void setupEnv() {
        try {
            file = new File(
                Utils.mergePaths(Environment.PROJECT_DIR, "env.properties")
            );
            propertyManager = new PropertyManager(file);
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while loading environment file.");
            System.exit(-1);
        }
    }

    public static void saveProperties() {
        propertyManager.save(SAVE_STR);
    }

    public static String getString(Keys key, String defaultValue) {
        String value = propertyManager.getString(key.toString(), defaultValue);
        return value;
    }

    public static int getInteger(Keys key, int defaultValue) {
        try {
            return Integer.parseInt(propertyManager.getString(key.toString(), String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(Keys key, boolean defaultValue) {
        return Boolean.parseBoolean(propertyManager.getString(key.toString(), String.valueOf(defaultValue)));
    }

    public static void setString(Keys key, String value) {
        propertyManager.addString(key.toString(), value);
    }

    public static void setInteger(Keys key, int value) {
        propertyManager.addInteger(key.toString(), value);
    }

    public static void setBoolean(Keys key, boolean value) {
        propertyManager.addBoolean(key.toString(), value);
    }

    public static void createDefaultEnvFile() throws StreamReadException, DatabindException, IOException {
        propertyManager.addString(Keys.API_BASE.toString(), "api.mamiiblt.me/ifl");
        propertyManager.addString(Keys.APPLIED_PATCHES.toString(), "");
        setIgVerCodeAndVersion();
    }

    private static void setIgVerCodeAndVersion() throws StreamReadException, DatabindException, IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        
        JsonNode root = mapper.readTree(new File(
            Utils.mergePaths(Environment.PROJECT_DIR, "sources", "apktool.yml")
        )).get("versionInfo");
        
        propertyManager.addString(Keys.INSTAGRAM_VERSION.toString(), root.get("versionName").asText());
        propertyManager.addString(Keys.INSTAGRAM_VERSION_CODE.toString(), root.get("versionCode").asText());
    }
}
