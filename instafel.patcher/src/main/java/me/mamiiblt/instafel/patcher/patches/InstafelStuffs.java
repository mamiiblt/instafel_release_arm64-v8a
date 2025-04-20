package me.mamiiblt.instafel.patcher.patches;

import java.util.List;

import me.mamiiblt.instafel.patcher.patches.general.*;
import me.mamiiblt.instafel.patcher.utils.patch.*;

@PInfos.PatchGroupInfo(
    name = "Instafel Stuffs",
    shortname = "instafel",
    desc = "You can add Instafel stuffs with this patches.",
    author = "mamiiblt"
)
public class InstafelStuffs extends InstafelPatchGroup {

    @Override
    public List<Class<? extends InstafelPatch>> initializePatches() throws Exception {
        return List.of(
            GetGenerationInfo.class, // get_generation_info
            CopyInstafelSources.class, // copy_instafel_src
            AddInitInstafel.class, // add_init_instafel
            AddLongClickEvent.class, // add_long_click_event
            AddAppTrigger.class // add_app_trigger
        );
    }
}
