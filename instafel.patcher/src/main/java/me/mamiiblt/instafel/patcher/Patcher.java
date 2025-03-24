package me.mamiiblt.instafel.patcher;

import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;


public class Patcher {
    public static void main(String[] args)  {

        System.setProperty("java.awt.headless", "true");
        System.setProperty("jdk.nio.zipfs.allowDotZipEntry", "true");
        System.setProperty("jdk.util.zip.disableZip64ExtraFieldValidation", "true");

        Log.setLogger();

        Environment.organizeEnvironment();
        Environment.printPatcherHeader();
        Environment.configurePatcher(args);
    }
}