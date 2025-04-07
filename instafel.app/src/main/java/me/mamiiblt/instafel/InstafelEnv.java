package me.mamiiblt.instafel;

public class InstafelEnv {
    public static String IFL_LANG = null;
    public static int IFL_THEME = 25891;

    // These fields need to be set from patcher.
    public static boolean PRODUCTION_MODE = false;
    public static String IFL_VERSION = "454";
    public static String GENERATION_ID = "01926031325"; // it will be "CUSTOM_PROD" if build is Custom Production
    public static String IG_VERSION = "371.0.0.0.23";
    public static String IG_VERSION_CODE = "377506971";
    public static String PATCHER_COMMIT = "3ed4c6e";
    public static String APPLIED_PATCHES = "unlock_developer_options,get_generation_info,copy_instafel_src,add_init_instafel,add_long_click_event,add_app_trigger,remove_ads,ext_snooze_warning_dur,clone_general,clone_str";

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
