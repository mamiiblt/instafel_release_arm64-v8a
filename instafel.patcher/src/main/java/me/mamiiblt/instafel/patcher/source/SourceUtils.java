package me.mamiiblt.instafel.patcher.source;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import brut.androlib.Config;
import brut.androlib.exceptions.AndrolibException;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.PropertyManager;
import me.mamiiblt.instafel.patcher.utils.Utils.OSDedector;

public class SourceUtils {
    public static Config getDefaultIflConfig(Config config) throws AndrolibException {
        config.setBaksmaliDebugMode(false);
        config.setApiLevel(35);
        config.setCopyOriginalFiles(false);
        config.setJobs(getSuggesstedThreadCount());
        config.setAaptVersion(1);

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
}
