package me.mamiiblt.instafel.patcher.smali;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.Utils;
import me.mamiiblt.instafel.patcher.utils.models.LineData;

public class SmaliUtils {
    
    private final String projectDir;
    private final File[] smaliFolders;

    public SmaliUtils(String projectDir) {
        this.projectDir = projectDir;
        this.smaliFolders = getSmaliFolders();
    }

    public List<File> getSmaliFilesByName(String fileNamePart) {
        List<File> smaliFiles = new ArrayList<>();
        
        for (int i = 0; i < smaliFolders.length; i++) {
            File smaliFolder = new File(smaliFolders[i].getAbsolutePath());
            Collection<File> files = FileUtils.listFiles(smaliFolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

            for (File file : files) {
                if (file.getAbsolutePath().contains(fileNamePart)) {
                    smaliFiles.add(file);
                }
            }
        }

        return smaliFiles;
    }

    public void writeContentIntoFile(String filePath, List<String> fContent) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : fContent) {
                bw.write(line);
                bw.newLine();
            }
        }
    }

    public List<LineData> getContainLines(List<String> fContent, String... searchParams) {
        List<LineData> lineDatas = new ArrayList<>();
        for (int i = 0; i < fContent.size(); i++) {
            if (containsAllKeys(fContent.get(i), searchParams)) {
                lineDatas.add(
                    new LineData(i, fContent.get(i))
                );
            }
        }

        return lineDatas;
    }

    public List<String> getSmaliFileContent(String filePath) throws FileNotFoundException {
        List<String> fContent = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                fContent.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }        

        return fContent;
    }

    public File[] getSmaliFolders() {

        File decompiledClassesFolder = new File(Utils.mergePaths(projectDir, "sources"));
        if (decompiledClassesFolder.exists() && decompiledClassesFolder.isDirectory()) {
            File[] folders = decompiledClassesFolder.listFiles(File::isDirectory);
            Arrays.sort(folders, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
            return folders;
        } else {
            Log.severe("classesX folders not found.");
            System.exit(-1);
            return null;
        }
    }

    private boolean containsAllKeys(String input, String... keys) {
        for (String key : keys) {
            if (!input.contains(key)) {
                return false; 
            }
        }
        return true; 
    }
}
