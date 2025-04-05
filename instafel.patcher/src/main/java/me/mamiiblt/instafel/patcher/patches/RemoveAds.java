package me.mamiiblt.instafel.patcher.patches;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.antlr.grammar.v3.ANTLRParser.block_return;
import org.apache.commons.io.FileUtils;

import me.mamiiblt.instafel.patcher.smali.SmaliUtils;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;
import me.mamiiblt.instafel.patcher.utils.models.LineData;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PatchInfo;

// Thanks to ReVanced developers for made this patch possible!
@PatchInfo (
    name = "Remove Ads",
    shortname = "remove_ads",
    desc = "Remove Ads in Instagram",
    author = "mamiiblt"
)
public class RemoveAds extends InstafelPatch {

    private SmaliUtils smaliUtils = getSmaliUtils();
    private File removeAdsFile = null;

    @Override
    public List<InstafelTask> initializeTasks() throws Exception {
        return List.of(
            findSourceFile,
            changeMethodReturn
        );
    }

    InstafelTask changeMethodReturn = new InstafelTask("Change method return") {

        @Override
        public void execute() throws Exception {
            List<String> fContent = smaliUtils.getSmaliFileContent(removeAdsFile.getAbsolutePath());
            
            boolean status = false;;
            int methodLine = 0;
            for (int i = 0; i < fContent.size(); i++) {
                if (fContent.get(i).contains("SponsoredContentController.insertItem")) {
                    for (int a = i; a > 0; a--) {
                        if (fContent.get(a).contains(".method")) {
                            methodLine = a;
                            status = true;
                            break;
                        }
                    }
                }
            }

            if (status) {
                String[] lines = {
                    "",
                    "    const/4 v0, 0x1",
                    "",
                    "    return v0",
                };
                fContent.add(methodLine + 2, String.join("\n", lines));
                FileUtils.writeLines(removeAdsFile, fContent);
                success("Method return succesfully appiled");
            } else {
                failure("Required method cannot be found.");
            }
        }
        
    };

    InstafelTask findSourceFile = new InstafelTask("Find source file") {

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
                            "SponsoredContentController.insertItem");
                        
                        if (matchLines.size() == 1) {
                            removeAdsFile = file;
                            Log.info("File found in " + removeAdsFile.getName() + " at " + folder.getName());
                            fileFoundLock = true;
                            break;
                        } 
                    }
                }
            }

            if (removeAdsFile != null) {
                Log.info("Totally scanned " + scannedFileSize + " file in X folders");
                success("Remove ads controller file founded.");
            } else {
                failure("Remove ads controller file file cannot found");
            }
        }
        
    };

}
