package me.mamiiblt.instafel.patcher.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import me.mamiiblt.instafel.patcher.utils.Log;

public class APKSigner {

    public static int execSigner(List<String> params, File APK_SIGNER_JAR) throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<>();
        cmd.add("java");
        cmd.add("-jar");
        cmd.add(APK_SIGNER_JAR.getAbsolutePath());
        cmd.addAll(params);

        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.redirectErrorStream(true);
        Process process = builder.start();

        return process.waitFor();
    }

    public static void moveOrDeleteApkSigner(boolean status, File APK_SIGNER_JAR, File DEBUG_KEYSTORE) throws IOException {
        if (status) {
            InputStream apkSignerJar = APKSigner.class.getResourceAsStream("/signing/uber-apk-signer.zip");
            InputStream debugKeystore = APKSigner.class.getResourceAsStream("/signing/debug.keystore");
            try (OutputStream outputStream = new FileOutputStream(APK_SIGNER_JAR)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = apkSignerJar.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (OutputStream outputStream = new FileOutputStream(DEBUG_KEYSTORE)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = debugKeystore.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FileUtils.delete(APK_SIGNER_JAR);
            FileUtils.delete(DEBUG_KEYSTORE);
        }
    }
}
