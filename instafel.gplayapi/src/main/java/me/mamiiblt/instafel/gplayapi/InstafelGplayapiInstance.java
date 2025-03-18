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

    String arch;
    String packageName;
    AuthData authData;

    public InstafelGplayapiInstance(String arch, String packageName) throws Exception {
        this.arch = arch;
        this.packageName = packageName;
        if (arch.equals("arm64")) {
            this.authData = General.authenticateUser(Env.email, Env.aas_token, Env.devicePropertiesArm64);
        } else if (arch.equals("arm32")) {
            this.authData = General.authenticateUser(Env.email, Env.aas_token, Env.devicePropertiesArm64);
        } else {
            throw new Exception();
        }
    }

    public AppInfo getIgApk() throws Exception {
        AppInfo appInfo = new AppInfo(arch, new AppDetailsHelper(authData).getAppByPackageName(packageName));
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