package me.mamiiblt.instafel.ota;

import static me.mamiiblt.instafel.utils.GeneralFn.getPackageName;
import static me.mamiiblt.instafel.utils.GeneralFn.getPackageNameFromCtx;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Locale;
import java.util.SplittableRandom;

import me.mamiiblt.instafel.R;
import me.mamiiblt.instafel.managers.PreferenceManager;
import me.mamiiblt.instafel.utils.GeneralFn;
import me.mamiiblt.instafel.utils.Localizator;
import me.mamiiblt.instafel.utils.PreferenceKeys;
import me.mamiiblt.instafel.utils.PreferenceTypes;

public class IflEnvironment {

    public static int getIflVersion(Activity activity) {
        PreferenceManager preferenceManager = new PreferenceManager(activity);
        int customValue = preferenceManager.getPreferenceInt(PreferenceKeys.ifl_custom_ifl_version, 0);
        if (customValue == 0) {
            String rawVersion = Localizator.getLocalizedString(
                    activity,
                    Localizator.getIflLocale(activity),
                    "ifl_version"
            );
            try {

                return Integer.parseInt(rawVersion);
            } catch (Exception e) {
                return 0;
            }
        } else {
            return customValue;
        }
    }

    public static int getIflVersionFromCtx(Context ctx) {
        try {
            PreferenceManager preferenceManager = new PreferenceManager(ctx);
            String customValue = preferenceManager.getPreferenceString(PreferenceKeys.ifl_custom_ifl_version, "");
            if (customValue.isEmpty()) {
                PackageManager packageManager = ctx.getPackageManager();
                Resources appResources = packageManager.getResourcesForApplication(getPackageNameFromCtx(ctx));
                int iflStringId = appResources.getIdentifier("ifl_version", "string", getPackageNameFromCtx(ctx));
                String rawVersion = appResources.getString(iflStringId);
                return Integer.parseInt(rawVersion);
            } else {
                return Integer.parseInt(customValue);
            }

        } catch (Exception e) {
            return 0;
        }
    }

    public static String getIflVersionString(Activity activity) {
        return "Release v" + getIflVersion(activity);
    }

