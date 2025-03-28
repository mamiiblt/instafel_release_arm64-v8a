package me.mamiiblt.instafel.patcher.utils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class PEnvironment {
    
    public static enum Keys {
        API_BASE,
        KS_FILE_PATH,
        KS_KEY_ALIAS,
        KS_KEY_PASS,
        KS_PASS,
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

    public String get(Keys key) {
        return properties.getProperty(key.toString());
    }

    public void set(Keys key, String value) {
        properties.setProperty(key.toString(), value);
        propertyManager.save("Update Environment File");
    }

    public void createDefaultEnvFile() {
        propertyManager.add(Keys.API_BASE.toString(), "api.mamiiblt.me/ifl");
        propertyManager.save("Update Environment File");
    }
}
