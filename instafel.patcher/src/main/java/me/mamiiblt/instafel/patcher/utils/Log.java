package me.mamiiblt.instafel.patcher.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    public static void pr(String tag, String message) {
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(currentDate) + " - " + tag + message);
    }

    public static void pr(String message) {
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(currentDate) + " - " + "LOG - " + message);
    }
}
