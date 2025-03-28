package me.mamiiblt.instafel.patcher.utils.patch;

import java.util.List;

import me.mamiiblt.instafel.patcher.utils.Log;

public abstract class InstafelPatch {
    public String name, author, description, shortname;
    public List<InstafelTask> tasks;

    public InstafelPatch() {
        PatchInfo patchInfo = this.getClass().getAnnotation(PatchInfo.class);
        if (patchInfo != null) {
            this.name = patchInfo.name();
            this.author = patchInfo.author();
            this.description = patchInfo.desc();
            this.shortname = patchInfo.shortname();
        } else {
            Log.severe("Please add PatchInfo for running patches normally.");
        }
    }
        
    public abstract List<InstafelTask> initializeTasks();

    public void loadTasks() {
        tasks = initializeTasks();
    }
}
