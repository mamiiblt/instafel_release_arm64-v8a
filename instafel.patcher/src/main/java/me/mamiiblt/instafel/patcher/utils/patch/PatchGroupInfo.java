package me.mamiiblt.instafel.patcher.utils.patch;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.TYPE)
public @interface PatchGroupInfo {
    String name();
    String author();
    String desc();
    String shortname();
}
