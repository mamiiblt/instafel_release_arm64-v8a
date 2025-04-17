package me.mamiiblt.instafel.patcher.commands;

import java.util.List;

import me.mamiiblt.instafel.patcher.utils.patch.PatchLoader;
import me.mamiiblt.instafel.patcher.utils.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatchGroup;
import me.mamiiblt.instafel.patcher.utils.patch.PInfos;
import me.mamiiblt.instafel.patcher.utils.patch.PInfos.PatchInfo;

public class ListPatches implements Command {
    @Override
    public void execute(String[] args) {
        
        try {
            System.out.println("Patches: ");
            List<PInfos.PatchInfo> patchInfos = PatchLoader.getPatchInfos();
            for (PInfos.PatchInfo info : patchInfos) {
                if (info.listable() != false) {
                    System.out.println("    • " + getPatchInfoString(info)); 
                }
            }

            System.out.println("");
            System.out.println("Patch Groups:");
            List<PInfos.PatchGroupInfo> patchGroupInfos = PatchLoader.getPatchGroupInfos();
            for (PInfos.PatchGroupInfo patchGroupInfo : patchGroupInfos) {
                InstafelPatchGroup group = PatchLoader.findPatchGroupByShortname(patchGroupInfo.shortname());
                System.out.println("    • " + patchGroupInfo.name() + " (" + patchGroupInfo.shortname() + ")");

                group.loadPatches();
                for (Class<? extends InstafelPatch> patch : group.patches) {
                    PInfos.PatchInfo info = patch.getAnnotation(PInfos.PatchInfo.class);
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

    private String getPatchInfoString(PInfos.PatchInfo info) {
        return info.name() + " (" + info.shortname() + ")";
    }
}
