package me.mamiiblt.instafel.patcher.utils;

import java.util.logging.Handler;

import me.mamiiblt.instafel.patcher.apk.DecompileAPK;

public class Environment extends Configuration {
    private enum Verbosity { NORMAL, VERBOSE, QUIET }

    public static String VERSION_STRING = "";
    public static String COMMIT_HASH = "";
    public static String PROJECT_DIR = "";

    public static void organizeEnvironment() {
        VERSION_STRING = "v1.0.5";
        COMMIT_HASH = "3e64v5e";
    }

    public static void printPatcherHeader() {
        String versionLine = "Version " + VERSION_STRING + " (commit " + COMMIT_HASH + ")";
        System.out.println("Instafel Patcher ");
        System.out.println(versionLine);
        System.out.println("");
    }

    public static void configurePatcher(String[] args) {
        String stringHelp = "Visit this link for learn anything about patcher!\nhttps://mamiiblt.me/blogs/patcher-guide";
        if (args.length == 0) {
            Log.pr(stringHelp);
        } else {
            String firstArg = args[0];
            switch (firstArg) {
                case "--help":
                    Log.pr(stringHelp);
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
                                decompileAPK.setFrameworkDirectory(PROJECT_DIR + "/fw");
                                decompileAPK.configureParams();
                                decompileAPK.decompile();

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.pr("Error while creating project: " + e.getMessage());
                            }
                        } else {
                            try {
                                WorkingDir workingDir = new WorkingDir();
                                PROJECT_DIR = workingDir.getExistsWorkingDir(fileArg);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.pr("Error while creating working dir: " + e.getMessage());
                            }
                        }
                    } else {
                        Log.pr("Please specify an Instagram APK file or working directory for use patcher!");
                    }
                    break;
                default:
                    Log.pr("Unknown command: " + firstArg);
                    Log.pr("Use --help for show help information.");
                    break;
            }

        }
    }
}
