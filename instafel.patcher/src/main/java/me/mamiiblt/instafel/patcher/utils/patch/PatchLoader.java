package me.mamiiblt.instafel.patcher.utils.patch;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import me.mamiiblt.instafel.patcher.utils.patch.PInfos.PatchGroupInfo;
import me.mamiiblt.instafel.patcher.utils.patch.PInfos.PatchInfo;

public class PatchLoader {

    public static final String DEFAULT_PACKAGE_NAME = "me.mamiiblt.instafel.patcher.patches";

    public static InstafelPatchGroup findPatchGroupByShortname(String shortName) {
        try (ScanResult scanResult = new ClassGraph()
                .acceptPackages(DEFAULT_PACKAGE_NAME)
                .enableClassInfo()
                .enableAnnotationInfo()
                .scan()) {

            for (ClassInfo classInfo : scanResult.getSubclasses(InstafelPatchGroup.class.getName())) {
                Class<?> clazz = Class.forName(classInfo.getName());

                if (!Modifier.isAbstract(clazz.getModifiers()) && clazz.isAnnotationPresent(PInfos.PatchGroupInfo.class)) {
                    PInfos.PatchGroupInfo info = clazz.getAnnotation(PInfos.PatchGroupInfo.class);
                    
                    if (shortName.equals("all")) {
                        Constructor<?> constructor = clazz.getDeclaredConstructor();
                        return (InstafelPatchGroup) constructor.newInstance();
                    } else {
                        if (info.shortname().equals(shortName)) {
                            Constructor<?> constructor = clazz.getDeclaredConstructor();
                            return (InstafelPatchGroup) constructor.newInstance();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InstafelPatch findPatchByShortname(String shortPatchName) {
        try (ScanResult scanResult = new ClassGraph()
                .acceptPackages(DEFAULT_PACKAGE_NAME)
                .enableClassInfo()
                .enableAnnotationInfo()
                .scan()) {

            for (ClassInfo classInfo : scanResult.getSubclasses(InstafelPatch.class.getName())) {
                Class<?> clazz = Class.forName(classInfo.getName());

                if (!Modifier.isAbstract(clazz.getModifiers()) && clazz.isAnnotationPresent(PatchInfo.class)) {
                    PatchInfo info = clazz.getAnnotation(PatchInfo.class);
                    
                    if (shortPatchName.equals("all")) {
                        Constructor<?> constructor = clazz.getDeclaredConstructor();
                        return (InstafelPatch) constructor.newInstance();
                    } else {
                        if (info.shortname().equals(shortPatchName)) {
                            Constructor<?> constructor = clazz.getDeclaredConstructor();
                            return (InstafelPatch) constructor.newInstance();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<PatchInfo> getPatchInfos() {
        List<PatchInfo> patchInfos = new ArrayList<>();
        Set<Class<? extends InstafelPatch>> patches = findPatchClassesInPackage();
        for (Class<? extends InstafelPatch> patch : patches) {
            patchInfos.add(patch.getAnnotation(PatchInfo.class));
        }
        return patchInfos;
    }

    public static List<PatchGroupInfo> getPatchGroupInfos() {
        List<PatchGroupInfo> patchGroupInfos = new ArrayList<>();
        Set<Class<? extends InstafelPatchGroup>> patches = findPatchGroupClassesInPackage();
        for (Class<? extends InstafelPatchGroup> patch : patches) {
            patchGroupInfos.add(patch.getAnnotation(PatchGroupInfo.class));
        }
        return patchGroupInfos;
    }

    @SuppressWarnings("unchecked")
    public static Set<Class<? extends InstafelPatchGroup>> findPatchGroupClassesInPackage() {
        Set<Class<? extends InstafelPatchGroup>> groups = new HashSet<>();

        try (ScanResult scanResult = new ClassGraph()
                .acceptPackages(DEFAULT_PACKAGE_NAME) 
                .enableClassInfo()
                .enableAnnotationInfo()
                .scan()) {

            for (ClassInfo classInfo : scanResult.getSubclasses(InstafelPatchGroup.class.getName())) {
                Class<?> clazz = classInfo.loadClass();
                if (InstafelPatchGroup.class.isAssignableFrom(clazz)) {
                    groups.add((Class<? extends InstafelPatchGroup>) clazz);
                }
            }
        }

        return groups; 
    }

    @SuppressWarnings("unchecked")
    public static Set<Class<? extends InstafelPatch>> findPatchClassesInPackage() {
        Set<Class<? extends InstafelPatch>> patches = new HashSet<>();

        try (ScanResult scanResult = new ClassGraph()
                .acceptPackages(DEFAULT_PACKAGE_NAME) 
                .enableClassInfo()
                .enableAnnotationInfo()
                .scan()) {

            for (ClassInfo classInfo : scanResult.getSubclasses(InstafelPatch.class.getName())) {
                Class<?> clazz = classInfo.loadClass();
                if (InstafelPatch.class.isAssignableFrom(clazz)) {
                    patches.add((Class<? extends InstafelPatch>) clazz);
                }
            }
        }
        return patches;
    }
}
