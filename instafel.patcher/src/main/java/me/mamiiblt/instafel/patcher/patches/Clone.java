package me.mamiiblt.instafel.patcher.patches;

import java.util.List;

import me.mamiiblt.instafel.patcher.patches.clone.CloneGeneral;
import me.mamiiblt.instafel.patcher.patches.clone.ClonePackageReplacer;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatch;
import me.mamiiblt.instafel.patcher.utils.patch.InstafelPatchGroup;
import me.mamiiblt.instafel.patcher.utils.patch.PatchGroupInfo;

@PatchGroupInfo (
    name = "Clone Patches",
    shortname = "clone",
    desc = "These patchs needs to be applied for generate clone in build.",
    author = "mamiiblt"
)
public class Clone extends InstafelPatchGroup {

    @Override
    public List<Class<? extends InstafelPatch>> initializePatches() throws Exception {
        return List.of(
            CloneGeneral.class,
            ClonePackageReplacer.class
        );
    }

}
