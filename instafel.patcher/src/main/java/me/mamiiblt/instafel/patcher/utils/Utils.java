package me.mamiiblt.instafel.patcher.utils;

import java.nio.file.Paths;

public class Utils {
    public static String mergePaths(String basePath, String... args) {
        return Paths.get(basePath, args).toString();
    }

    public static class OSDedector {
        private static String OS = System.getProperty("os.name").toLowerCase();
    
        public static boolean isWindows() {
            return OS.contains("win");
        } 
    
        public static boolean isMac() {
            return OS.contains("mac");
        }
    
        public static boolean isUnix() {
            return OS.contains("nix") || OS.contains("nux") || OS.contains("aix") || OS.contains("sunos");
        }
    
        public static String getOS() {
            return OS;
        }
    }  

}
