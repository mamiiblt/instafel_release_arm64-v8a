package me.mamiiblt.instafel;

public class InstafelEnv {
    public static String IFL_LANG = null;
    public static int IFL_THEME = 25891;

    // These fields need to be set from patcher.
    public static boolean PRODUCTION_MODE = false;
    public static String IFL_VERSION = "_iflver_"; // 454
    public static String GENERATION_ID = "_genid_"; // 01926031325
    public static String IG_VERSION = "_igver_"; // 371.0.0.0.23
    public static String IG_VERSION_CODE = "_igvercode_"; // 377506971
    public static String PATCHER_COMMIT = "_pcommit_"; // 3ed4c6e
    public static String PATCHER_VERSION = "_pversion_"; // 1.0.3
    public static String PATCHER_TAG = "_ptag_"; // release or debug
    public static String APPLIED_PATCHES = "_patches_"; // aaaa,bbbbb (without space)
    public static boolean isPatchApplied(String patchName) {
        String[] patches = APPLIED_PATCHES.split(",");
        for (String patch : patches) {
            if (patch.equals(patchName)) {
                return true;
            }
        }
        return false;
    }
}
