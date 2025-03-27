package me.mamiiblt.instafel.patcher.source;

import org.apache.commons.io.FileUtils;

import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.PropertyManager;
import me.mamiiblt.instafel.patcher.utils.Utils;

import java.io.File;
import java.io.IOException;

public class WorkingDir {

    public static String createWorkingDir(String igApkFileName) throws IOException {
        String folderName = igApkFileName.replace(".apk", "");
        File dirPath = new File(Utils.mergePaths(Environment.USER_DIR, folderName));
        if (dirPath.exists()) {
            Log.severe("Working dir already exists, delete it or continue from the project.");
            System.exit(-1);
            return null;
        } else {
            FileUtils.forceMkdir(dirPath);
            Log.info("Project working directory succesfully created.");
            return dirPath.getAbsolutePath();
        }
    }

    public static String getExistsWorkingDir(String folderName) {
        File dirPath = new File(Utils.mergePaths(Environment.USER_DIR, folderName));
        if (dirPath.isDirectory()) {
            if (dirPath.exists()) {
                return dirPath.getAbsolutePath();
            } else {
                Log.severe("Working dir not exists, please create it or use different working dir.");
                System.exit(-1);
                return null;
            }
        } else {
            Log.severe("Its's not an directory.");
            System.exit(-1);
            return null;
        }
    }

    public static void createConfigFile() throws IOException {
        PropertyManager propertyManager = new PropertyManager(
            new File(Utils.mergePaths(Environment.PROJECT_DIR, "config.properties"))
        );
        propertyManager.addInteger("manifest_version", 1);
        propertyManager.addBoolean("production_mode", false);
        propertyManager.addString("custom_fw_folder_dir", "");
        propertyManager.addString("source_dir", "/sources");
        propertyManager.save();
    }

    public static void createEmptyEnvironmentFile() {

    }
}
