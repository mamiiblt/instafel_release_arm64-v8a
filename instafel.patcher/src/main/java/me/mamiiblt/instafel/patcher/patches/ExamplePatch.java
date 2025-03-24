package me.mamiiblt.instafel.patcher.patches;

import me.mamiiblt.instafel.patcher.InstafelPatch;
import me.mamiiblt.instafel.patcher.InstafelTask;

public class ExamplePatch extends InstafelPatch {

    public ExamplePatch() {
        super(new PatchBuilder()
            .patchName("Example Patch")
            .patchAuthor("mamiiblt")
            .patchDesc("açıklama işte"));
    }

    @Override
    public void initializeSteps() {
        addTask(step1);
        addTask(step2);
    }

    InstafelTask step1 = new InstafelTask("Örnek task 1") {
        @Override
        public void execute() {
            System.out.println("naber high");
            success("bu nedir lO");
        }
    };

    InstafelTask step2 = new InstafelTask("Örnek task 2") {
        @Override
        public void execute() {
            System.out.println("pisu pisu");
            success("leaaaawww");
        }
    };
}
