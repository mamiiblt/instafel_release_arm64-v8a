package me.mamiiblt.instafel.patcher.patches;

import java.util.List;

import me.mamiiblt.instafel.patcher.utils.Log;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelTask;
import me.mamiiblt.instafel.patcher.utils.patch.PatchInfo;

@PatchInfo(
    name = "Example Patch",
    shortname = "example_patch",
    desc = "You can do everything with this patch!",
    author = "mamiiblt"
)
public class ExamplePatch extends InstafelPatch {

    @Override
    public List<InstafelTask> initializeTasks() {
        return List.of(
            exampleTask
        );
    }

    InstafelTask exampleTask = new InstafelTask("MERHABA TASK") {
        @Override
        public void execute() {
            Log.info("Nabersin canom");
        }
    };
    
}
