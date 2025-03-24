package me.mamiiblt.instafel.patcher.smali;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class SmaliUtils {
    
    private final String projectDir;
    private final List<String> smaliFolders;
    private final SmaliDecompiler smaliDecompiler;

    public SmaliUtils(String projectDir) {
        this.projectDir = projectDir;
        this.smaliDecompiler = new SmaliDecompiler(projectDir);
        this.smaliFolders = smaliDecompiler.getSmaliFolders();
    }

    public List<File> getSmaliFileByName(String fileNamePart) {
        List<File> smaliFiles = new ArrayList<>();
        
        for (int i = 0; i < smaliFolders.size(); i++) {
            File smaliFolder = new File(smaliFolders.get(i));
            Collection<File> files = FileUtils.listFiles(smaliFolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

            for (File file : files) {
                if (file.getName().contains(fileNamePart)) {
                    smaliFiles.add(file);
                }
            }
        }

        return smaliFiles;
    }
}
