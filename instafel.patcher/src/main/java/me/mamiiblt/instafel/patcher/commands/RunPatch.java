package me.mamiiblt.instafel.patcher.commands;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import me.mamiiblt.instafel.patcher.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.source.PConfig;
import me.mamiiblt.instafel.patcher.source.PEnvironment;
import me.mamiiblt.instafel.patcher.source.WorkingDir;
import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatchGroup;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PatchLoader;

public class RunPatch implements Command {

    @Override
    public void execute(String[] args) {

        String wdirFolder = args[0];
        Environment.PROJECT_DIR = WorkingDir.getExistsWorkingDir(wdirFolder);
        Environment.PEnvironment = getEnv();
        Environment.PConfig = getConfig();

        List<InstafelPatch> runnablePatches = new ArrayList<>();
        Log.info("Loading patches...");
        for (int i = 1; i < args.length; i++) {
            String shortName = args[i];
            
            InstafelPatch patch = PatchLoader.findPatchByShortname(shortName);
            if (patch != null) {
                Log.info("Patch, " + patch.name + " loaded");
                runnablePatches.add(patch);
            } 

            InstafelPatchGroup group = PatchLoader.findPatchGroupByShortname(shortName);
            if (group != null) {
                try {
                    group.loadPatches();
                    for (Class<? extends InstafelPatch> gPatch : group.patches) {
                        Constructor<?> constructor = gPatch.getDeclaredConstructor();
                        InstafelPatch gnPatch = (InstafelPatch) constructor.newInstance();
                        Log.info("Patch, " + gnPatch.name + " loaded from " + group.name);
                        runnablePatches.add(gnPatch);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.severe("Error while loading patches in group " + group.name);
                }
                
            }
        }

        if (runnablePatches.size() != 0) {
            Log.info("Totally " + runnablePatches.size() + " patch loaded");
        } else {
            Log.severe("No any patch lodaded to execute");
            System.exit(-1);
        }

        Log.info("Executing patches...");
        for (InstafelPatch patch : runnablePatches) {
            Log.info("");
            Log.info(Environment.SPERATOR_STR);
            System.out.println(patch.name);
            System.out.println("by @" + patch.author);
            System.out.println(patch.description);
            System.out.println("");
            Log.info("Loading tasks..");
            try {
                patch.loadTasks();
                Log.info(patch.tasks.size() + " task loaded");
            } catch (Exception e) {
                e.printStackTrace();
                Log.severe("Error while loading task in " + patch.name);
            }
            Log.info("Executing tasks...");
            for (InstafelTask task : patch.tasks){
                Log.info("");
                Log.info("Executing task " + task.stepName);
                try {
                    task.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    task.failure("Error while running task: " + e.getMessage());
                }
            }
            Log.info("");
            Log.info("All tasks runned succesfully.");
            Log.info(Environment.SPERATOR_STR);
        }
        Log.info("");
        Log.info("All patches executed succesfully.");
    }

    public PEnvironment getEnv() {
        try {
            return new PEnvironment(new File(
                Utils.mergePaths(Environment.PROJECT_DIR, "env.properties")
            ));
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while loading environment file.");
            System.exit(-1);
            return null;
        }
    }

    public PConfig getConfig() {
        try {
            return new PConfig(new File(
                Utils.mergePaths(Environment.PROJECT_DIR, "config.properties")
            ));
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while loading configuration file.");
            System.exit(-1);
            return null;
        }
    }
}