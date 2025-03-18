package me.mamiiblt.instafel.gplayapi.utils;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        Log.print("E", e.getMessage());
        System.exit(-1);
    }
}
