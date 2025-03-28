package me.mamiiblt.instafel.patcher.commands;

import java.util.ArrayList;
import java.util.List;

import me.mamiiblt.instafel.patcher.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PatchLoader;

public class RunPatch implements Command {

    @Override
    public void execute(String[] args) {
        List<InstafelPatch> runnablePatches = new ArrayList<>();
        Log.info("Loading patches...");
        for (String patchShortName : args) {
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
            patch.loadTasks();
            Log.info(patch.tasks.size() + " task loaded");
            Log.info("Executing tasks...");
            for (InstafelTask task : patch.tasks){
                Log.info("");
                Log.info("Executing task " + task.stepName);
                task.execute();
            }
            Log.info("");
            Log.info("All tasks runned succesfully.");
            Log.info(Environment.SPERATOR_STR);
        }
        Log.info("");
        Log.info("All patches executed succesfully.");
    }
}