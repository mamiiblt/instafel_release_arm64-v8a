package me.mamiiblt.instafel.patcher.utils.patch;

import java.io.File;
import java.util.List;

import me.mamiiblt.instafel.patcher.smali.SmaliUtils;
import me.mamiiblt.instafel.patcher.source.PConfig;
import me.mamiiblt.instafel.patcher.source.PEnvironment;
import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;

public abstract class InstafelPatch {
    public String name, author, description, shortname;
    public boolean listable = true;
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
            } else {
                Log.severe("Please add PatchInfo for running patches normally.");
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

    public abstract List<InstafelTask> initializeTasks() throws Exception;

    public void loadTasks() throws Exception {
        tasks = initializeTasks();
    }
}
