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

public class PatchLoader {

    public static final String DEFAULT_PACKAGE_NAME = "me.mamiiblt.instafel.patcher.patches";

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
                    
                    if (info.shortname().equals(shortPatchName)) {
                        Constructor<?> constructor = clazz.getDeclaredConstructor();
                        return (InstafelPatch) constructor.newInstance();
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
