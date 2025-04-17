package me.mamiiblt.instafel.patcher.patches.clone;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.SmaliUtils;
import me.mamiiblt.instafel.patcher.utils.Utils;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PInfos;

@PInfos.PatchInfo (
    name = "Replace Instagram Strings",
    shortname = "clone_replace_strs",
    desc = "It makes app compatible for clone generation.",
    author = "mamiiblt",
    listable = false,
    runnable = false
)
public class ClonePackageReplacer extends InstafelPatch {

    private SmaliUtils smaliUtils = getSmaliUtils();

    @Override
    public List<InstafelTask> initializeTasks() throws Exception {
        return List.of(
            updateSmaliFiles
        );
    }

    InstafelTask updateSmaliFiles = new InstafelTask("Update smali files") {

        @Override
        public void execute() throws Exception {
            Log.info("Changing package name strings in smali files.");
            int changedLSize = 0;
            for (File folder : smaliUtils.getSmaliFolders()) {
                Iterator<File> fileIterator = FileUtils.iterateFiles(new File(Utils.mergePaths(folder.getAbsolutePath())), null, true);
                while (fileIterator.hasNext()) {
                    File sourcesFile = fileIterator.next();
                    if (!sourcesFile.getAbsolutePath().contains("/me/mamiiblt/instafel")) {
                        File cloneFile = new File(sourcesFile.getAbsolutePath().replace("sources", "clone_ref"));
                        File file = cloneFile.exists() ? cloneFile : sourcesFile;
                        List<String> fContent = smaliUtils.getSmaliFileContent(file.getAbsolutePath());
    
                        for (int i = 0; i < fContent.size(); i++) {
                            String line = fContent.get(i);
                            if (line.contains("\"com.instagram.android\"")) {
                                changedLSize++;
                                fContent.set(i, line.replace("\"com.instagram.android\"", "\"com.instafel.android\""));
                                Log.info("Constraint updated in " + folder.getName() + "/"+ file.getName() + " at line " + i);
                                FileUtils.writeLines(new File(file.getAbsolutePath().replace("sources", "clone_ref")), fContent);
                            }
                        }
                    }
                }
            }
            Log.info("Totally " + changedLSize + " line updated.");
            success("Package strings succesfully updated in clone referances.");
        }
    }; 
}
