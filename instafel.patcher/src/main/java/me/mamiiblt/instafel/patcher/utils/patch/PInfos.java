package me.mamiiblt.instafel.patcher.utils.patch;

import java.lang.annotation.*;

public class PInfos {

    @Retention(RetentionPolicy.RUNTIME) 
    @Target(ElementType.TYPE)
    public @interface PatchInfo {
        String name();
        String author();
        String desc();
        String shortname();
        boolean listable();
        boolean runnable();
    }

    @Retention(RetentionPolicy.RUNTIME) 
    @Target(ElementType.TYPE)
    public @interface PatchGroupInfo {
        String name();
        String author();
        String desc();
        String shortname();
    }
}
