package me.mamiiblt.instafel.patcher.putils;

import java.util.ArrayList;
import java.util.List;

import me.mamiiblt.instafel.patcher.utils.Log;

public class InstafelPatch {
    private String name, author, description;
    private List<InstafelTask> tasks;
    
    public InstafelPatch(PatchBuilder builder) {
        this.name = builder.name;
        this.author = builder.author;
        this.description = builder.description;
        this.tasks = new ArrayList<>();
    }

    public void addTask(InstafelTask instafelTask) {
        tasks.add(instafelTask);
    }

    public void execute() {
        System.out.println("--------------------");
        System.out.println(name);
        System.out.println("by @" + author);
        System.out.println(description);
        System.out.println("");
        System.out.println("--------------------");
        Log.info("Totally " + tasks.size() + " task initialized.");
        Log.info("Executing tasks...");

        for (int i = 0; i < tasks.size(); i++) {
            InstafelTask task = tasks.get(i);
            System.out.println("");
            System.out.println("Task " + (i + 1 ) + " - " + task.stepName);
            task.execute();
            System.out.println("");
        }

        System.out.println("All tasks runned succesfully.");
    }

    public void initializeSteps() { }

    public static class PatchBuilder {
        private String name;
        private String author;
        private String description;

        public PatchBuilder patchName(String patchName) {
            this.name = patchName;
            return this;
        }

        public PatchBuilder patchAuthor(String patchAuthor) {
            this.author = patchAuthor;
            return this;
        }

        public PatchBuilder patchDesc(String patchDescription) {
            this.description = patchDescription;
            return this;
        }

        public InstafelPatch build() {
            return new InstafelPatch(this);
        }
    }
}
