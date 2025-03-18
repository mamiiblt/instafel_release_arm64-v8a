package me.mamiiblt.instafel.gplayapi;

import com.aurora.gplayapi.data.models.AuthData;
import me.mamiiblt.instafel.gplayapi.utils.ExceptionHandler;
import me.mamiiblt.instafel.gplayapi.utils.Log;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler()); // set an uncaught exception handler for abnormal status

        Log.println("I", "Instafel GplayApi");

        // load email & aas token from gplayapi.properties
        Env.updateEnvironment();
        // set device config (you can get device props from https://gitlab.com/AuroraOSS/gplayapi , And don't forget to import prop file into resources)
        Env.updateDeviceProp("gplayapi_px_3a.properties", "gplayapi_rm_5_pro.properties");
        // this method starts checker, you can customize it whatever you want :)
        Env.startChecker();

    }
}