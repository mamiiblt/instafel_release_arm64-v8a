package me.mamiiblt.instafel.patcher.source;

import org.apache.commons.io.FileUtils;

import me.mamiiblt.instafel.patcher.utils.Env;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;

import java.io.File;
import java.io.IOException;

public class WorkingDir {

    public static String createWorkingDir(String igApkFileName) throws IOException {
        String folderName = igApkFileName.replace(".apk", "");
        File dirPath = new File(Utils.mergePaths(Env.USER_DIR, folderName));
        if (dirPath.exists()) {
            Log.severe("Working directory for this apk is already exists, delete it or continue from the project.");
            System.exit(-1);
            return null;
        } else {
            FileUtils.forceMkdir(dirPath);
            Log.info("Project working directory succesfully created.");
            return dirPath.getAbsolutePath();
        }
    }

    public static String getExistsWorkingDir(String folderName) {
        File dirPath = new File(Utils.mergePaths(Env.USER_DIR, folderName));
        if (dirPath.isDirectory()) {
            if (dirPath.exists()) {
                return dirPath.getAbsolutePath();
            } else {
                Log.severe("Working directory not exists, please create it or use different working dir.");
                System.exit(-1);
                return null;
            }
        } else {
            Log.severe("Its's not an directory bro...");
            System.exit(-1);
            return null;
        }
    }
}
