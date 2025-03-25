package me.mamiiblt.instafel.patcher.putils;

public abstract class InstafelTask {
    protected String stepName;

    public InstafelTask(String stepName) {
        this.stepName = stepName;
    }

    public abstract void execute();

    protected void success(String message) {
        System.out.println("Success: " + message);
    }

    protected void failure(String message) {
        System.out.println("Failure: " + message);
    }
}
