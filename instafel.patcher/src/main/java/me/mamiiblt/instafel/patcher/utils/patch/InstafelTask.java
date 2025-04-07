package me.mamiiblt.instafel.patcher.utils.patch;

import me.mamiiblt.instafel.patcher.utils.Environment;
import me.mamiiblt.instafel.patcher.utils.Log;

public abstract class InstafelTask {
    public String stepName;
    public int taskStatus = 0;
    public String finishString;

    public InstafelTask(String stepName) {
        this.stepName = stepName;
    }

    public abstract void execute() throws Exception;

    public void success(String message) {
        taskStatus = 1;
        finishString = message;
        finishTask();
    }

    public void failure(String message) {
        taskStatus = 2;
        finishString = message;
        finishTask();
    }

    private void finishTask() {
        if (taskStatus == 1) {
            Log.info("SUCCESS: " + finishString);
        } else if (taskStatus == 2) {
            Log.info("FAILURE: " + finishString);
            Log.info(Environment.SPERATOR_STR);
            System.exit(-1);
        }
    }
}
