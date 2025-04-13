package me.mamiiblt.instafel.patcher.source;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.PropertyManager;
import me.mamiiblt.instafel.patcher.utils.Utils;

public class PConfig {
    public static enum Keys {
        manifest_version,
        source_dir,
        use_external_ifl_source,
        prod_mode,
        manager_token,
        use_debug_keystore,
        keystore_file,
        keystore_pass,
        keystore_alias,
        keystore_keypass,
        github_pat
    }

    public static String UPDATE_STR = "Updated Config";
    public static File file; 
    public static PropertyManager propertyManager;

    public static void setupConfig() {
        try {
            file = new File(Utils.mergePaths(Environment.PROJECT_DIR, "config.properties"));
            propertyManager = new PropertyManager(file);
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while loading configuration file.");
            System.exit(-1);
        }
    }

    public static void saveProperties() {
        propertyManager.save(UPDATE_STR);
    }

    public static String getString(Keys key, String defaultValue) {
        return propertyManager.getString(key.toString(), defaultValue);
    }

    public static int getInteger(Keys key, int defaultValue) {
        return propertyManager.getInteger(key.toString(), defaultValue);
    }

    public static boolean getBoolean(Keys key, boolean defaultValue) {
        return propertyManager.getBoolean(key.toString(), defaultValue);
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

    public static void createDefaultConfigFile() throws StreamReadException, DatabindException, IOException {
        propertyManager.addInteger(Keys.manifest_version.toString(), 1);
        propertyManager.addString(Keys.source_dir.toString(), "/sources");
        propertyManager.addBoolean(Keys.use_external_ifl_source.toString(), false);
        propertyManager.addBoolean(Keys.prod_mode.toString(), false);
        propertyManager.addString(Keys.manager_token.toString(), "not_needed");
        propertyManager.addBoolean(Keys.use_debug_keystore.toString(), true);
        propertyManager.addString(Keys.keystore_alias.toString(), "");
        propertyManager.addString(Keys.keystore_file.toString(), "");
        propertyManager.addString(Keys.keystore_keypass.toString(), "");
        propertyManager.addString(Keys.keystore_pass.toString(), "");
        propertyManager.addString(Keys.github_pat.toString(), "null");
        saveProperties();
    }
}
