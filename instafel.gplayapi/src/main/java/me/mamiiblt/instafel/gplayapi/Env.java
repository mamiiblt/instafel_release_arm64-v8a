package me.mamiiblt.instafel.gplayapi;

import me.mamiiblt.instafel.gplayapi.utils.AppInfo;
import me.mamiiblt.instafel.gplayapi.utils.Log;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Env {
    public static String email, aas_token, github_releases_link, github_pat, telegram_api_key;
    public static Properties devicePropertiesArm64, devicePropertiesArm32;
    public static OkHttpClient client;

    public static void updateEnvironment() {
        Properties prop = new Properties();
        client = new OkHttpClient();
        try {
            InputStream input = Main.class.getClassLoader().getResourceAsStream("gplayapi.properties");
            if (input == null) {
                Log.println("E", "Please configure gplayapi.properties file in resources and build jar.");
                System.exit(-1);
            }

            prop.load(input);

            String email_p = prop.getProperty("email", null);
            String aas_token_p = prop.getProperty("aas_token", null);
            String github_rel_link = prop.getProperty("github_releases_link", null);
            String github_pat_p = prop.getProperty("github_pat", null);

            if (email_p != null && aas_token_p != null && github_rel_link != null && github_pat_p != null) {
                email = email_p;
                aas_token = aas_token_p;
                github_releases_link = github_rel_link;
                github_pat = github_pat_p;

                Log.println("I", "User (" + email + ") read from config file.");

            } else {
                Log.println("E", "Error while reading email & aas token property from gplayapi.properties");
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.println("E", "Error while updating environment");
            System.exit(-1);
        }
    }

    public static void updateDeviceProp(String propNameArm64, String propNameArm32) {
        try {
            InputStream input64 = Main.class.getClassLoader().getResourceAsStream(Paths.get("device_props", propNameArm64).toString());
            InputStream input32 = Main.class.getClassLoader().getResourceAsStream(Paths.get("device_props", propNameArm32).toString());
            if (input64 == null || input32 == null) {
                Log.println("E", "Please write a valid property name");
                System.exit(-1);
            }

            devicePropertiesArm64 = new Properties();
            devicePropertiesArm64.load(input64);
            devicePropertiesArm32 = new Properties();
            devicePropertiesArm32.load(input32);

            Log.println("I", "Device " + devicePropertiesArm64.getProperty("UserReadableName") + " is set to Arm64");
            Log.println("I", "Device " + devicePropertiesArm32.getProperty("UserReadableName") + " is set to Arm32");
        } catch (Exception e) {
            e.printStackTrace();
            Log.println("E", "Error while updating device properties");
        }
    }

    public static void startChecker() {
        Timer timer = new Timer();


        final String[] lastCheckedVersion = {""};
        final int[] checkTime = {0};

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    checkTime[0]++;
                    Log.println("I", checkTime[0] + " check started.");
                    InstafelGplayapiInstance instanceArm64 = new InstafelGplayapiInstance("arm64", "com.instagram.android");
                    InstafelGplayapiInstance instanceArm32 = new InstafelGplayapiInstance("arm32", "com.instagram.android");

                    AppInfo appInfo64 = instanceArm64.getIgApk();
                    AppInfo appInfo32 = instanceArm32.getIgApk();

                    Log.println("I", "appInfoArm64: " + appInfo64.getRawJson());
                    Log.println("I", "appInfoArm32: " + appInfo32.getRawJson());

                    if (appInfo64.getVer_name().equals(appInfo32.getVer_name())) {
                        if (appInfo64.getVer_name().contains(".0.0.0.")) { // alpha version names always has this regex
                            if (!lastCheckedVersion[0].equals(appInfo64.getVer_name())) {
                                lastCheckedVersion[0] = appInfo64.getVer_name();
                                String latestIflVersion = getLatestInstafelVersion(); // get latest instafel version
                                if (latestIflVersion != null && latestIflVersion.equals(appInfo64.getVer_name())) { // this version released or not
                                    triggerUpdate(appInfo64, appInfo32); // trigger the generator
                                }
                            }
                        }
                    } else {
                        Log.println("E", "IG Versions are not same");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.println("E", "Error while checking ig updates.");
                }
            }
        };

        long delayMs = 900000; // check every 15 minutes
        timer.scheduleAtFixedRate(task, 0, delayMs);
    }


    private static void triggerUpdate(AppInfo appInfo64, AppInfo appInfo32) throws Exception {
        JSONObject workflowData = new JSONObject();
        workflowData.put("event_type", "build_multi");
        workflowData.put("client_payload", new JSONObject()
                .put("apk_url_64", appInfo64.getApkUrl())
                .put("apk_url_32", appInfo32.getApkUrl())
        );

        Request request = new Request.Builder()
                .url("https://api.github.com/repos/mami-server-2/instafel_generator/dispatches")
                .post(RequestBody.create(MediaType.parse("application/json"), workflowData.toString()))
                .addHeader("Authorization", "Bearer " + github_pat)
                .addHeader("Accept", "application/vnd.github+json")
                .addHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new Exception("Error while triggering patcher for version " + appInfo64.getVer_name() + " (" + response.code() + ")");

            Log.println("I", "Generator succesfully triggered for " + appInfo64.getVer_name() + " (status: " + response.code() + ")");
        }
    }

    private static String getLatestInstafelVersion() throws IOException, Exception {
        Request request = new Request.Builder()
                .url(github_releases_link)
                .addHeader("Authorization", "Bearer " + github_pat)
                .addHeader("Accept", "application/vnd.github+json")
                .addHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new Exception("Instafel Release response code is " + response.code());

            JSONObject responseObject = new JSONObject(response.body().string());
            String[] releaseBody = responseObject.getString("body").split("\n");

            for (String line : releaseBody) {
                if (line.contains("app.version_name")) {
                    String[] verNameLines = line.split("\\|");
                    for (String part : verNameLines) {
                        if (!part.isEmpty() && isNumeric(part)) {
                            return part;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static boolean isNumeric(String part) {
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (int i = 0; i < chars.length; i++) {
            if (part.indexOf(chars[i]) != -1) {
                return true;
            }
        }

        return false;
    }
}