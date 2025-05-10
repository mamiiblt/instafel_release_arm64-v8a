package me.mamiiblt.instafel.gplayapi;

import me.mamiiblt.instafel.gplayapi.utils.AppInfo;
import me.mamiiblt.instafel.gplayapi.utils.General;
import me.mamiiblt.instafel.gplayapi.utils.Log;

import com.aurora.gplayapi.data.models.AuthData;
import com.aurora.gplayapi.data.models.File;
import com.aurora.gplayapi.helpers.AppDetailsHelper;
import com.aurora.gplayapi.helpers.PurchaseHelper;

import java.util.List;

public class InstafelGplayapiInstance {

    String packageName;
    AuthData authData;

    public InstafelGplayapiInstance(String packageName) throws Exception {
        this.packageName = packageName;
        this.authData = General.authenticateUser(Env.email, Env.aas_token, Env.deviceProperties);
    }

    public AppInfo getIgApk() throws Exception {
        AppInfo appInfo = new AppInfo(new AppDetailsHelper(authData).getAppByPackageName(packageName));
        List<File> files = new PurchaseHelper(authData).purchase(appInfo.getApp().getPackageName(), appInfo.getApp().getVersionCode(), appInfo.getApp().getOfferType());
        for (File file : files) {
            Log.println("I", "File found, " + file.getName());
            if (file.getName().equals("com.instagram.android.apk")) {
                appInfo.addApkInfo("base_apk", file.getUrl(), file.getSize());
            } else if (file.getName().contains("config") && file.getName().contains("dpi.apk")) {
                appInfo.addApkInfo("rconf_apk", file.getUrl(), file.getSize());
            }
        }
        if (appInfo.ifApkExist("base_apk") && appInfo.ifApkExist("rconf_apk")) {
            return appInfo;
        } else {
            return null;
        }
    }
}