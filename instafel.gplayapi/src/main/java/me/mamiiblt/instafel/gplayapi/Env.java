package me.mamiiblt.instafel.gplayapi;

import me.mamiiblt.instafel.gplayapi.utils.AppInfo;
import me.mamiiblt.instafel.gplayapi.utils.General;
import me.mamiiblt.instafel.gplayapi.utils.Log;
import okhttp3.*;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Env {
    public static String email, aas_token, github_releases_link, github_pat, telegram_api_key;
    public static Properties deviceProperties;
    public static OkHttpClient client;

    public static void updateEnvironment() {
        Properties prop = new Properties();
        client = new OkHttpClient();
        try {
            java.io.File configFile = new java.io.File(General.mergePaths(System.getProperty("user.dir"), "config.properties"));
            if (!configFile.exists()) {
                Log.println("E", "Please configure config.properties file in JAR's directory");
                System.exit(-1);
            }

            FileInputStream input = new FileInputStream(configFile);
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

    public static void updateDeviceProp(String propName) {
        try {
            InputStream input = Main.class.getClassLoader().getResourceAsStream(Paths.get("device_props", propName).toString());
            if (input == null) {
                Log.println("E", "Please write a valid property name");
                System.exit(-1);
            }

            deviceProperties = new Properties();
            deviceProperties.load(input);
    

            Log.println("I", "Device " + deviceProperties.getProperty("UserReadableName") + " is set for check API.");
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
                    InstafelGplayapiInstance instance = new InstafelGplayapiInstance("com.instagram.android");

                    AppInfo appInfo = instance.getIgApk();

                    if (appInfo.getVer_name().contains(".0.0.0.")) { // alpha version names always has this regex
                        if (!lastCheckedVersion[0].equals(appInfo.getVer_name())) {
                            lastCheckedVersion[0] = appInfo.getVer_name();
                            String latestIflVersion = getLatestInstafelVersion(); // get latest instafel version
                            if (latestIflVersion != null && !latestIflVersion.equals(appInfo.getVer_name())) { // this version released or not
                                Log.println("I", "Triggering update, " + latestIflVersion + " -> " + appInfo.getVer_name());
                                triggerUpdate(appInfo); 
                            }
                        }
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


    @SuppressWarnings("deprecation")
    private static void triggerUpdate(AppInfo appInfo) throws Exception {
        JSONObject workflowData = new JSONObject();
        workflowData.put("event_type", "generate_instafel");
        workflowData.put("client_payload", new JSONObject()
                .put("apk_url", appInfo.getApkUrl())
        );

        Log.println("I", "Calling patcher for new version: " + appInfo.getVer_name());

        Request request = new Request.Builder()
                .url("https://api.github.com/repos/mamiiblt/instafel_patch_runner/dispatches")
                .post(RequestBody.create(MediaType.parse("application/json"), workflowData.toString()))
                .addHeader("Authorization", "Bearer " + github_pat)
                .addHeader("Accept", "application/vnd.github+json")
                .addHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new Exception("Error while triggering patcher for version " + appInfo.getVer_name() + " (" + response.code() + ")");

            Log.println("I", "Generator succesfully triggered for " + appInfo.getVer_name() + " (status: " + response.code() + ")");
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
                            return part.trim();
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