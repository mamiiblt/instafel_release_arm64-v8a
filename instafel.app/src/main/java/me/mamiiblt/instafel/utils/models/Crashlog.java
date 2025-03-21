package me.mamiiblt.instafel.utils.models;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

import me.mamiiblt.instafel.ota.IflEnvironment;

public class Crashlog {
    private AppData appData;
    private DeviceData deviceData;
    private CrashData crashData;
    private Object date;

    public Crashlog(AppData appData, DeviceData deviceData, CrashData crashData, Object date) {
        this.appData = appData;
        this.deviceData = deviceData;
        this.crashData = crashData;
        this.date = date;
    }

    public Object getDate() {
        return date;
    }

    public String convertToString() {
       try {
           JSONObject logObject = new JSONObject();
           JSONObject newAppData = new JSONObject();
           newAppData.put("ifl_ver", appData.getIfl_ver());
           newAppData.put("ig_ver", appData.getIg_ver());
           newAppData.put("ig_ver_code", appData.getIg_ver_code());
           newAppData.put("ig_itype", appData.getIg_itype());
           newAppData.put("ig_arch", appData.getIg_arch());
           logObject.put("appData", newAppData);

           JSONObject newDeviceData = new JSONObject();
           newDeviceData.put("aver", deviceData.getAver());
           newDeviceData.put("sdk", deviceData.getSdk());
           newDeviceData.put("model", deviceData.getModel());
           newDeviceData.put("brand", deviceData.getBrand());
           newDeviceData.put("product", deviceData.getProduct());
           logObject.put("deviceData", newDeviceData);

           JSONObject newCrashData = new JSONObject();
           newCrashData.put("msg", crashData.getMsg());
           newCrashData.put("trace", crashData.getTrace());
           newCrashData.put("class", crashData.getClassName());
           logObject.put("crashData", newCrashData);

           //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
           logObject.put("date", date);

           return logObject.toString();
       } catch (Exception e) {
           e.printStackTrace();
           return "ERROR_WHILE_CONVERTING_CRASHLOG";
       }
    }

    public AppData getAppData() {
        return appData;
    }

    public DeviceData getDeviceData() {
        return deviceData;
    }

    public CrashData getCrashData() {
        return crashData;
    }
}