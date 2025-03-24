package me.mamiiblt.instafel.patcher.utils;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WorkingDir {

    public String createWorkingDir(String igApkFileName) throws IOException {
        String folderName = igApkFileName.replace(".apk", "");
        File dirPath = new File(Environment.USER_DIR + folderName);
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

    public String getExistsWorkingDir(String folderName) {
        File dirPath = new File(Environment.USER_DIR + folderName);
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

    public void createConfigFile(String projectDir) throws IOException {
        JSONObject configContent = new JSONObject();
        configContent.put("manifest_version", 1);
        configContent.put("patcher_commit", Environment.PROP_COMMIT_HASH);

        FileUtils.writeStringToFile(
                new File(projectDir + "/config.json"),
                configContent.toString(2),
                StandardCharsets.UTF_8
        );
        Log.info("Configuration file created.");
    }
}
