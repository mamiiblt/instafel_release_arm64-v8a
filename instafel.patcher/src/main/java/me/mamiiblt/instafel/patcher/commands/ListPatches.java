package me.mamiiblt.instafel.patcher.commands;

import java.util.List;

import me.mamiiblt.instafel.patcher.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.utils.patch.PatchLoader;
import me.mamiiblt.instafel.patcher.utils.patch.PatchInfo;

public class ListPatches implements Command {
    @Override
    public void execute(String[] args) {
        try {
            List<PatchInfo> patchInfos = PatchLoader.getPatchInfos();
            
            for (PatchInfo info : patchInfos) {                
                System.out.println(info.name() + " (" + info.shortname() + ")");
                System.out.println("Added by " + info.author());
                System.out.println(info.desc());
                System.out.println("");
            }

            System.out.println("Totally found " + patchInfos.size() + " patch.");
        } catch (Exception e) {
            e.printStackTrace();
        }     

    }
}
