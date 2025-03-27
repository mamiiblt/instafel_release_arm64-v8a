package me.mamiiblt.instafel.patcher.commands;

import java.util.List;

import me.mamiiblt.instafel.patcher.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.patches.ExamplePatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;

public class ListPatches implements Command {

    List<InstafelPatch> patches = List.of(
        new ExamplePatch()
    );

    @Override
    public void execute(String[] args) {
        for (int i = 0; i < patches.size(); i++) {
            InstafelPatch patch = patches.get(i);
            System.out.println(patch.name + " - by " + patch.author);
            System.out.println(patch.description);
            System.out.println("");
        }
        System.out.println("Totally listened " + patches.size() + " patch.");
    }
}
