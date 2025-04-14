package me.mamiiblt.instafel.gplayapi;

import me.mamiiblt.instafel.gplayapi.utils.AppInfo;
import me.mamiiblt.instafel.gplayapi.utils.General;
import me.mamiiblt.instafel.gplayapi.utils.Log;
import com.aurora.gplayapi.data.models.App;
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
            if (file.getName().equals("com.instagram.android.apk")) {
                appInfo.addApkInfo(file.getUrl(), file.getSize());
                return appInfo;
            }
        }
        return null;
    }
}