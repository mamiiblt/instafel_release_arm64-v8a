package me.mamiiblt.instafel.patcher.patches;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.android.tools.smali.smali.Smali;

import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PatchInfo;

@PatchInfo (
    name = "Copy Instafel Sources",
    shortname = "copy_instafel_src",
    desc = "This patch needs to executed for use Instafel stuffs",
    author = "mamiiblt"
)
public class CopyInstafelSources extends InstafelPatch {

    @Override
    public List<InstafelTask> initializeTasks() {
        return List.of(
            copySmaliSources
        );
    }

    InstafelTask copySmaliSources = new InstafelTask("Copy smali sources") {

        @Override
        public void execute() throws Exception {
            File smallDexFolder = SmaliUtils.getSmallSizeSmaliFolder(SmaliUtils.getSmaliFolders());
            File destFolder = new File(
                Utils.mergePaths(smallDexFolder.getAbsolutePath(), "me", "mamiiblt", "instafel"));

            Utils.unzipFromResources("/ifl_sources/ifl_sources.zip", destFolder.getAbsolutePath());
    
        }
    };
}
