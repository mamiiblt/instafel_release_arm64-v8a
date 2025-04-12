package me.mamiiblt.instafel.patcher.utils.patch;

import java.util.List;

import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.SmaliUtils;

public abstract class InstafelPatch {
    public String name, author, description, shortname;
    public boolean listable = true, runnable = true;
    public List<InstafelTask> tasks;

    public InstafelPatch() {
        try {
            PatchInfo patchInfo = this.getClass().getAnnotation(PatchInfo.class);
            if (patchInfo != null) {
                this.name = patchInfo.name();
                this.author = patchInfo.author();
                this.description = patchInfo.desc();
                this.shortname = patchInfo.shortname();
                this.listable = patchInfo.listable();
                this.runnable = patchInfo.runnable();
            } else {
                Log.severe("Please add PatchInfo for running patches");
                System.exit(-1);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while creating InstafelPatch");
            System.exit(-1);
        }
    }

    public PatchInfo getPatchInfo() {
        return this.getClass().getAnnotation(PatchInfo.class);
    }

    public SmaliUtils getSmaliUtils() {
        return new SmaliUtils(Environment.PROJECT_DIR);
    }
    
    public abstract List<InstafelTask> initializeTasks() throws Exception;

    public void loadTasks() throws Exception {
        tasks = initializeTasks();
    }
}
