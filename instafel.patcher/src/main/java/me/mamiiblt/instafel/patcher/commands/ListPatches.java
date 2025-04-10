package me.mamiiblt.instafel.patcher.commands;

import java.util.List;

import com.android.tools.smali.smali.smaliParser.literal_return;

import me.mamiiblt.instafel.patcher.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.utils.patch.PatchLoader;
import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatchGroup;
import me.mamiiblt.instafel.patcher.utils.patch.PatchGroupInfo;
import me.mamiiblt.instafel.patcher.utils.patch.PatchInfo;

public class ListPatches implements Command {
    @Override
    public void execute(String[] args) {
        
        try {
            System.out.println("Patches: ");
            List<PatchInfo> patchInfos = PatchLoader.getPatchInfos();
            for (PatchInfo info : patchInfos) {
                if (info.listable() != false) {
                    System.out.println("    • " + getPatchInfoString(info)); 
                }
            }

            System.out.println("");
            System.out.println("Patch Groups:");
            List<PatchGroupInfo> patchGroupInfos = PatchLoader.getPatchGroupInfos();
            for (PatchGroupInfo patchGroupInfo : patchGroupInfos) {
                InstafelPatchGroup group = PatchLoader.findPatchGroupByShortname(patchGroupInfo.shortname());
                System.out.println("    • " + patchGroupInfo.name() + " (" + patchGroupInfo.shortname() + ")");

                group.loadPatches();
                for (Class<? extends InstafelPatch> patch : group.patches) {
                    PatchInfo info = patch.getAnnotation(PatchInfo.class);
                    System.out.println("        - " + getPatchInfoString(info));
                    
                }
            }

            System.out.println("");
            System.out.println("Use run <wdir> name... for executing patches / patch groups");
            System.out.println("Totally found " + patchInfos.size() + " patch.");
        } catch (Exception e) {
            e.printStackTrace();
        }     
    }

    private String getPatchInfoString(PatchInfo info) {
        return info.name() + " (" + info.shortname() + ")";
    }
}
