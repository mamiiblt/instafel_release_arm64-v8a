package me.mamiiblt.instafel.updater.update;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.mamiiblt.instafel.updater.utils.LogUtils;

public class UpdateWorkHelper {

    private static final String WORK_NAME = "INSTAFEL_UPDATER_WORK";

    public static void scheduleWork(Context ctx) {
        LogUtils logUtils = new LogUtils(ctx);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        PeriodicWorkRequest workRequest;
        if (sharedPreferences.getBoolean("15m_rule", false)) {
            workRequest = new PeriodicWorkRequest.Builder(UpdateWork.class, 15, TimeUnit.MINUTES).build();
            logUtils.w("Checker interval is set to 15m (BREAK_RULE)");
        } else {
            int interval =  getCheckIntervalHour(ctx);
            workRequest = new PeriodicWorkRequest.Builder(UpdateWork.class, interval, TimeUnit.HOURS).build();
            logUtils.w("Checker interval is set to " + interval + " hour");
        }
        WorkManager.getInstance(ctx).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
        );
        logUtils.w("Work succesfully scheduled");
    }

    public static void cancelWork(Context ctx) {
        LogUtils logUtils = new LogUtils(ctx);
        WorkManager.getInstance(ctx).cancelUniqueWork(WORK_NAME);
        logUtils.w("Work cancelled");
    }

    public static LiveData<List<WorkInfo>> getWorkStatus(Context ctx) {
        return WorkManager.getInstance(ctx).getWorkInfosForUniqueWorkLiveData(WORK_NAME);
    }

    public static void isWorkManagerActive(Context context, final WorkManagerActiveCallback callback) {
        WorkManager workManager = WorkManager.getInstance(context);
        LiveData<List<WorkInfo>> workInfosLiveData = workManager.getWorkInfosForUniqueWorkLiveData(WORK_NAME);

        workInfosLiveData.observeForever(new Observer<List<WorkInfo>>() {
            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                if (workInfos == null || workInfos.isEmpty()) {
                    callback.onResult(false);
                    return;
                }

                boolean isActive = false;
                for (WorkInfo workInfo : workInfos) {
                    WorkInfo.State state = workInfo.getState();
                    if (state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED) {
                        isActive = true;
                        break;
                    }
                }

                callback.onResult(isActive);
            }
        });
    }

    public static void restartWork(Activity activity) {
        cancelWork(activity);
        scheduleWork(activity);
        LogUtils logUtils = new LogUtils(activity);
        logUtils.w("Work is restarted");
    }

    public static int getCheckIntervalHour(Context ctx) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        String checkIntervalString = sharedPreferences.getString("checker_interval", "4 hour");
        switch (checkIntervalString) {
            case "2 hour":
                return 2;
            case "3 hour":
                return 3;
            case "4 hour":
                return 4;
            case "6 hour":
                return 6;
            case "8 hour":
                return 8;
            case "12 hour":
                return 12;
            case "14 hour":
                return 14;
            case "24 hour":
                return 24;
            default:
                return 4;
        }
    }

    public interface WorkManagerActiveCallback {
        void onResult(boolean isActive);
    }
}
