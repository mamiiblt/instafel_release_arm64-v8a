package me.mamiiblt.instafel.patcher.commands;

import me.mamiiblt.instafel.patcher.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.source.SourceManager;
import me.mamiiblt.instafel.patcher.source.SourceUtils;
import me.mamiiblt.instafel.patcher.source.WorkingDir;
import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;

public class BuildProject implements Command {

    @Override
    public void execute(String[] args) {
        try {
            if (args.length != 0) {
                String folderArg = args[0];
                Environment.PROJECT_DIR = WorkingDir.getExistsWorkingDir(folderArg);
                SourceManager sourceManager = new SourceManager();
                sourceManager.setConfig(SourceUtils.getDefaultIflConfig(sourceManager.getConfig()));
                sourceManager.getConfig().setFrameworkDirectory(SourceUtils.getDefaultFrameworkDirectory());
                sourceManager.build();
                Log.info("APKs succesfuly generated at /build folder.");
            } else {
                Log.info("Wrong commage usage type, use like that;");
                Log.info("java -jar patcher.jar build instagram");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while running command");
            System.exit(-1);
        }
    }

}
