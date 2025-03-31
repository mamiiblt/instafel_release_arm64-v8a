package me.mamiiblt.instafel.patcher.utils.patch;

import java.io.File;
import java.io.IOException;
import java.util.List;

import me.mamiiblt.instafel.patcher.smali.SmaliUtils;
import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.PEnvironment;
import me.mamiiblt.instafel.patcher.utils.Utils;

public abstract class InstafelPatch {
    public String name, author, description, shortname;
    public PEnvironment PEnvironment;
    public SmaliUtils SmaliUtils;
    public List<InstafelTask> tasks;

    public InstafelPatch() {
        try {
            PatchInfo patchInfo = this.getClass().getAnnotation(PatchInfo.class);
            if (patchInfo != null) {
                this.name = patchInfo.name();
                this.author = patchInfo.author();
                this.description = patchInfo.desc();
                this.shortname = patchInfo.shortname();
            } else {
                Log.severe("Please add PatchInfo for running patches normally.");
                System.exit(-1);
            }
    
            PEnvironment = getOrCreateEnvironmentFile();
            SmaliUtils = new SmaliUtils(Environment.PROJECT_DIR);
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while creating InstafelPatch");
            System.exit(-1);
        }
    }
        
    private PEnvironment getOrCreateEnvironmentFile() throws IOException {
        File file = new File(Utils.mergePaths(Environment.PROJECT_DIR, "env.properties"));
        PEnvironment pEnvironment;
        if (!file.exists()) {
            file.createNewFile();
            pEnvironment = new PEnvironment(file);
            pEnvironment.createDefaultEnvFile();
        } else {
            pEnvironment = new PEnvironment(file);
        }
        return pEnvironment;
    }

    public abstract List<InstafelTask> initializeTasks() throws Exception;

    public void loadTasks() throws Exception {
        tasks = initializeTasks();
    }
}
