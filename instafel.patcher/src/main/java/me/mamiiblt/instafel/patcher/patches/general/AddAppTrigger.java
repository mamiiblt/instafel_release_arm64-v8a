package me.mamiiblt.instafel.patcher.patches.general;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.SmaliUtils;
import me.mamiiblt.instafel.patcher.utils.Utils;
import me.mamiiblt.instafel.patcher.utils.models.LineData;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PInfos;

@PInfos.PatchInfo (
    name = "Add App Trigger",
    shortname = "add_app_trigger",
    desc = "This patch must be applied for Instafel Stuffs",
    author = "mamiiblt",
    listable = false,
    runnable = true
)
public class AddAppTrigger extends InstafelPatch {

    private final SmaliUtils smaliUtils = getSmaliUtils();
    private File interfaceFile = null;
    private String interfaceClassName = null; 
    private File activityFile = null;

    @Override
    public List<InstafelTask> initializeTasks() throws Exception {
        return List.of(
            findGetRootContentMethod,
            findActivityTrigger,
            addTriggerToActivity
        );
    }
    
    InstafelTask addTriggerToActivity = new InstafelTask("Add trigger to activity") {

        @Override
        public void execute() throws Exception {
            List<String> fContent = smaliUtils.getSmaliFileContent(activityFile.getAbsolutePath());
            
            boolean status = false;

            Log.info("Searching referance line...");
            List<String> invokeLines = new ArrayList<>();
            for (int i = 0; i < fContent.size(); i++) {
                String line = fContent.get(i);
                if (
                    line.contains("invoke-direct") && 
                    line.contains("Landroidx/fragment/app/Fragment") &&
                    !line.contains("Lcom/instagram/quickpromotion/intf/QuickPromotionSlot;")
                ) {
                    Log.info("Invoke line found in line " + i  + ", " + line.trim());
                    invokeLines.add(line);
                }
            }

            String[] callerLines = null;
            if (invokeLines.size() == 0 || invokeLines.size() > 1) {
                failure("invokeLines size is more or equal to 0");
            } else {
                String[] veriablesArr = invokeLines.get(0).split("    invoke-direct \\{")[1].split("\\}")[0].split(", ");

                callerLines = new String[] {
                    "    invoke-virtual {" + veriablesArr[1] + "}, LX/" + interfaceClassName + ";->getRootActivity()Landroid/app/Activity;",
                    "",
                    "    move-result-object v0",
                    "",
                    "    invoke-static {v0}, Lme/mamiiblt/instafel/utils/InitializeInstafel;->triggerCheckUpdates(Landroid/app/Activity;)V",
                    ""
                };

                Log.info("Caller lines setted succesfully.");
            }

            Log.info("Finding end of method...");
            for (int i = 0; i < fContent.size(); i++) {
                String line = fContent.get(i);

                if (
                    line.contains("iput-object") &&
                    fContent.get(i + 2).contains("return-void")
                ) {
                    Log.info("Method end found at line " + i + 2);
                    int sVal = i + 2;
                    for (int a = 0; a < callerLines.length; a++) { 
                        fContent.add(sVal, callerLines[a]);
                        sVal++;
                    }
                    status = true;
                }
            }

            if (status) {
                FileUtils.writeLines(activityFile, fContent);
                success("Caller lines added succesfully");
            } else {
                failure("Patcher can't find enough lines :)");
            }
        }
        
    };

    InstafelTask findActivityTrigger = new InstafelTask("Find activity") {

        @Override
        public void execute() throws Exception {
            File[] smaliFolders = smaliUtils.getSmaliFolders();
            int scannedFileSize = 0;
            boolean fileFoundLock = false;

            List<File> foundFiles = new ArrayList<>();
            for (File folder : smaliFolders) {
                if (fileFoundLock) {
                    break;
                } else {
                    File xFolder = new File(Utils.mergePaths(folder.getAbsolutePath(), "X"));
                    Log.info("Searching in X folder of " + folder.getName());
    
                    
                    Iterator<File> fileIterator = FileUtils.iterateFiles(xFolder, null, true);
                    while (fileIterator.hasNext()) {
                        scannedFileSize++;
                        File file = fileIterator.next();
                        List<String> fContent = smaliUtils.getSmaliFileContent(file.getAbsolutePath()); 

                        boolean[] conditions = {false, false, false, false};

                        for (String line : fContent) {
                            if (line.contains("Landroid/content/res/Configuration;")) {
                                conditions[0] = true;
                            }

                            if (line.contains("Lcom/facebook/quicklog/reliability/UserFlowLogger")) {
                                conditions[1] = true;
                            }

                            if (line.contains("Lcom/instagram/quickpromotion/intf/QPTooltipAnchor")) {
                                conditions[2] = true;
                            }
                            
                            if (line.contains(".super Ljava/lang/Object;")) {
                                conditions[3] = true;
                            }
                        }

                        boolean passStatus = true;
                        for (int i = 0; i < conditions.length; i++) {
                            boolean cond = conditions[i];
                            if (cond == false) {
                                passStatus = false;
                            }
                        }

                        if (passStatus) {
                            Log.info("A file found in " + file.getName() + " at " + folder.getName());
                            foundFiles.add(file);
                        }
                    }
                }
            }

            if (foundFiles.size() == 0 || foundFiles.size() > 1) {
                failure("Found more files than one (or no any file found) for apply patch, add more condition for find correct file.");
            } else {
                activityFile = foundFiles.get(0);
                Log.info("Totally scanned " + scannedFileSize + " file in X folders");
                Log.info("File name is " + activityFile.getName());
                success("Activity file found in X files succesfully");
            }
        }
    };

    InstafelTask findGetRootContentMethod = new InstafelTask("Find getRootContent() method") {

        @Override
        public void execute() throws Exception {
            File[] smaliFolders = smaliUtils.getSmaliFolders();
            int scannedFileSize = 0;
            boolean fileFoundLock = false;
            
            for (File folder : smaliFolders) {
                if (fileFoundLock) {
                    break;
                } else {
                    File xFolder = new File(Utils.mergePaths(folder.getAbsolutePath(), "X"));
                    Log.info("Searching in X folder of " + folder.getName());
    
                    Iterator<File> fileIterator = FileUtils.iterateFiles(xFolder, null, true);
                    while (fileIterator.hasNext()) {
                        scannedFileSize++;
                        File file = fileIterator.next();
                        List<String> fContent = smaliUtils.getSmaliFileContent(file.getAbsolutePath()); 
                        List<LineData> matchLines = smaliUtils.getContainLines(fContent,
                            ".method public getRootActivity()Landroid/app/Activity;");
                        
                        if (matchLines.size() == 1) {
                            interfaceFile = file;
                            interfaceClassName = interfaceFile.getName().split("\\.")[0];
                            Log.info("File found in " + interfaceFile.getName() + " at " + folder.getName());
                            fileFoundLock = true;
                            break;
                        } 
                    }
                }
            }

            if (interfaceFile != null) {
                Log.info("Totally scanned " + scannedFileSize + " file in X folders");
                Log.info("Interface class name is " + interfaceClassName);
                success("Interface file founded.");
            } else {
                failure("Interface file cannot be found.");
            }
        }
        
    };
}
