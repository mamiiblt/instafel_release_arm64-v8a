package me.mamiiblt.instafel.patcher.source;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import me.mamiiblt.instafel.patcher.utils.PropertyManager;

public class PConfig {
    public static enum Keys {
        manifest_version,
        source_dir,
        use_external_ifl_source,
        prod_mode,
        manager_token
    }

    private static String UPDATE_STR = "Updated Config";
    File file; 
    PropertyManager propertyManager;
    Properties properties;

    public PConfig(File envFile) throws IOException {
        this.file = envFile;
        this.propertyManager = new PropertyManager(file);
        this.properties = propertyManager.getProperties();
    }

    public void saveConfig() {
        propertyManager.save(UPDATE_STR);
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
        propertyManager.save("Update Config File");
    }

    public void setInteger(Keys key, int value) {
        properties.setProperty(key.toString(), String.valueOf(value));
        propertyManager.save("Update Config File");
    }

    public void setBoolean(Keys key, boolean value) {
        properties.setProperty(key.toString(), String.valueOf(value));
        propertyManager.save("Update Config File");
    }

    public void createDefaultConfigFile() throws StreamReadException, DatabindException, IOException {
        propertyManager.add(Keys.manifest_version.toString(), "1", UPDATE_STR);
        propertyManager.addString(Keys.source_dir.toString(), "/sources", UPDATE_STR);
        propertyManager.addBoolean(Keys.use_external_ifl_source.toString(), false, UPDATE_STR);
        propertyManager.addBoolean(Keys.prod_mode.toString(), false, UPDATE_STR);
        propertyManager.addString(Keys.manager_token.toString(), "not_needed", UPDATE_STR);
        propertyManager.save("Update Config File");
    }
}
