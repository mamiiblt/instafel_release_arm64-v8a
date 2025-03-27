package me.mamiiblt.instafel.patcher.source;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import brut.androlib.ApkBuilder;
import brut.androlib.ApkDecoder;
import brut.androlib.Config;
import brut.androlib.exceptions.AndrolibException;
import brut.directory.ExtFile;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SourceManager {

    private String projectDir;
    private Config config;
    private ExtFile extFile;

    public SourceManager(String projectDir, String igApkFileName) {
        this.projectDir = projectDir;
        this.config = new Config();
        this.extFile = new ExtFile(Utils.mergePaths(projectDir, igApkFileName));
    }

    public void copyFrameworksToWorkdir() throws IOException {
        File workdirFrameworks = new File(projectDir + "/fw");
        FileUtils.forceMkdir(workdirFrameworks);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("fw_default.apk");
        File targetDir = new File(Utils.mergePaths(workdirFrameworks.getAbsolutePath(), "fw_default.apk"));
        OutputStream outputStream = new FileOutputStream(targetDir);
        IOUtils.copy(inputStream, outputStream);
        Log.info("Default framework file extracted");
    }

    public void decompile() throws IOException, AndrolibException {
        Log.info("Decompiling Instagram APK...");        
        ApkDecoder apkDecoder = new ApkDecoder(extFile, config);
        apkDecoder.decode(new File(Utils.mergePaths(projectDir, "sources")));
        Log.info("APK decompiled succesfully");
    }

    public void build() throws AndrolibException, IOException {
        Log.info("Building APK");
        File buildDir = new File(Utils.mergePaths(projectDir, "build"));
        FileUtils.forceMkdir(buildDir);
        ApkBuilder apkBuilder = new ApkBuilder(extFile, config);
        apkBuilder.build(new File(Utils.mergePaths(buildDir.getAbsolutePath(), "ig_build.apk")));
        Log.info("APK builded succesfully");
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void copyInstafelSources() throws IOException {
    
        Log.info("Extracting Instafel sources...");
        File iflSourceDir = new File(Utils.mergePaths(projectDir, "iflr"));
        
        File tempZipFile = File.createTempFile("temp_zip", ".zip");
        try (InputStream zipStream = SourceManager.class.getClassLoader().getResourceAsStream("instafel_sr.zip");
            FileOutputStream fos = new FileOutputStream(tempZipFile)) {
       
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = zipStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(tempZipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(iflSourceDir, entry.getName());
                
                if (entry.isDirectory()) {
                    newFile.mkdirs();
                    continue;
                }

                newFile.getParentFile().mkdirs();

                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
            }
        }

        Log.info("Instafel sources extracted succesfully");
    }
}
