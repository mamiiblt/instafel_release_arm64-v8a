package me.mamiiblt.instafel.patcher.smali;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.android.tools.smali.baksmali.Baksmali;
import com.android.tools.smali.baksmali.BaksmaliOptions;
import com.android.tools.smali.dexlib2.DexFileFactory;
import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile;

import me.mamiiblt.instafel.patcher.utils.Log;

public class SmaliDecompiler {
    
    private final File rawDexFolder, decompileSmaliFolder;
    private final String projectDir;

    public SmaliDecompiler(String projectDir) {
        this.projectDir = projectDir;
        this.rawDexFolder = new File(projectDir + "/apk/smali/raw");
        this.decompileSmaliFolder = new File(projectDir + "/apk/smali/");
    }

    public List<String> getSmaliFolders() {
        if (rawDexFolder.exists() && rawDexFolder.isDirectory()) {
            File[] dexFiles = rawDexFolder.listFiles();  
            Arrays.sort(dexFiles, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
            List<String> folderPaths = new ArrayList<>();
            for (File dexFile : dexFiles) {
                folderPaths.add(
                    decompileSmaliFolder.getAbsolutePath() + 
                    dexFile.getName().replace(".dex", ""));
            }
            return folderPaths; 
        } else {
            Log.severe("SMALI folder not found.");
            System.exit(-1);
            return null;
        }
    }

    public File[] getRawDexFiles() {
        if (rawDexFolder.exists() && rawDexFolder.isDirectory()) {
            File[] dexFiles = rawDexFolder.listFiles();  
            Arrays.sort(dexFiles, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
            return dexFiles; 
        } else {
            Log.severe("SMALI folder not found.");
            System.exit(-1);
            return null;
        }
    }

    public void decodeDexFile(File dexFileF) throws IOException {
        BaksmaliOptions options = new BaksmaliOptions();
        options.deodex = false;
        options.implicitReferences = false;
        options.parameterRegisters = true;
        options.localsDirective = true;
        options.sequentialLabels = true;
        options.debugInfo = false; // disables debug info like .line etc.
        options.codeOffsets = false;
        options.accessorComments = false;
        options.registerInfo = 0;
        options.inlineResolver = null;
        
        decompileSmaliFolder.mkdirs(); 
        File outputFolder = new File(
            decompileSmaliFolder.getAbsolutePath() 
            + dexFileF.getName().replace(".dex", ""));
        DexBackedDexFile dexFile = DexFileFactory.loadDexFile(dexFileF.getAbsolutePath(), Opcodes.forApi(35));
        Baksmali.disassembleDexFile(dexFile, outputFolder, getJobSize(), options);
        Log.info("Dex " + dexFileF.getName().replace(".dex", "") + " succesfully decompiled!");
    }

    public int getJobSize() {
        int jobs = Runtime.getRuntime().availableProcessors();
        if (jobs > 6) {
            jobs = 6;
        }
        return jobs;
    }
}
