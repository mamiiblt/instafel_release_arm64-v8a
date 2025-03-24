package me.mamiiblt.instafel.patcher.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Handler;

import me.mamiiblt.instafel.patcher.apk.DecompileAPK;
import me.mamiiblt.instafel.patcher.patches.ExamplePatch;

public class Environment extends Configuration {

    public static String PROP_VERSION_STRING = null;
    public static String PROP_COMMIT_HASH = null;
    public static String PROP_PROJECT_TAG = null;    
    public static String PROJECT_DIR = null;

    public static void organizeEnvironment()  {
        try {
            Properties patcherProperties = new Properties();
            InputStream in = Environment.class.getClassLoader().getResourceAsStream("patcher.properties");
            patcherProperties.load(in);
    
            PROP_VERSION_STRING = patcherProperties.getProperty("patcher.version");
            PROP_COMMIT_HASH = patcherProperties.getProperty("patcher.commit");
            PROP_PROJECT_TAG = patcherProperties.getProperty("patcher.tag");
        } catch (Exception e) {
            e.printStackTrace();
            Log.severe("Error while organizing environment.");
            System.exit(-1);
        }
    }

    public static void printPatcherHeader() {
        System.out.println("Instafel Patcher v" + PROP_VERSION_STRING);
        System.out.println(PROP_PROJECT_TAG + "/" + PROP_COMMIT_HASH);
        System.out.println("");
    }

    public static void configurePatcher(String[] args) {
        String stringHelp = "Visit this link for learn anything about patcher!\nhttps://mamiiblt.me/blogs/patcher-guide";
        if (args.length == 0) {
            Log.info(stringHelp);
        } else {
            String firstArg = args[0];
            switch (firstArg) {
                case "--help":
                    Log.info(stringHelp);
                    break;
                case "run":
                        ExamplePatch examplePatch = new ExamplePatch();
                        examplePatch.initializeSteps();
                        examplePatch.execute();
                    break;
                case "create":
                    if (args.length >= 2) {
                        String fileArg = args[1];
                        if (fileArg.contains(".apk")) {
                            try {
                                WorkingDir workingDir = new WorkingDir();
                                PROJECT_DIR = workingDir.createWorkingDir(fileArg);
                                workingDir.createConfigFile(PROJECT_DIR);

                                DecompileAPK decompileAPK = new DecompileAPK(PROJECT_DIR, USER_DIR + fileArg);
                                decompileAPK.copyFrameworksToWorkdir();
                                decompileAPK.decompile();
                                decompileAPK.copyInstafelSources();
                                Log.info("Project created succesfully");
                                Log.info("use -run <patch.name> parameters for applying patches");

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.severe("Error while creating project: " + e.getMessage());
                            }
                        } else {
                            try {
                                WorkingDir workingDir = new WorkingDir();
                                PROJECT_DIR = workingDir.getExistsWorkingDir(fileArg);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.severe("Error while creating working dir: " + e.getMessage());
                            }
                        }
                    } else {
                        Log.warning("Please specify an Instagram APK file or working directory for use patcher!");
                    }
                    break;
                default:
                    Log.warning("Unknown command: " + firstArg);
                    Log.info("Use --help for show help information.");
                    break;
            }

        }
    }
}
