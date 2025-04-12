package me.mamiiblt.instafel.patcher.patches.ifl_general;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.SmaliUtils;
import me.mamiiblt.instafel.patcher.utils.Utils;
import me.mamiiblt.instafel.patcher.utils.models.LineData;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PatchInfo;

@PatchInfo (
    name = "Add Long Click Event",
    shortname = "add_long_click_event",
    desc = "This patch must be applied for Instafel Menu",
    author = "mamiiblt",
    listable = false,
    runnable = false
)
public class AddLongClickEvent extends InstafelPatch {

    private SmaliUtils smaliUtils = getSmaliUtils();
    private File longClickFile = null;
    
    @Override
    public List<InstafelTask> initializeTasks() throws Exception {
        return List.of(
            findLongClickSmali,
            addCallerLines
        );
    }

    InstafelTask addCallerLines = new InstafelTask("Change dev options caller to Instafel menu caller") {

        @Override
        public void execute() throws Exception {
            List<String> fContent = smaliUtils.getSmaliFileContent(longClickFile.getAbsolutePath());

            boolean lock = false;
            for (int i = 0; i < fContent.size(); i++) {
                if (
                    fContent.get(i).contains("new-instance") &&
                    fContent.get(i + 2).contains("invoke-direct") &&
                    fContent.get(i + 7).contains("Landroid/content/Context") // for be sure we searching right line :)
                ) {
                    String vNameRw = fContent.get(i).trim().split(" ")[1];
                    String veriablePart = vNameRw.substring(0, vNameRw.length() - 1);
                    Log.info(veriablePart);

                    fContent.set(i, "    new-instance " + veriablePart + ", Lme/mamiiblt/instafel/utils/OpenIflMenu;");
                    fContent.set(i + 2, "    invoke-direct {" + veriablePart+ "}, Lme/mamiiblt/instafel/utils/OpenIflMenu;-><init>()V");
                    Log.info("new-instance & invoke-direct lines modified at " + i + " & " + (i + 2) + " lines");
                    lock = true;
                }
            }

            if (lock) {
                FileUtils.writeLines(longClickFile, fContent);
                success("Lines modified successfully.");
            } else {
                failure("instance and invoke lines couldn't found");
            }
        }
        
    };

    InstafelTask findLongClickSmali = new InstafelTask("Find long click smali file") {

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
                            "com/instagram/profile/fragment/UserDetailFragment;Lcom/instagram/profile/intf/UserDetailLaunchConfig;");
                        
                        if (matchLines.size() == 1) {
                            longClickFile = file;
                            Log.info("File found in " + longClickFile.getName() + " at " + folder.getName());
                            fileFoundLock = true;
                            break;
                        } 
                    }
                }
            }

            if (longClickFile != null) {
                Log.info("Totally scanned " + scannedFileSize + " file in X folders");
                success("Long click event file founded.");
            } else {
                failure("Long click event file cannot be found.");
            }
        }
    };
}
