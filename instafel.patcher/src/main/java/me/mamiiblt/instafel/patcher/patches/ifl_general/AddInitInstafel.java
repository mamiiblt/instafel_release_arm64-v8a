package me.mamiiblt.instafel.patcher.patches.ifl_general;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import me.mamiiblt.instafel.patcher.smali.SmaliUtils;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PatchInfo;

@PatchInfo (
    name = "Add Initialize Instafel",
    shortname = "add_init_instafel",
    desc = "This patch must be applied for Instafel Menu",
    author = "mamiiblt",
    listable = false
)
public class AddInitInstafel extends InstafelPatch {

    private SmaliUtils smaliUtils = getSmaliUtils();

    @Override
    public List<InstafelTask> initializeTasks() throws Exception {
        return List.of(
            addPatchLines
        );
    }

    InstafelTask addPatchLines = new InstafelTask("Add initalizator lines to InstagramAppShell") {

        @Override
        public void execute() throws Exception {
            List<File> appShellResult = smaliUtils.getSmaliFilesByName("/com/instagram/app/InstagramAppShell.smali");
            File appShellFile = null;
            if (appShellResult.size() == 0 || appShellResult.size() > 1) {
                failure("InstagramAppShell file can't be found / selected.");
            } else {
                appShellFile = appShellResult.get(0);
                Log.info("InstagramAppShell file is " + appShellFile.getPath());
            }
            
            List<String> fContent = smaliUtils.getSmaliFileContent(appShellFile.getAbsolutePath());

            boolean lock = false;
            for (int i = 0; i < fContent.size(); i++) {
                String line = fContent.get(i);
                if (line.contains("Landroid/app/Application;->onCreate()V")) {
                    String veriableName = line.split("\\}")[0].split("\\{")[1];
                    
                    String[] content = {
                        "    invoke-static {" + veriableName + "}, Lme/mamiiblt/instafel/utils/InitializeInstafel;->setContext(Landroid/app/Application;)V",
                        "    new-instance v2, Lme/mamiiblt/instafel/utils/InstafelCrashHandler;",
                        "    invoke-direct {v2, " + veriableName + "}, Lme/mamiiblt/instafel/utils/InstafelCrashHandler;-><init>(Landroid/content/Context;)V",
                        "    invoke-static {v2}, Ljava/lang/Thread;->setDefaultUncaughtExceptionHandler(Ljava/lang/Thread$UncaughtExceptionHandler;)V"
                    };

                    if (fContent.get(i + 2).contains("Lme/mamiiblt/instafel")) {
                        failure("This patch is applied already.");
                    }
                    fContent.add(i + 2, String.join("\n\n", content) + "\n");
                    lock = true;
                }
            }

            if (lock) {
                FileUtils.writeLines(appShellFile, fContent);
                success("Initalizator lines added succesfully.");
            } else {
                failure("onCreate() method not found.");
            }
        }
        
    };

}
