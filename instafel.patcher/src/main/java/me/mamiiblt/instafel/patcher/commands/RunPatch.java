package me.mamiiblt.instafel.patcher.commands;

import java.util.ArrayList;
import java.util.List;

import me.mamiiblt.instafel.patcher.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.source.WorkingDir;
import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PatchLoader;

public class RunPatch implements Command {

    @Override
    public void execute(String[] args) {

        String wdirFolder = args[0];
        Environment.PROJECT_DIR = WorkingDir.getExistsWorkingDir(wdirFolder);

        List<InstafelPatch> runnablePatches = new ArrayList<>();
        Log.info("Loading patches...");
        for (int i = 1; i < args.length; i++) {
            String patchShortName = args[i];
            InstafelPatch patch = PatchLoader.findPatchByShortname(patchShortName);
            if (patch != null) {
                runnablePatches.add(patch);
            } else {
                Log.severe(patchShortName + " not found in patches!");
                System.exit(-1);
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
}