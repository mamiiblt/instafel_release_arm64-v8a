package me.mamiiblt.instafel.utils;

import android.app.Activity;
import android.util.Base64;

import java.nio.charset.StandardCharsets;

import me.mamiiblt.instafel.managers.PreferenceManager;

public class InstafelAdminUser {

    public static void loginUser(Activity activity, String username, String password) {
        PreferenceManager preferenceManager = new PreferenceManager(activity);
        preferenceManager.setPreferenceString(PreferenceKeys.ifl_admin_username, GeneralFn.encodeString(username));
        preferenceManager.setPreferenceString(PreferenceKeys.ifl_admin_password, GeneralFn.encodeString(password));
    }

    public static void logoutUser(Activity activity) {
        PreferenceManager preferenceManager = new PreferenceManager(activity);
        preferenceManager.setPreferenceString(PreferenceKeys.ifl_admin_username, "def");
        preferenceManager.setPreferenceString(PreferenceKeys.ifl_admin_password, "def");
    }

    public static boolean isUserLogged(Activity activity) {
        PreferenceManager preferenceManager = new PreferenceManager(activity);
        if (!preferenceManager.getPreferenceString(PreferenceKeys.ifl_admin_username, "def").equals("def")) {
            return true;
        } else {
            return false;
        }
    }

    public static UserData returnUserData(Activity activity) {
        PreferenceManager preferenceManager = new PreferenceManager(activity);
        if (!preferenceManager.getPreferenceString(PreferenceKeys.ifl_admin_username, "def").equals("def")) {
            return new UserData(
                    preferenceManager.getPreferenceString(PreferenceKeys.ifl_admin_username, "def"),
                    preferenceManager.getPreferenceString(PreferenceKeys.ifl_admin_password, "def"));
        } else {
            return null;
        }
    }
}

class UserData {
    String username;
    String password;

    public UserData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return GeneralFn.decodeString(username);
    }

    public String getPassword() {
        return GeneralFn.decodeString(password);
    }
}
