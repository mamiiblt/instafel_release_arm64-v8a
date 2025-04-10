package me.mamiiblt.instafel.patcher.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.json.JSONArray;
import org.json.JSONObject;

import brut.androlib.exceptions.AndrolibException;
import me.mamiiblt.instafel.patcher.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.smali.SmaliUtils;
import me.mamiiblt.instafel.patcher.source.PConfig;
import me.mamiiblt.instafel.patcher.source.PEnvironment;
import me.mamiiblt.instafel.patcher.source.SourceManager;
import me.mamiiblt.instafel.patcher.source.SourceUtils;
import me.mamiiblt.instafel.patcher.source.WorkingDir;
import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;

public class BuildProject implements Command {

    private boolean generateClone = false, isProductionMode = false;
    private List<String> appliedPatches = new ArrayList<>();
    private File cloneRefFolder = null;
    private JSONObject buildInfo = null;
    private String IG_VERSION = null, IG_VER_CODE = null, GENERATION_ID = null, IFL_VERSION = null;
    private String FNAME_UNCLONE = null, FNAME_CLONE = null;
    private String BUILD_TS = null;
    private SmaliUtils smaliUtils = null;
    private SourceManager sourceManager = null;
    private File buildFolder = null;

    @Override
    public void execute(String[] args) {
        try {
            if (args.length != 0) {
                String folderArgs = args[0];
                Log.info("Building project...");
                Environment.PROJECT_DIR = WorkingDir.getExistsWorkingDir(folderArgs);
                Environment.PConfig = Environment.getConfig();
                Environment.PEnvironment = Environment.getEnv();
                this.smaliUtils = new SmaliUtils(Environment.PROJECT_DIR);
                this.buildFolder = new File(Utils.mergePaths(Environment.PROJECT_DIR, "build"));
                if (buildFolder.exists()) {
                    FileUtils.deleteDirectory(buildFolder);
                    Log.info("Old build directory deleted.");
                }

                configureBuilderData();
                setFnames();
                updateInstafelEnv();

                sourceManager = new SourceManager();
                sourceManager.setConfig(SourceUtils.getDefaultIflConfigBuilder(sourceManager.getConfig()));
                sourceManager.getConfig().setFrameworkDirectory(SourceUtils.getDefaultFrameworkDirectory());
                generateAPKs();
                generateBuildInfo();
            } else {
                Log.info("Wrong commage usage type, use like that;");
                Log.info("java -jar patcher.jar build instagram");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while running command");
            System.exit(-1);
        }
    }
    private void generateAPKs() throws AndrolibException, IOException {
        Log.info("Generating unclone variant...");
        sourceManager.build(FNAME_UNCLONE);
        if (generateClone) {
            Log.info("Generating clone variant...");
            replaceSourceFiles(true);
            sourceManager.build(FNAME_CLONE);
            replaceSourceFiles(false);
        }
    }

    private void replaceSourceFiles(boolean type) throws IOException {
        File originalTempDir = new File(Utils.mergePaths(Environment.PROJECT_DIR, "orig_temp"));
        FileUtils.forceMkdir(originalTempDir);
            Collection<File> filesAndDirs = FileUtils.listFilesAndDirs(
            cloneRefFolder,
            TrueFileFilter.TRUE,
            TrueFileFilter.TRUE
        );

        if (type) {
            for (File file : filesAndDirs) {
                if (!file.isDirectory()) {
                    File origFile = new File(file.getAbsolutePath().replace("clone_ref", "sources"));
                    FileUtils.moveFile(origFile, new File(origFile.getAbsolutePath().replace("sources", "orig_temp")));
                    FileUtils.moveFile(file, origFile);
                }
            }
        } else {
            for (File file : filesAndDirs) {
                if (!file.isDirectory()) {
                    File cloneFile = new File(file.getAbsolutePath().replace("clone_ref", "sources"));
                    cloneFile.delete();
                    FileUtils.moveFile(new File(file.getAbsolutePath().replace("sources", "orig_temp")), cloneFile);
                }
            }
        }

        FileUtils.deleteDirectory(originalTempDir);
    }

    private void updateInstafelEnv() throws IOException {
        if (appliedPatches.contains("copy_instafel_src")) {
            File smallDexFolder = smaliUtils.getSmallSizeSmaliFolder(smaliUtils.getSmaliFolders());
            File envFile = new File(
                Utils.mergePaths(Environment.PROJECT_DIR, "sources", smallDexFolder.getName(), "me", "mamiiblt", "instafel", "InstafelEnv.smali"));
            File origEnvFile = new File(Utils.mergePaths(Environment.PROJECT_DIR, "sources", "InstafelEnv_orig.smali"));

            if (origEnvFile.exists()) {
                FileUtils.copyFile(origEnvFile, envFile);
            } else {
                FileUtils.copyFile(envFile, origEnvFile);
            }
                  
            List<String> fContent = smaliUtils.getSmaliFileContent(envFile.getAbsolutePath());
            Map<String, String> pairs = new HashMap<>();
            Log.info("Creating pairs...");
            Log.info("Production mode is " + isProductionMode);
            pairs.put("_iflver_", isProductionMode ? IFL_VERSION : "NOT_PROD_MODE");        
            pairs.put("_genid_", isProductionMode ? GENERATION_ID : "NOT_PROD_MODE");
            pairs.put("_igver_", IG_VERSION);
            pairs.put("_igvercode_", IG_VER_CODE);
            pairs.put("_pcommit_", Environment.PROP_COMMIT_HASH);
            pairs.put("_ptag", Environment.PROP_PROJECT_TAG);
            pairs.put("_pversion_", "v" + Environment.PROP_VERSION_STRING);
            pairs.put("_patches_", Environment.PEnvironment.getString(PEnvironment.Keys.APPLIED_PATCHES, "No any patch applied."));

            for (Map.Entry<String, String> prop : pairs.entrySet()) {
                if (prop.getValue().equals(null)) {
                    Log.severe("Prop " + prop.getKey() + " is empty.");
                }
            }

            Log.info("Configuring InstafelEnv file...");
            for (int i = 0; i < fContent.size(); i++) {
                String line = fContent.get(i);

                if (line.contains("PRODUCTION_MODE")) {
                    fContent.set(i, isProductionMode ?
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
            Log.info("InstafelEnv configured.");
        }
    }

    private void setFnames() {
        if (!isProductionMode) {
            this.FNAME_UNCLONE = "unclone";
            this.FNAME_CLONE = "clone";
        } else {
            this.FNAME_UNCLONE = "instafel_uc_v" + IFL_VERSION + "_" + IG_VERSION + ".apk";
            this.FNAME_CLONE = "instafel_c_v" + IFL_VERSION + "_" + IG_VERSION + ".apk";
        }
    }

    private void configureBuilderData() {
        this.BUILD_TS = String.valueOf(System.currentTimeMillis());
        this.cloneRefFolder = new File(Environment.PROJECT_DIR, "clone_ref");
        this.generateClone = cloneRefFolder.exists();
        this.isProductionMode = Environment.PConfig.getBoolean(PConfig.Keys.prod_mode, false);
        String[] appliedPatches = Environment.PEnvironment.getString(PEnvironment.Keys.APPLIED_PATCHES, "").split(",");
        for (String patch : appliedPatches) {
            this.appliedPatches.add(patch);
        }
        IG_VERSION = Environment.PEnvironment.getString(PEnvironment.Keys.INSTAGRAM_VERSION, null);
        IG_VER_CODE = Environment.PEnvironment.getString(PEnvironment.Keys.INSTAGRAM_VERSION_CODE, null);
        if (isProductionMode) {
            IFL_VERSION = Environment.PEnvironment.getString(PEnvironment.Keys.INSTAFEL_VERSION, null);
            GENERATION_ID = Environment.PEnvironment.getString(PEnvironment.Keys.GENID, null); 
                
            if (IFL_VERSION == null && GENERATION_ID == null) {
                Log.severe("Environment file is not compatible for building..");
            }
        }

        if (IG_VERSION != null && IG_VER_CODE != null) {
            Log.info("Building environment is configured succesfully");
        } else {
            Log.severe("Environment file is not compatible for building..");
        }
    }

    private void generateBuildInfo() {
        this.buildInfo = new JSONObject();
        buildInfo.put("manifest_version", 1);
        buildInfo.put("clone_generated", generateClone);
        buildInfo.put("build_ts", BUILD_TS);

        JSONObject patcherInfo = new JSONObject();
        patcherInfo.put("patcher_version", Environment.PROP_VERSION_STRING);
        patcherInfo.put("commit", Environment.PROP_COMMIT_HASH + "/" + Environment.PROP_PROJECT_TAG);
        JSONArray appliedP = new JSONArray();
        for (String applied : appliedPatches) {
            appliedP.put(applied);
        }
        patcherInfo.put("applied_patches", appliedP);
        
        JSONObject genData = new JSONObject();
        buildInfo.put("production_mode", isProductionMode);
        genData.put("ig_version", IG_VERSION);
        genData.put("ig_ver_code", IG_VER_CODE);
        if (isProductionMode) {
            genData.put("ifl_version", IFL_VERSION);
            genData.put("generation_id", GENERATION_ID);
        }

        JSONObject fNames = new JSONObject();
        fNames.put("unclone", FNAME_UNCLONE);
        if (generateClone) {
            fNames.put("clone", FNAME_CLONE);
        }
        
        buildInfo.put("patcher", patcherInfo);
        buildInfo.put("info", genData);
        buildInfo.put("fnames", fNames);

        File bInfoFile = new File(Utils.mergePaths(buildFolder.getAbsolutePath(), "build_info.json"));
        
        try (FileWriter file = new FileWriter(bInfoFile)) {
            file.write(buildInfo.toString(2));
            Log.info("Build information file saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
