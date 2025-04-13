package me.mamiiblt.instafel.patcher.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import me.mamiiblt.instafel.patcher.utils.models.LineData;

public class SmaliUtils {
    
    private final String projectDir;
    private final File[] smaliFolders;

    public SmaliUtils(String projectDir) {
        this.projectDir = projectDir;
        this.smaliFolders = getSmaliFolders();
    }

    public int getUnusedRegistersOfMethod(List<String> fContent, int methodStart, int lineEnd) {
        List<Integer> registers = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\bv(\\d+)\\b");
        for (int i = methodStart; i <= lineEnd; i++) {
            Matcher matcher = pattern.matcher(fContent.get(i));

            while(matcher.find()) {
                registers.add(Integer.parseInt(matcher.group(1)));
            }
        }

        Set<Integer> uniqueSet = new HashSet<>(registers);

        List<Integer> sortedRegisters = new ArrayList<>(uniqueSet);
        Collections.sort(sortedRegisters);

        for (int i = 0; i <= Collections.max(sortedRegisters) + 1; i++) {
            if (!sortedRegisters.contains(i)) {
                return i;
            }
        }

        return sortedRegisters.get(sortedRegisters.size() - 1) + 1;
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

    public List<String> getSmaliFileContent(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    public File getSmallSizeSmaliFolder(File[] smaliFolders) {
        if (smaliFolders == null || smaliFolders.length == 0) {
            return null;
        }

        Optional<File> smallestFolder = Arrays.stream(smaliFolders)
                .filter(File::isDirectory) 
                .min((f1, f2) -> Long.compare(getFolderSize(f1), getFolderSize(f2)));

        return smallestFolder.orElse(null);
    }

    public File getSmaliFolderByPaths(String... folders) {
        if (smaliFolders == null) {
            return null;
        }


        for (File smaliFolder : smaliFolders) {
            File extPath = new File(Utils.mergePaths(smaliFolder.getAbsolutePath(), folders));
            if (extPath.exists()) {
                return smaliFolder;
            }
        }
        return null;
    }

    public static long getFolderSize(File folder) {
        if (!folder.exists() || folder.isFile()) {
            return 0;
        }
        File[] files = folder.listFiles();
        if (files == null) return 0;
        long size = 0;
        for (File file : files) {
            size += file.isFile() ? file.length() : getFolderSize(file);
        }
        return size;
    }

    public File[] getSmaliFolders() {

        File decompiledClassesFolder = new File(Utils.mergePaths(projectDir, "sources"));
        if (decompiledClassesFolder.exists() && decompiledClassesFolder.isDirectory()) {
            File[] folders = decompiledClassesFolder.listFiles(File::isDirectory);
            folders = Arrays.stream(folders)
                .filter(f -> f.getName().toLowerCase().contains("smali"))
                .toArray(File[]::new);
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
