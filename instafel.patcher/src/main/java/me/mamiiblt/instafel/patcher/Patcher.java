package me.mamiiblt.instafel.patcher;

import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.cmdhandler.CommandHandler;

public class Patcher {
    public static void main(String[] args)  {

        System.setProperty("java.awt.headless", "true");
        System.setProperty("jdk.nio.zipfs.allowDotZipEntry", "true");
        System.setProperty("jdk.util.zip.disableZip64ExtraFieldValidation", "true");

        Log.setupLogger();
        Environment.readPatcherProps();
        new CommandHandler(args);
    }
}