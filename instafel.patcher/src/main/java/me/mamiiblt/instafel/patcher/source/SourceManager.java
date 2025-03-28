package me.mamiiblt.instafel.patcher.source;

import org.apache.commons.io.FileUtils;

import brut.androlib.ApkBuilder;
import brut.androlib.ApkDecoder;
import brut.androlib.Config;
import brut.androlib.exceptions.AndrolibException;
import brut.directory.ExtFile;
import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;
import okio.Source;

import java.io.*;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SourceManager {

    private Config config;

    public SourceManager() {
        this.config = new Config();
    }

    public void decompile(ExtFile apkFile) throws IOException, AndrolibException {
        Log.info("Decompiling Instagram APK...");        
        ApkDecoder apkDecoder = new ApkDecoder(apkFile, config);
        apkDecoder.decode(new File(Utils.mergePaths(Environment.PROJECT_DIR, "sources")));
        Log.info("APK decompiled succesfully");
    }

    public void build() throws AndrolibException, IOException {
        Log.info("Building APK");
        File buildDir = new File(Utils.mergePaths(Environment.PROJECT_DIR, "build"));
        FileUtils.forceMkdir(buildDir);
        ApkBuilder apkBuilder = new ApkBuilder(
            new ExtFile(
                Utils.mergePaths(Environment.PROJECT_DIR, "sources")
            ), config);
        apkBuilder.build(new File(Utils.mergePaths(buildDir.getAbsolutePath(), "ig_build.apk")));
        Log.info("APK builded succesfully");
    }

    public void createConfigFile() throws IOException {
        SourceUtils.createDefaultConfigFile(
            Utils.mergePaths(Environment.PROJECT_DIR, "config.properties")
        );
    }
    
    public void copyInstafelSources() throws IOException {
    
        Log.info("Extracting Instafel sources...");
        File iflSourceDir = new File(Utils.mergePaths(Environment.PROJECT_DIR, "iflr"));
        
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

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
