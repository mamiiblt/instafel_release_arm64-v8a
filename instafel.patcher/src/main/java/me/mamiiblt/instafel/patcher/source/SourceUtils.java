package me.mamiiblt.instafel.patcher.source;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import brut.androlib.Config;
import me.mamiiblt.instafel.patcher.utils.Env;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;
import me.mamiiblt.instafel.patcher.utils.Utils.OSDedector;

public class SourceUtils {
    public static Config getDefaultIflConfigDecoder(Config config) {
        config.setBaksmaliDebugMode(false);
        config.setJobs(getSuggesstedThreadCount());
        return config;
    }

    public static Config getDefaultIflConfigBuilder(Config config) {
        config.setBaksmaliDebugMode(false);
        config.setJobs(getSuggesstedThreadCount());
        config.setAaptVersion(1); // aapt2 is buggy for Instagram in Apktool now.

        return config;
    }

    public static int getSuggesstedThreadCount() {
        int threadCount = Runtime.getRuntime().availableProcessors();
        return Math.min(threadCount, 8); // use max 8 threads
    }

    public static String getDefaultFrameworkDirectory() {
        File base = new File(System.getProperty("user.home"));
        Path path;
        if (OSDedector.isMac()) {
            path = Paths.get(base.getAbsolutePath(), "Library", "ipatcher", "framework");
        } else if (OSDedector.isWindows()) {
            path = Paths.get(base.getAbsolutePath(), "AppData", "Local", "ipatcher", "framework");
        } else {
            String xdgDataFolder = System.getenv("XDG_DATA_HOME");
            if (xdgDataFolder != null) {
                path = Paths.get(xdgDataFolder, "ipatcher", "framework");
            } else {
                path = Paths.get(base.getAbsolutePath(), ".local", "share", "ipatcher", "framework");
            }
        }
        return path.toString(); 
    }

    public static String createTempSourceDir(String igApkFileName) throws IOException {
        String folderName = igApkFileName.replace(".apk", "") + "_temp";
        File dirPath = new File(Utils.mergePaths(Env.USER_DIR, folderName));
        if (dirPath.exists()) {
            System.exit(-1);
            return null;
        } else {
            FileUtils.forceMkdir(dirPath);
            Log.info("Temp folder for parsing source succesfully created.");
            return dirPath.getAbsolutePath();
        }
    }
}
