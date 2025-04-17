package me.mamiiblt.instafel.patcher.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.json.JSONArray;
import org.json.JSONObject;

import brut.androlib.exceptions.AndrolibException;
import me.mamiiblt.instafel.patcher.source.APKSigner;
import me.mamiiblt.instafel.patcher.source.SourceManager;
import me.mamiiblt.instafel.patcher.source.SourceUtils;
import me.mamiiblt.instafel.patcher.source.WorkingDir;
import me.mamiiblt.instafel.patcher.utils.Env;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.SmaliUtils;
import me.mamiiblt.instafel.patcher.utils.Utils;
import me.mamiiblt.instafel.patcher.utils.cmdhandler.Command;

public class BuildProject implements Command {

    private SmaliUtils smaliUtils = null;
    private SourceManager sourceManager = null;
    private boolean isCloneGenerated = false, isProductionMode = false;

    private List<String> appliedPatches = new ArrayList<>();
    private File buildFolder = null;
    private File cloneRefFolder = null;
    private JSONObject buildInfo = null;
    private String IG_VERSION = null, IG_VER_CODE = null, GENERATION_ID = null, IFL_VERSION = null;
    private File APK_UC = null, APK_C = null;
    private String BUILD_TS = null;

    private boolean useDebugKeystore = true;
    private File APK_SIGNER_JAR = null;
    private String KS_ALIAS = null;
    private String KS_KEY_PASS = null;
    private File KS_FILE = null;
    private String KS_PASS = null;
    private File DEBUG_KEYSTORE = null;

