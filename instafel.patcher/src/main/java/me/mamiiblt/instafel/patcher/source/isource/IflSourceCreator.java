package me.mamiiblt.instafel.patcher.source.isource;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;

public class IflSourceCreator {
    public static String createTempSourceDir(String igApkFileName) throws IOException {
        String folderName = igApkFileName.replace(".apk", "") + "_temp";
        File dirPath = new File(Utils.mergePaths(Environment.USER_DIR, folderName));
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
