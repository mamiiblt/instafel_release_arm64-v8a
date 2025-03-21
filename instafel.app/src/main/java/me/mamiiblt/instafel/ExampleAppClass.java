package me.mamiiblt.instafel;

import android.app.Application;

import me.mamiiblt.instafel.utils.InstafelCrashHandler;

public class ExampleAppClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new InstafelCrashHandler(this));
    }
}
