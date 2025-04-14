package me.mamiiblt.instafel.gplayapi.utils;

import com.aurora.gplayapi.data.models.AuthData;
import com.aurora.gplayapi.helpers.AuthHelper;

import java.nio.file.Paths;
import java.util.Properties;

public class General {
    public static AuthData authenticateUser(String email, String aasKey, Properties deviceProp) {
        try {
            AuthData authData = AuthHelper.Companion.build(email, aasKey, deviceProp);
            return authData;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }

    public static String mergePaths(String basePath, String... args) {
        return Paths.get(basePath, args).toString();
    }
}
