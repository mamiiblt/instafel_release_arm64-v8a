package me.mamiiblt.instafel.gplayapi.utils;

import com.aurora.gplayapi.data.models.App;
import org.json.JSONObject;

public class AppInfo {

    JSONObject infoObject;
    App app;

    public AppInfo(String arch, App app) {
        this.app = app;
        infoObject = new JSONObject();
        infoObject.put("arch", arch);
        infoObject.put("id", app.getId());
        infoObject.put("ver_name", app.getVersionName());
        infoObject.put("ver_code", app.getVersionCode());
        infoObject.put("target_sdk", app.getTargetSdk());
        infoObject.put("updated_on", app.getUpdatedOn());
    }

    public JSONObject getRawJson() {
        return infoObject;
    }

    public App getApp() {
        return app;
    }

    public String getArch() {
        return infoObject.getString("arch");
    }

    public int getId() {
        return infoObject.getInt("id");
    }

    public void addApkInfo(String url, long size) {
        infoObject.put("file", new JSONObject().put("url", url).put("size", size));
    }

    public long getApkSize() {
        return infoObject.getJSONObject("file").getLong("size");
    }

    public String getApkUrl() {
        return infoObject.getJSONObject("file").getString("url");
    }

    public String getVer_name() {
        return infoObject.getString("ver_name");
    }

    public String getVer_code() {
        return infoObject.getString("ver_code");
    }

    public String getTarget_sdk() {
        return infoObject.getString("target_sdk");
    }

    public String getUpdated_on() {
        return infoObject.getString("updated_on");
    }
}