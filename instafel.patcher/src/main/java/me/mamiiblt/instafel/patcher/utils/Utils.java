package me.mamiiblt.instafel.patcher.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

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

    public static void deleteDirectory(String path) throws IOException{
        File file = new File(path);
        if (file.exists()) {
            FileUtils.deleteDirectory(file);
        }
    }

    public static void zipDirectory(Path sourceDir, Path zipFilePath) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath.toFile()))) {
            Files.walk(sourceDir)
                 .forEach(path -> {
                     String zipEntryName = sourceDir.relativize(path).toString().replace("\\", "/");
                     try {
                         if (Files.isDirectory(path)) {
                             if (!zipEntryName.isEmpty()) {
                                 zipOutputStream.putNextEntry(new ZipEntry(zipEntryName + "/"));
                                 zipOutputStream.closeEntry();
                             }
                         } else {
                             zipOutputStream.putNextEntry(new ZipEntry(zipEntryName));
                             Files.copy(path, zipOutputStream);
                             zipOutputStream.closeEntry();
                         }
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 });
        }
    }

}
