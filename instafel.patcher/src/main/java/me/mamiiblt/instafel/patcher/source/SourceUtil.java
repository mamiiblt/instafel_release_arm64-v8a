package me.mamiiblt.instafel.patcher.source;

import brut.androlib.Config;
import brut.androlib.exceptions.AndrolibException;
import me.mamiiblt.instafel.patcher.utils.Utils;

public class SourceUtil {
    public static Config getIflConfig(Config config, String projectDir) throws AndrolibException {
        config.setBaksmaliDebugMode(false);
        config.setApiLevel(35);
        config.setFrameworkDirectory(Utils.mergePaths(projectDir, "fw"));
        config.setCopyOriginalFiles(false);
        config.setJobs(getSuggesstedThreadCount());
        config.setAaptVersion(2);

        return config;
    }

    public static int getSuggesstedThreadCount() {
        int threadCount = Runtime.getRuntime().availableProcessors();
        return Math.min(threadCount, 8); // use max 8 threads
    }

}
