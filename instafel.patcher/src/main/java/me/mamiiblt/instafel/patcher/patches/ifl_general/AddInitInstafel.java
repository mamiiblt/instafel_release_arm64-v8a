package me.mamiiblt.instafel.patcher.patches.ifl_general;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.SmaliUtils;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PInfos;

@PInfos.PatchInfo (
    name = "Add Initialize Instafel",
    shortname = "add_init_instafel",
    desc = "This patch must be applied for Instafel Menu",
    author = "mamiiblt",
    listable = false,
    runnable = true
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
            }
            
            List<String> fContent = smaliUtils.getSmaliFileContent(appShellFile.getAbsolutePath());

            int onCreateMethodLine = 0;
            boolean lock = false;
            for (int i = 0; i < fContent.size(); i++) {
                String line = fContent.get(i);

                if (line.contains("onCreate()V") && line.contains(".method")) {
                    onCreateMethodLine = i;
                }
                
                if (line.contains("Landroid/app/Application;->onCreate()V")) {
                    if (onCreateMethodLine == 0) {
                        Log.severe("onCreateMethod cannot found before caller.");
                    }

                    String onCreateVeriableName = line.split("\\}")[0].split("\\{")[1];
                    int unusedRegister = smaliUtils.getUnusedRegistersOfMethod(fContent, onCreateMethodLine, i);
                    Log.info("Unused register is v" + unusedRegister + " before line " + i + " in onCreate method");

                    String[] content = {
                        "    invoke-static {" + onCreateVeriableName + "}, Lme/mamiiblt/instafel/utils/InitializeInstafel;->setContext(Landroid/app/Application;)V",
                        "    new-instance v" + unusedRegister + ", Lme/mamiiblt/instafel/utils/InstafelCrashHandler;",
                        "    invoke-direct {v" + unusedRegister + ", " + onCreateVeriableName + "}, Lme/mamiiblt/instafel/utils/InstafelCrashHandler;-><init>(Landroid/content/Context;)V",
                        "    invoke-static {v" + unusedRegister + "}, Ljava/lang/Thread;->setDefaultUncaughtExceptionHandler(Ljava/lang/Thread$UncaughtExceptionHandler;)V"
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
