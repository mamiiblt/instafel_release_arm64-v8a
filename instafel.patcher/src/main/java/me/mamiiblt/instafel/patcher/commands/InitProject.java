package me.mamiiblt.instafel.patcher.commands;

import java.io.File;
import java.nio.file.Paths;

import me.mamiiblt.instafel.patcher.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.source.SourceManager;
import me.mamiiblt.instafel.patcher.source.SourceUtil;
import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.WorkingDir;

public class InitProject implements Command {

    @Override
    public void execute(String[] args) {
        try {
            String fileArgument = args[0];
            if (args.length != 0) {
                if (fileArgument.contains(".apk") || fileArgument.contains(".zip")) {
                    File apkPath = new File(Paths.get(Environment.USER_DIR, fileArgument).toString());
                    Environment.PROJECT_DIR = WorkingDir.createWorkingDir(apkPath.getAbsolutePath()); 
    
                    SourceManager decompileAPK = new SourceManager(Environment.PROJECT_DIR, apkPath.getName());
                    decompileAPK.copyFrameworksToWorkdir();
                    decompileAPK.setConfig(
                        SourceUtil.getIflConfig(
                            decompileAPK.getConfig(),
                            Environment.PROJECT_DIR
                        ));
                    decompileAPK.decompile();
                    decompileAPK.copyInstafelSources();
                    Log.info("Project succesfully created.");
                } else {
                    Log.warning("Please select an .apk file");
                }
            } else {
                Log.info("Wrong commage usage type, use like that;");
                Log.info("java -jar patcher.jar cwdir instagram.apk");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while running InitProject command.");
            System.exit(-1);
        }
    }
}
