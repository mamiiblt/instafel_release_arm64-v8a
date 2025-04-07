package me.mamiiblt.instafel.patcher.patches.ifl_general;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import me.mamiiblt.instafel.patcher.smali.SmaliUtils;
import me.mamiiblt.instafel.patcher.source.PConfig;
import me.mamiiblt.instafel.patcher.source.PEnvironment;
import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PatchInfo;

@PatchInfo (
    name = "Configure App Environment",
    shortname = "configure_app_env",
    desc = "This patch must be applied for Instafel Menu",
    author = "mamiiblt",
    listable = false
)
public class ConfigureAppEnvironment extends InstafelPatch {

    private SmaliUtils smaliUtils = getSmaliUtils();

    @Override
    public List<InstafelTask> initializeTasks() throws Exception {
        return List.of(
            updateEnvFile            
        );
    }

    InstafelTask updateEnvFile = new InstafelTask("Change values in environment smali file") {

        @Override
        public void execute() throws Exception {
            File smallDexFolder = smaliUtils.getSmallSizeSmaliFolder(smaliUtils.getSmaliFolders());
            File envFile = new File(
                Utils.mergePaths(Environment.PROJECT_DIR, "sources", smallDexFolder.getName(), "me", "mamiiblt", "instafel", "InstafelEnv.smali"));

            boolean isProdMode = Environment.PConfig.getBoolean(PConfig.Keys.prod_mode, false);
            Log.info("Reading file...");
            List<String> fContent = smaliUtils.getSmaliFileContent(envFile.getAbsolutePath());

            Map<String, String> pairs = new HashMap<>();
            Log.info("Creating pairs...");
            Log.info("Production mode is " + isProdMode);
            pairs.put("_iflver_", isProdMode ? 
                Environment.PEnvironment.getString(PEnvironment.Keys.INSTAFEL_VERSION, "empty")
                : "NOT_PROD_MODE");        
            pairs.put("_genid_", isProdMode ?
                Environment.PEnvironment.getString(PEnvironment.Keys.GENID, "empty")
                : "NOT_PROD_MODE");
            pairs.put("_igver_", Environment.PEnvironment.getString(PEnvironment.Keys.INSTAGRAM_VERSION, "empty"));
            pairs.put("_igvercode_", Environment.PEnvironment.getString(PEnvironment.Keys.INSTAGRAM_VERSION_CODE, "empty"));
            pairs.put("_commit_", Environment.PROP_COMMIT_HASH + " (" + Environment.PROP_PROJECT_TAG + ")");
            pairs.put("_patches_", "aaaaa,bbbbb,ccccc,ddddd,eeeee,ffff,gggg,dddd,gggg,hhhh,jjjj");

            for (Map.Entry<String, String> prop : pairs.entrySet()) {
                if (prop.getValue().equals("empty")) {
                    failure("Prop " + prop.getKey() + " is empty.");
                }
            }

            Log.info("Replacing values in env file..");
            for (int i = 0; i < fContent.size(); i++) {
                String line = fContent.get(i);

                if (line.contains("PRODUCTION_MODE")) {
                    fContent.set(i, isProdMode ?
                        ".field public static PRODUCTION_MODE:Z = true" :
                        ".field public static PRODUCTION_MODE:Z = false");
                }

                for (Map.Entry<String, String> prop : pairs.entrySet()) {
                    if (line.contains(prop.getKey())) {
                        fContent.set(i, line.replace(prop.getKey(), prop.getValue()));
                    }
                }
            }

            FileUtils.writeLines(envFile, fContent);
            success("Environment file succesfully saved.");
        }        
    };

}
