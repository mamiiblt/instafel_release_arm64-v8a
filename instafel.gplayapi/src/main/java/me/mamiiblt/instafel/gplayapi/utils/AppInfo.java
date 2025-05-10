package me.mamiiblt.instafel.gplayapi.utils;

import com.aurora.gplayapi.data.models.App;
import org.json.JSONObject;

public class AppInfo {

    JSONObject infoObject;
    App app;

    public AppInfo(App app) {
        this.app = app;
        infoObject = new JSONObject();
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

    public int getId() {
        return infoObject.getInt("id");
    }

    public void addApkInfo(String name, String url, long size) {
        infoObject.put(name, new JSONObject().put("url", url).put("size", size));
    }

    public boolean ifApkExist(String name) {
        if (infoObject.has(name)) {
            return true;
        } else {
            return false;
        }
    }

    public long getApkSize(String name) {
        return infoObject.getJSONObject(name).getLong("size");
    }

    public String getApkUrl(String name) {
        return infoObject.getJSONObject(name).getString("url");
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