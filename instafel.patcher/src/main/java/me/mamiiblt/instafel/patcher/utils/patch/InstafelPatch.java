package me.mamiiblt.instafel.patcher.utils.patch;

import java.util.ArrayList;
import java.util.List;

import me.mamiiblt.instafel.patcher.utils.Log;

public class InstafelPatch {
    public String name, author, description;
    private List<InstafelTask> tasks = new ArrayList<>();

    public InstafelPatch() {
        PatchInfo patchInfo = this.getClass().getAnnotation(PatchInfo.class);
        if (patchInfo != null) {
            this.name = patchInfo.name();
            this.author = patchInfo.author();
            this.description = patchInfo.desc();
        } else {
            Log.severe("Please add PatchInfo for running patches normally.");
        }
    }

    public void execute() {
        System.out.println("--------------------");
        System.out.println(name);
        System.out.println("by @" + author);
        System.out.println(description);
        System.out.println("");
        System.out.println("--------------------");
        Log.info("Totally " + tasks.size() + " task initialized.");
        Log.info("Executing tasks...");

        for (int i = 0; i < tasks.size(); i++) {
            InstafelTask task = tasks.get(i);
            System.out.println("");
            System.out.println("Task " + (i + 1 ) + " - " + task.stepName);
            task.execute();
            System.out.println("");
        }

        System.out.println("All tasks runned succesfully.");
    }
    
    public void addTask(InstafelTask instafelTask) {
        tasks.add(instafelTask);
    }
}
