package me.mamiiblt.instafel.patcher.apk;

import me.mamiiblt.instafel.patcher.smali.SmaliDecompiler;
import me.mamiiblt.instafel.patcher.smali.SmaliUtils;
import me.mamiiblt.instafel.patcher.utils.Log;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DecompileAPK {

    private String projectDir, igApkFileName;

    public DecompileAPK(String projectDir, String igApkFilePath) {
        this.projectDir = projectDir;
        this.igApkFileName = igApkFilePath;
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

    public void decompile() throws IOException, InterruptedException {
        Log.info("Decompiling Instagram APK...");        

        Log.info("Unzipping APK...");
        unzipAPK();
        Log.info("Preapering apk directory...");
        preapereDir();
        Log.info("Decoding (baksmaling) dex sources...");
        decodeDexSources();

        Log.info("Instagram APK decompiled succesfully");
    }

    private void decodeDexSources() throws IOException {
        SmaliDecompiler smakDecompiler = new SmaliDecompiler(projectDir);
        File[] dexFiles = smakDecompiler.getRawDexFiles();
        Log.info("Totally found " + dexFiles.length + " dex file.");
        Log.info("Using " + smakDecompiler.getJobSize() + " job for baksmaling...");

        for (File dexFile : dexFiles) {
            Log.info("Decompiling " + dexFile.getName() + "");
            smakDecompiler.decodeDexFile(dexFile);
        }

        // deneme smali araması kısımı

        SmaliUtils smaliUtils = new SmaliUtils(projectDir);
        List<File> smaliFiles = smaliUtils.getSmaliFileByName("RtcConnectionEntity");

        for (File file : smaliFiles) {
            Log.info(file.getName());
        }
    }

    private void preapereDir() throws IOException, InterruptedException {
        String outputDir = projectDir + "/apk_temp";
        String apkDir = projectDir + "/apk";
        createDirs(new File(apkDir), "smali", "smali/raw", "resources", "resources/raw", "other");

        Collection<File> tempFiles = FileUtils.listFilesAndDirs(new File(outputDir), TrueFileFilter.TRUE, null);

        for (File file : tempFiles) {
            File dest = null;

            if (!file.getName().equals("apk_temp")) {
                if (file.getName().contains(".dex")) {
                    dest = new File(apkDir + "/smali/raw/");
                } else if (
                    file.getName().equals("resources.arsc") ||
                    file.getName().equals("res") || 
                    file.getName().equals("r")) {
                        dest = new File(apkDir + "/resources/raw/");
                } else if (file.getName().equals("AndroidManifest.xml.orig")) {
                    dest = new File(apkDir);
                } else {
                    dest = new File(apkDir + "/other/");
                }
    
                if (file.isDirectory()) {
                    FileUtils.moveDirectoryToDirectory(file, dest, true);
                } else {
                    FileUtils.moveFileToDirectory(file, dest, false);
                }
            }
        }

        FileUtils.deleteDirectory(new File(outputDir));

        Log.info("Directory preapered for decompilation.");     
    }

    private void unzipAPK() throws FileNotFoundException, IOException {
        String outputDir = projectDir + "/apk_temp";
        File destDir = new File(outputDir);
        if (!destDir.exists()) destDir.mkdirs();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(igApkFileName))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(outputDir, entry.getName());

                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
        Log.info("APK succesfully unzipped.");
    }

    public void copyInstafelSources() throws IOException {
    
        Log.info("Extracting Instafel sources...");
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

        Log.info("Instafel sources extracted succesfully");
    }
    
    private void createDirs(File directory, String... dirs) throws IOException {
        for (String dir : dirs) {
            FileUtils.forceMkdir(new File(directory.getAbsolutePath() + "/" + dir));
        }
    }
}