    public static String getIgVerAndCodeString(Activity activity) {
        try {
            PreferenceManager preferenceManager = new PreferenceManager(activity);
            String customIgVer = preferenceManager.getPreferenceString(PreferenceKeys.ifl_custom_ig_version, "");
            String customIgVerCode = preferenceManager.getPreferenceString(PreferenceKeys.ifl_custom_ig_ver_code, "");

            if (!customIgVer.isEmpty()) {
                if (!customIgVerCode.isEmpty()) {
                    return "v" + customIgVer + " (" + customIgVerCode + ")";
                } else {
                    return "v" + customIgVer + " (" + Localizator.getString(activity, "ifl_ig_vercode") + ")";
                }
            } else {
                if (!customIgVerCode.isEmpty()) {
                    return "v" + Localizator.getString(activity, "ifl_ig_version") + " (" + customIgVerCode + ")";
                } else {
                    return "v" + Localizator.getString(activity, "ifl_ig_version") + " (" + Localizator.getString(activity, "ifl_ig_vercode") + ")";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error_while_getting_value";
        }
    }

    public static String getArchFromCtx(Context ctx) {
        try {
            PreferenceManager preferenceManager = new PreferenceManager(ctx);
            String customArch = preferenceManager.getPreferenceString(PreferenceKeys.ifl_custom_arch, "");
            if (customArch.isEmpty()) {
                PackageManager packageManager = ctx.getPackageManager();
                Resources appResources = packageManager.getResourcesForApplication(getPackageNameFromCtx(ctx));
                int iflStringId = appResources.getIdentifier("ifl_ig_arch", "string", getPackageNameFromCtx(ctx));
                String arch = appResources.getString(iflStringId);
                if (arch.equals("arm64-v8a") || arch.equals("armeabi-v7a")) {
                    return arch;
                } else {
                    return "arm64-v8a";
                }
            } else {
                if (customArch.equals("arm64-v8a") || customArch.equals("armeabi-v7a")) {
                    return customArch;
                } else {
                    return "arm64-v8a";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "arm64-v8a";
        }
    }

    public static String getIgVerCode(Context ctx) {
        try {
            PreferenceManager preferenceManager = new PreferenceManager(ctx);
            String customIgVerCode = preferenceManager.getPreferenceString(PreferenceKeys.ifl_custom_ig_ver_code, "");
            if (customIgVerCode.isEmpty()) {
                PackageManager packageManager = ctx.getPackageManager();
                Resources appResources = packageManager.getResourcesForApplication(getPackageNameFromCtx(ctx));
                int iflStringId = appResources.getIdentifier("ifl_ig_vercode", "string", getPackageNameFromCtx(ctx));
                return appResources.getString(iflStringId);
            } else {
                return customIgVerCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "CNULL";
        }
    }

    public static String getGenerationId(Context ctx) {
        try {
            PreferenceManager preferenceManager = new PreferenceManager(ctx);
            String customGenId = preferenceManager.getPreferenceString(PreferenceKeys.ifl_custom_gen_id, "");
            if (customGenId.isEmpty()) {
                PackageManager packageManager = ctx.getPackageManager();
                Resources appResources = packageManager.getResourcesForApplication(getPackageNameFromCtx(ctx));
                int iflStringId = appResources.getIdentifier("ifl_genid", "string", getPackageNameFromCtx(ctx));
                return appResources.getString(iflStringId);
            } else {
                return customGenId;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
    }

    public static String getIgVersion(Context ctx) {
        try {
            PreferenceManager preferenceManager = new PreferenceManager(ctx);
            String customIgVer = preferenceManager.getPreferenceString(PreferenceKeys.ifl_custom_ig_version,"");
            if (customIgVer.isEmpty()) {
                PackageManager packageManager = ctx.getPackageManager();
                Resources appResources = packageManager.getResourcesForApplication(getPackageNameFromCtx(ctx));
                int iflStringId = appResources.getIdentifier("ifl_ig_version", "string", getPackageNameFromCtx(ctx));
                return appResources.getString(iflStringId);
            } else {
                return customIgVer;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "CNULL";
        }
    }

    public static String getArch(Activity activity) {
        PreferenceManager preferenceManager = new PreferenceManager(activity);
        String customArch = preferenceManager.getPreferenceString(PreferenceKeys.ifl_custom_arch, "");
        if (customArch.isEmpty()) {
            String arch = Localizator.getString(
                    activity,
                    "ifl_ig_arch"
            );

            if (arch == "arm64-v8a" || arch == "armeabi-v7a") {
                return arch;
            } else {
                return "arm64-v8a";
            }
        } else {
            return customArch;
        }

    }

    public static String getTypeString(Activity activity, Locale locale) {
        if (getType(activity).equals("Clone")) return Localizator.getLocalizedString(activity, locale.getLanguage(), "ifl_a1_07");
        if (getType(activity).equals("Unclone")) return Localizator.getLocalizedString(activity, locale.getLanguage(), "ifl_a1_08");
        return getType(activity);
    }

    public static String getType(Context ctx) {
        try {
            String packageName = ctx.getApplicationContext().getPackageName();
            if (packageName.equals("com.instagram.android") || packageName.equals("me.mamiiblt.instafel")) {
                return "Unclone";
            } else {
                return "Clone";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Unclone";
        }
    }

    public static String getType(Activity activity) {
        String packageName = activity.getApplicationContext().getPackageName();
        if (packageName.equals("com.instagram.android") || packageName.equals("me.mamiiblt.instafel")) {
            return "Unclone";
        } else {
            return "Clone";
        }
    }
}
