package me.mamiiblt.instafel.patcher.patches.fix;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.SmaliUtils;
import me.mamiiblt.instafel.patcher.utils.Utils;
import me.mamiiblt.instafel.patcher.utils.models.LineData;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PInfos;

@PInfos.PatchInfo (
    name = "Fix Secure Content Crash",
    shortname = "fix_secure_ctx_crash",
    desc = "Fixes Link & MainActivity crash issues with when using custom signature",
    author = "mamiiblt",
    listable = true,
    runnable = true
)
public class FixSecureCtxCrash extends InstafelPatch {

    private SmaliUtils smaliUtils = getSmaliUtils();
    private File secureCtxFile = null;
    private List<String> sCtxFContent = null;
    private int invokerLine = 0;
    private String invokerClassName = null;

    @Override
    public List<InstafelTask> initializeTasks() throws Exception {
        return List.of(
            findSecureContext,
            getMethodFromSecureCtx,
            findOldCallerInterface
        );
    }


    InstafelTask findSecureContext = new InstafelTask("Find IgSecureContext part in X") {

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
                        "const-string", "IgSecureContext");
                        
                        if (matchLines.size() == 1) {
                            secureCtxFile = file;
                            Log.info("File found in " + secureCtxFile.getName() + " at " + folder.getName());
                            fileFoundLock = true;
                            break;
                        } 
                    }
                }
            }

            if (secureCtxFile != null) {
                Log.info("Totally scanned " + scannedFileSize + " file in X folders");
                success("IgSecureContext X file is find succesfully.");
            } else {
                failure("IgSecureContext file cannot be found.");
            }
        }
    };

    InstafelTask getMethodFromSecureCtx = new InstafelTask("Get invoke line from caller") {

        @Override
        public void execute() throws Exception {
            sCtxFContent = smaliUtils.getSmaliFileContent(secureCtxFile.getAbsolutePath());
            List<LineData> methodA01 = smaliUtils.getContainLines(sCtxFContent, 
            ".method", "A01", "Landroid/content/Context;Landroid/content/Intent;");            

            if (methodA01.size() != 0) {
                Map<Integer, String> methodLines = smaliUtils.getMethodContent(sCtxFContent, methodA01.get(0).getNum());

                for (Map.Entry<Integer, String> line : methodLines.entrySet()) {
                    String value = line.getValue();
                    int num = line.getKey();
                    if (value.contains("invoke-virtual") && value.contains("{v0}")) {
                        invokerLine = num;
                        invokerClassName = value.split(", ")[1].split(";")[0];
                        Log.info("Invoke line is " + value.trim());
                    }
                }

                if (invokerLine == 0) {
                    failure("Invoke line is not found in A01 method.");
                } else {
                    success("Invoker line is succesfully found, " + invokerLine);
                }
            } else {
                failure("A01 method not found.");
            }
        }
    };

    InstafelTask findOldCallerInterface = new InstafelTask("Find old caller interface from AppRestartUtil") {

        @Override
        public void execute() throws Exception {
            List<File> appRestartUtilQuery = smaliUtils.getSmaliFilesByName(
                "com/instagram/debug/devoptions/refresh/AppRestartUtil.smali");
            File appRestartUtilSmali = null;
            if (appRestartUtilQuery.size() == 0 || appRestartUtilQuery.size() > 1) {
                failure("AppRestartUtil file can't be found / selected.");
            } else {
                appRestartUtilSmali = appRestartUtilQuery.get(0);
                Log.info("AppRestartUtil file is " + appRestartUtilSmali.getPath());
            }
            List<String> fContent = smaliUtils.getSmaliFileContent(appRestartUtilSmali.getAbsolutePath());
            List<LineData> mRestartApp = smaliUtils.getContainLines(fContent, 
            ".method", "restartApp");
            
            if (mRestartApp.size() != 0) {
                Map<Integer, String> methodLines = smaliUtils.getMethodContent(fContent, mRestartApp.get(0).getNum());

                boolean successLock = false;
                for (Map.Entry<Integer, String> line : methodLines.entrySet()) {
                    String value = line.getValue();
                    if (
                        value.contains("invoke-virtual") &&
                        value.contains(invokerClassName) 
                    ) {
                        String oldCallerArgPart = value.split(";->")[1];
                        String sCallerPart = sCtxFContent.get(invokerLine).split(";->")[1];
                        Log.info("Old: " + sCtxFContent.get(invokerLine).trim());
                        sCtxFContent.set(invokerLine, sCtxFContent.get(invokerLine).replace(sCallerPart, oldCallerArgPart));
                        Log.info("New: " + sCtxFContent.get(invokerLine).trim());
                        Log.info("Arg updated " + sCallerPart.trim() + " -> " + oldCallerArgPart.trim());
                        successLock = true;
                    }
                }

                if (successLock) {
                    success("Patch succesfully applied.");
                } else {
                    failure("invoke-virtual line cannot be found in restartApp method");
                }
            } else {
                failure("restartApp method cannot be found in AppRestartUtil");
            }
        }
    }; 
}
