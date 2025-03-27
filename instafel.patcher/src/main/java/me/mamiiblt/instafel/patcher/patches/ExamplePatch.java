package me.mamiiblt.instafel.patcher.patches;

import java.util.List;

import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PatchInfo;

@PatchInfo(
    name = "Example Patch",
    desc = "You can do everything with this patch!",
    author = "mamiiblt"
)
public class ExamplePatch extends InstafelPatch {

    public ExamplePatch() {
        List.of(
            new InstafelTask("Örnek task 1") {
                @Override
                public void execute() {
                    System.out.println("naber high");
                    success("bu nedir lO");
                    failure("naber kafam high");
                }
            },
            new InstafelTask("Örnek task 2") {
                @Override
                public void execute() {
                    System.out.println("pisu pisu");
                    success("leaaaawww");
                }
            }
        ).forEach(this::addTask);
    }
}
