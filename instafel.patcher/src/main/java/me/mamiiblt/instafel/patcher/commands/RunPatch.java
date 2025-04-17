package me.mamiiblt.instafel.patcher.commands;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import me.mamiiblt.instafel.patcher.source.WorkingDir;
import me.mamiiblt.instafel.patcher.utils.Env;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatchGroup;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PatchLoader;

public class RunPatch implements Command {

    private boolean runAll = false;

    @Override
    public void execute(String[] args) {

        String wdirFolder = args[0];
        Env.PROJECT_DIR = WorkingDir.getExistsWorkingDir(wdirFolder);
        Env.Project.setupEnv();
        Env.Config.setupConfig();

        List<InstafelPatch> runnablePatches = new ArrayList<>();
        Log.info("Loading patches...");
        for (int i = 1; i < args.length; i++) {
            boolean findResult = false;
            String shortName = "";
            if (args[1].equals("all")) {
                runAll = true;
                shortName = "all";
                Log.info("Loading all patches...");
            } else {
                shortName = args[i];
            }
            
            InstafelPatch patch = PatchLoader.findPatchByShortname(shortName);
            if (patch != null) {
                if (patch.runnable == true) {
                    Log.info("Patch, " + patch.name + " loaded");
                    runnablePatches.add(patch);
                    findResult = true;
                } else {
                    if (runAll == false) {
                        Log.info("Patch " + patch.shortname + " is not runnable in single mode!");
                    }
                }
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
                    findResult = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.severe("Error while loading patches in group " + group.name);
                }
            }

            if (findResult == false) {
                Log.info("Patch " + shortName + " is not found in patches.");
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
            Log.info(Env.SPERATOR_STR);
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
                Log.info("Execute: " + task.stepName);
                try {
                    task.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    task.failure("Error while running task: " + e.getMessage());
                }
            }
            Log.info("");
            String patches = Env.Project.getString(Env.Project.Keys.APPLIED_PATCHES, "");
            Env.Project.setString(Env.Project.Keys.APPLIED_PATCHES, patches.equals("") ? patches + patch.shortname : patches + "," + patch.shortname);
            Log.info("All tasks runned succesfully.");
            Log.info(Env.SPERATOR_STR);
        }
        Log.info("");
        Log.info("All patches executed succesfully.");
        Env.Project.saveProperties();
        Env.Config.saveProperties();
    }
}