    @Override
    public void execute(String[] args) {
        try {
            if (args.length != 0) {
                Env.PROJECT_DIR = WorkingDir.getExistsWorkingDir(args[0]);
                Env.Config.setupConfig();;
                Env.Project.setupEnv();

                Log.info("Building project...");
                configureEnvironment();
                setOutputAPKFiles();
                updateInstafelEnv();
                generateAPKs();
                signOutputs();
                generateBuildInfo();
                Log.info("Project succesfully builded into /build folder");

                Env.Config.saveProperties();
                Env.Project.saveProperties();
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

    private void configureEnvironment() throws IOException {
        Log.info("Initializing build environment...");
        this.BUILD_TS = String.valueOf(System.currentTimeMillis());
        this.APK_SIGNER_JAR = new File(Utils.mergePaths(Env.PROJECT_DIR,"build", "signer.jar"));
        this.DEBUG_KEYSTORE = new File(Utils.mergePaths(Env.PROJECT_DIR, "build", "debug.keystore"));
        this.smaliUtils = new SmaliUtils(Env.PROJECT_DIR);
        this.buildFolder = new File(Utils.mergePaths(Env.PROJECT_DIR, "build"));
        if (buildFolder.exists()) {
            FileUtils.deleteDirectory(buildFolder);
            Log.info("Old build directory deleted.");
        }
        this.useDebugKeystore = Env.Config.getBoolean(Env.Config.Keys.use_debug_keystore, false);
        if (useDebugKeystore == false) {
            this.KS_FILE = new File(Utils.mergePaths(Env.PROJECT_DIR, Env.Config.getString(Env.Config.Keys.keystore_file, "")));
            this.KS_PASS = Env.Config.getString(Env.Config.Keys.keystore_pass, null);
            this.KS_ALIAS = Env.Config.getString(Env.Config.Keys.keystore_alias, null);
            this.KS_KEY_PASS = Env.Config.getString(Env.Config.Keys.keystore_keypass, null);
        
            if (KS_FILE.getAbsolutePath() == Utils.mergePaths(Env.PROJECT_DIR) || KS_PASS == null || KS_ALIAS == null || KS_KEY_PASS == null) {
                Log.severe("Please set keystore configurations normally from config.properties file");
            }

            if (!KS_FILE.exists()) {
                Log.severe("Keystore file canno't be found, " + KS_FILE.getAbsolutePath());
            }
        }

        this.cloneRefFolder = new File(Env.PROJECT_DIR, "clone_ref");
        this.isCloneGenerated = cloneRefFolder.exists();
        this.isProductionMode = Env.Config.getBoolean(Env.Config.Keys.prod_mode, false);
        String[] appliedPatches = Env.Project.getString(Env.Project.Keys.APPLIED_PATCHES, "").split(",");
        for (String patch : appliedPatches) {
            this.appliedPatches.add(patch);
        }
        this.IG_VERSION = Env.Project.getString(Env.Project.Keys.INSTAGRAM_VERSION, null);
        this.IG_VER_CODE = Env.Project.getString(Env.Project.Keys.INSTAGRAM_VERSION_CODE, null);
        if (isProductionMode) {
            this.IFL_VERSION = Env.Project.getString(Env.Project.Keys.INSTAFEL_VERSION, null);
            this.GENERATION_ID = Env.Project.getString(Env.Project.Keys.GENID, null); 
                
            if (IFL_VERSION == null && GENERATION_ID == null) {
                Log.severe("Environment file is not compatible for building..");
            }
        }

        if (IG_VERSION == null && IG_VER_CODE == null) {
            Log.severe("Environment file is not compatible for building..");
        }

        sourceManager = new SourceManager();
        sourceManager.setConfig(SourceUtils.getDefaultIflConfigBuilder(sourceManager.getConfig()));
        sourceManager.getConfig().setFrameworkDirectory(SourceUtils.getDefaultFrameworkDirectory());

        Log.info("Building environment is configured succesfully");
    }

    private void signOutputs() throws Exception {
        APKSigner.moveOrDeleteApkSigner(true, APK_SIGNER_JAR, DEBUG_KEYSTORE);
        if (useDebugKeystore) {
            if (isCloneGenerated) {
                signAPKs(true, APK_UC, APK_C);
            } else {
                signAPKs(true, APK_UC);
            }
        } else {
            if (isCloneGenerated) {
                signAPKs(false, APK_UC, APK_C);
            } else {
                signAPKs(false, APK_UC);
            }
        }
        APKSigner.moveOrDeleteApkSigner(false, APK_SIGNER_JAR, DEBUG_KEYSTORE);
    }

    private void signAPKs(boolean useDebugKey, File... APKs) throws Exception {
        Log.info("Signing APKs");
        List<String> params = new ArrayList<>();
        params.add("-a");
        for (File file : APKs){
            params.add(file.getAbsolutePath());
        }
        if (useDebugKey) {
            Log.info("Using debug keystore for signing APKs");
            params.addAll(Arrays.asList(
                "--ksDebug", DEBUG_KEYSTORE.getAbsolutePath()
            ));
        } else {
            Log.info("Using " + KS_FILE.getName() + " keystore for signing APKs");
            params.addAll(Arrays.asList(
                "--ks", KS_FILE.getAbsolutePath(),
                "--ksAlias", KS_ALIAS,
                "--ksPass", KS_PASS,
                "--ksKeyPass", KS_KEY_PASS
            ));            
        }
        params.add("--overwrite");
        Log.info("Signing apks...");
        int exitCode = APKSigner.execSigner(params, APK_SIGNER_JAR);

        if (exitCode == 0) {
            Log.info("APKs successfully signed");
        } else {
            FileUtils.deleteDirectory(buildFolder);
            Log.severe("Error while signing apk, clearing /build directory and force exiting");
        }
    }

    private void generateAPKs() throws AndrolibException, IOException {
        Log.info("Generating APKs...");
        Log.info("Generating unclone variant...");
        sourceManager.build(APK_UC.getName());
        Log.info("Unclone APK succesfully generated.");
        if (isCloneGenerated) {
            Log.info("Generating clone variant...");
            replaceSourceFiles(true);
            sourceManager.build(APK_C.getName());
            replaceSourceFiles(false);
            Log.info("Clone APK succesfully generated.");
        }
    }

    private void replaceSourceFiles(boolean type) throws IOException {
        File originalTempDir = new File(Utils.mergePaths(Env.PROJECT_DIR, "orig_temp"));
        FileUtils.forceMkdir(originalTempDir);
        Collection<File> filesAndDirs = FileUtils.listFilesAndDirs(
            cloneRefFolder,
            TrueFileFilter.TRUE,
            TrueFileFilter.TRUE
        );

        if (type) {
            for (File cloneFile : filesAndDirs) {
                if (!cloneFile.isDirectory()) {
                    File sourceFile = new File(cloneFile.getAbsolutePath().replace("clone_ref", "sources"));
                    File tempOrigFile = new File(cloneFile.getAbsolutePath().replace("clone_ref", "orig_temp"));
                    FileUtils.moveFile(sourceFile, tempOrigFile);
                    FileUtils.moveFile(cloneFile, sourceFile);
                }
            }
        } else {
            for (File cloneFile : filesAndDirs) {
                if (!cloneFile.isDirectory()) {
                    File originalFileFromTemp = new File(cloneFile.getAbsolutePath().replace("clone_ref", "orig_temp"));
                    File originalFile = new File(cloneFile.getAbsolutePath().replace("clone_ref", "sources"));
                    FileUtils.delete(originalFile);
                    FileUtils.moveFile(originalFileFromTemp, originalFile);
                }
            }
        }

        // FileUtils.deleteDirectory(originalTempDir);
    }

    private void updateInstafelEnv() throws IOException {
        if (appliedPatches.contains("copy_instafel_src")) {
            Log.info("Updating Instafel app environment...");
            File smallDexFolder = smaliUtils.getSmallSizeSmaliFolder(smaliUtils.getSmaliFolders());
            File envFile = new File(
                Utils.mergePaths(Env.PROJECT_DIR, "sources", smallDexFolder.getName(), "me", "mamiiblt", "instafel", "InstafelEnv.smali"));
            File origEnvFile = new File(Utils.mergePaths(Env.PROJECT_DIR, "sources", "InstafelEnv_orig.smali"));

            if (origEnvFile.exists()) {
                FileUtils.copyFile(origEnvFile, envFile);
            } else {
                FileUtils.copyFile(envFile, origEnvFile);
            }
                  
            List<String> fContent = smaliUtils.getSmaliFileContent(envFile.getAbsolutePath());
            Map<String, String> pairs = new HashMap<>();
            pairs.put("_iflver_", isProductionMode ? IFL_VERSION : "NOT_PROD_MODE");        
            pairs.put("_genid_", isProductionMode ? GENERATION_ID : "NOT_PROD_MODE");
            pairs.put("_igver_", IG_VERSION);
            pairs.put("_igvercode_", IG_VER_CODE);
            pairs.put("_commit_", Env.PROP_COMMIT_HASH);
            pairs.put("_ptag", Env.PROP_PROJECT_TAG);
            pairs.put("_pversion_", "v" + Env.PROP_VERSION_STRING);
            pairs.put("_branch_", "main");
            String apatches = "";
            for (String patch : appliedPatches) {
                if (!patch.contains("clone")) {
                    apatches = apatches.equals("") ? apatches + patch : apatches + "," + patch;
                }
            }
            pairs.put("_patches_", apatches.trim());

            for (Map.Entry<String, String> prop : pairs.entrySet()) {
                if (prop.getValue() == null) {
                    Log.severe("Prop " + prop.getKey() + " is empty.");
                }
            }

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
            Log.info("Instafel app environment configured succesfully");
        }
    }

    private void setOutputAPKFiles() {
        if (!isProductionMode) {
            this.APK_UC = new File(Utils.mergePaths(Env.PROJECT_DIR, "build", "unclone.apk"));
            this.APK_C = new File(Utils.mergePaths(Env.PROJECT_DIR, "build", "clone.apk"));
        } else {
            this.APK_UC = new File(Utils.mergePaths(Env.PROJECT_DIR, "build", "instafel_uc_v" + IFL_VERSION + "_" + IG_VERSION + ".apk"));
            this.APK_C = new File(Utils.mergePaths(Env.PROJECT_DIR, "build", "instafel_c_v" + IFL_VERSION + "_" + IG_VERSION + ".apk"));
        }
    }

    private void generateBuildInfo() {
        this.buildInfo = new JSONObject();
        buildInfo.put("manifest_version", 1);
        if (isProductionMode == false) {
            buildInfo.put("clone_generated", isCloneGenerated);
        }
        JSONObject patcherInfo = new JSONObject();
        patcherInfo.put("version", Env.PROP_VERSION_STRING);
        patcherInfo.put("commit", Env.PROP_COMMIT_HASH);
        patcherInfo.put("channel", Env.PROP_PROJECT_TAG);
        buildInfo.put("patcher", patcherInfo);

        JSONObject patcherData = new JSONObject();
        patcherData.put("build_date", BUILD_TS);
        JSONObject ifl = new JSONObject();
        ifl.put("version", Integer.parseInt(IFL_VERSION));
        ifl.put("gen_id", GENERATION_ID);
        JSONObject ig = new JSONObject();
        ig.put("version", IG_VERSION);
        ig.put("ver_code", IG_VER_CODE);
        patcherData.put("ifl", ifl);
        patcherData.put("ig", ig);
        buildInfo.put("patcher_data", patcherData);

        if (isProductionMode) {
            JSONObject links = new JSONObject();
            links.put("unclone", "https://github.com/mamiiblt/instafel/releases/download/v" + IFL_VERSION + "/" + APK_UC.getName());
            if (isCloneGenerated) {
                links.put("clone", "https://github.com/mamiiblt/instafel/releases/download/v" + IFL_VERSION + "/" + APK_C.getName());
            }
            buildInfo.put("links", links);
        }

        JSONObject fnames = new JSONObject();
        fnames.put("unclone", APK_UC.getName());
        if (isCloneGenerated) {
            fnames.put("clone", APK_C.getName());
        }
        buildInfo.put("fnames", fnames);

        JSONObject hash = new JSONObject();
        hash.put("unclone", Utils.getFileMD5(APK_UC));
        if (isCloneGenerated) {
            hash.put("clone", Utils.getFileMD5(APK_C));
        }
        buildInfo.put("hash", hash);


        
        File bInfoFile = new File(Utils.mergePaths(buildFolder.getAbsolutePath(), "build_info.json"));
        
        try (FileWriter file = new FileWriter(bInfoFile)) {
            file.write(buildInfo.toString());
            Log.info("Build information file saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
