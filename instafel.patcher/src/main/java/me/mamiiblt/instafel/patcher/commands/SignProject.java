package me.mamiiblt.instafel.patcher.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import me.mamiiblt.instafel.patcher.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.source.PConfig;
import me.mamiiblt.instafel.patcher.source.WorkingDir;
import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;

public class SignProject implements Command {

    private File buildFolder = null;
    private File buildInfoFile = null;
    private JSONObject buildInfo = null;
    private boolean isCloneGenerated = false;
    private File APK_UC = null, APK_C = null;
    private File APK_SIGNER_JAR = null;
    private String KS_ALIAS = null;
    private String KS_KEY_PASS = null;
    private File KS_FILE = null;
    private String KS_PASS = null;
    private boolean useDebugKeystore = true;
    private File DEBUG_KEYSTORE = null;

    @Override
    public void execute(String[] args) {
         try {
            if (args.length != 0) {
                String folderArgs = args[0];
                Environment.PROJECT_DIR = WorkingDir.getExistsWorkingDir(folderArgs);
                Environment.PConfig = Environment.getConfig();
                Environment.PEnvironment = Environment.getEnv();
                this.buildFolder = new File(Utils.mergePaths(Environment.PROJECT_DIR,"build"));
                this.buildInfoFile = new File(Utils.mergePaths(Environment.PROJECT_DIR,"build", "build_info.json"));
                this.APK_SIGNER_JAR = new File(Utils.mergePaths(Environment.PROJECT_DIR,"build", "signer.jar"));
                this.DEBUG_KEYSTORE = new File(Utils.mergePaths(Environment.PROJECT_DIR, "build", "debug.keystore"));
                Log.info("Signing generated APKs...");
            
                if (!buildInfoFile.exists() && !buildFolder.exists()) {
                    Log.severe("Error while reading build folder.");
                }

                buildInfo = new JSONObject();
                try (FileInputStream fis = new FileInputStream(buildInfoFile)) {
                    String jsonText = new Scanner(fis, StandardCharsets.UTF_8)
                        .useDelimiter("\\A").next();
                    buildInfo = new JSONObject(jsonText);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.info("Error wile reading build_info.json file.");
                }

                isCloneGenerated = buildInfo.getBoolean("clone_generated");
                JSONObject fnames = buildInfo.getJSONObject("fnames");
                APK_UC = new File(Utils.mergePaths(Environment.PROJECT_DIR,"build", fnames.getString("unclone")));
                if (isCloneGenerated) {
                    APK_C = new File(Utils.mergePaths(Environment.PROJECT_DIR,"build", fnames.getString("clone")));
                }
                useDebugKeystore = Environment.PConfig.getBoolean(PConfig.Keys.use_debug_keystore, false);
            
                moveOrDeleteApkSigner(true);

                if (useDebugKeystore) {
                    if (isCloneGenerated) {
                        signAPKs(true, APK_UC.getAbsolutePath(), APK_C.getAbsolutePath());
                    } else {
                        signAPKs(true, APK_UC.getAbsolutePath());
                    }
                } else {
                    this.KS_FILE = new File(Utils.mergePaths(Environment.PROJECT_DIR, Environment.PConfig.getString(PConfig.Keys.keystore_file, "")));
                    this.KS_PASS = Environment.PConfig.getString(PConfig.Keys.keystore_pass, null);
                    this.KS_ALIAS = Environment.PConfig.getString(PConfig.Keys.keystore_alias, null);
                    this.KS_KEY_PASS = Environment.PConfig.getString(PConfig.Keys.keystore_keypass, null);
                
                    if (KS_FILE.getAbsolutePath() == Utils.mergePaths(Environment.PROJECT_DIR) || KS_PASS == null || KS_ALIAS == null || KS_KEY_PASS == null) {
                        Log.severe("Please set keystore configurations normally from config.properties file");
                    }

                    if (!KS_FILE.exists()) {
                        Log.severe("Keystore file canno't be found.");
                    }

                    if (isCloneGenerated) {
                        signAPKs(false, APK_UC.getAbsolutePath(), APK_C.getAbsolutePath());
                    } else {
                        signAPKs(false, APK_UC.getAbsolutePath());
                    }
                }
                
                moveOrDeleteApkSigner(false);

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

    private void signAPKs(boolean useDebugKey, String... APKs) throws Exception {

        List<String> params = new ArrayList<>();
        params.add("-a");
        params.addAll(Arrays.asList(APKs));
        if (useDebugKey) {
            Log.info("Using debug keystore for signing APKs");
            params.addAll(Arrays.asList(
                "--ksDebug", DEBUG_KEYSTORE.getAbsolutePath()
            ));
        } else {
            Log.info("Using custom keystore for signing APKs");
            params.addAll(Arrays.asList(
                "--ks", KS_FILE.getAbsolutePath(),
                "--ksAlias", KS_ALIAS,
                "--ksPass", KS_PASS,
                "--ksKeyPass", KS_KEY_PASS
            ));            
        }
        params.add("--skipZipAlign");
        params.add("--overwrite");
        int exitCode = execSigner(params);

        if (exitCode == 0) {
            Log.info("APKs succesfully signed");
        } else {
            FileUtils.deleteDirectory(buildFolder);
            Log.severe("Error while signing APKs, clearing /build directory.");
        }
    }


    private int execSigner(List<String> params) throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<>();
        cmd.add("java");
        cmd.add("-jar");
        cmd.add(APK_SIGNER_JAR.getAbsolutePath());
        cmd.addAll(params);

        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.redirectErrorStream(true);
        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Log.info(line);
            }
        }

        return process.waitFor();
    }

    private void moveOrDeleteApkSigner(boolean status) throws IOException {
        if (status) {
            InputStream apkSignerJar = SignProject.class.getResourceAsStream("/signing/uber-apk-signer.zip");
            InputStream debugKeystore = SignProject.class.getResourceAsStream("/signing/debug.keystore");
            try (OutputStream outputStream = new FileOutputStream(APK_SIGNER_JAR)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = apkSignerJar.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (OutputStream outputStream = new FileOutputStream(DEBUG_KEYSTORE)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = debugKeystore.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FileUtils.delete(APK_SIGNER_JAR);
            FileUtils.delete(DEBUG_KEYSTORE);
        }
    }

}
