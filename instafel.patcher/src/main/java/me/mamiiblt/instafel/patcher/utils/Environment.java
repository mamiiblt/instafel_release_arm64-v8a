package me.mamiiblt.instafel.patcher.utils;

import java.io.InputStream;
import java.util.Properties;

public class Environment extends Configuration {

    public static String PROP_VERSION_STRING = null;
    public static String PROP_COMMIT_HASH = null;
    public static String PROP_PROJECT_TAG = null;    
    public static String PROJECT_DIR = null;

    public static void readPatcherProps()  {
        try {
            Properties patcherProperties = new Properties();
            InputStream in = Environment.class.getClassLoader().getResourceAsStream("patcher.properties");
            patcherProperties.load(in);
    
            PROP_VERSION_STRING = patcherProperties.getProperty("patcher.version");
            PROP_COMMIT_HASH = patcherProperties.getProperty("patcher.commit");
            PROP_PROJECT_TAG = patcherProperties.getProperty("patcher.tag");
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while organizing environment.");
            System.exit(-1);
        }
    }

    public static void printPatcherHeader() {
        System.out.println("Instafel Patcher v" + PROP_VERSION_STRING);
        System.out.println("by mamiiblt");
        System.out.println("");
    }
}
