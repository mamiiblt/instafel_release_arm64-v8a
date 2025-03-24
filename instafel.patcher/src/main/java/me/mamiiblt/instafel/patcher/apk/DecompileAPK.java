package me.mamiiblt.instafel.patcher.apk;

import brut.androlib.Config;
import brut.androlib.exceptions.AndrolibException;
import brut.directory.ExtFile;
import me.mamiiblt.instafel.patcher.utils.Log;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DecompileAPK {

    private String projectDir, igApkFileName;
    private Config config;

    public DecompileAPK(String projectDir, String igApkFilePath) {
        this.projectDir = projectDir;
        this.igApkFileName = igApkFilePath;
        this.config = new Config();
    }

    public void copyFrameworksToWorkdir() throws IOException {
        File workdirFrameworks = new File(projectDir + "/fw");
        FileUtils.forceMkdir(workdirFrameworks);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("fw_default.apk");
        File targetDir = new File(workdirFrameworks.getAbsoluteFile() + "/fw_default.apk");
        OutputStream outputStream = new FileOutputStream(targetDir);
        IOUtils.copy(inputStream, outputStream);
        Log.info("Default framework file exported to working directory");
    }

    public void decompile() throws IOException, AndrolibException {
        Log.info("Decompiling Instagram APK...");        
    
        IflDecoder apkDecoder = new IflDecoder(new ExtFile(igApkFileName), config);
        apkDecoder.decode(new File(projectDir +  "/sources"));
        Log.info("Instagram APK decompiled succesfully");
    }

    public void copyInstafelSources() throws IOException {
    
        Log.info("Exporting Instafel sources...");
        File iflSourceDir = new File(projectDir + "/iflr");
        
        File tempZipFile = File.createTempFile("temp_zip", ".zip");
        try (InputStream zipStream = DecompileAPK.class.getClassLoader().getResourceAsStream("instafel_sr.zip");
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

        Log.info("Instafel sources exported succesfully");
    }
    
    public void configureParams() throws AndrolibException {
        config.setBaksmaliDebugMode(false);
        config.setDecodeResources(Config.DECODE_RESOURCES_NONE);
        Log.info("Baksmali parameters configured.");
    }

    public void setFrameworkDirectory(String fwDir) {
        config.setFrameworkDirectory(projectDir + "/fw");
        Log.info("Framework directory set.");
    }

    private void createDirs(File directory, String... dirs) throws IOException {
        for (String dir : dirs) {
            FileUtils.forceMkdir(new File(directory.getAbsolutePath() + "/" + dir));
        }
    }
}
