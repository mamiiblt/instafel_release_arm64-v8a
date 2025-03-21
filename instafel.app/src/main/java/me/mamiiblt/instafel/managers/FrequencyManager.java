package me.mamiiblt.instafel.managers;

import android.content.Context;

import me.mamiiblt.instafel.R;
import me.mamiiblt.instafel.ota.FreqLabels;
import me.mamiiblt.instafel.utils.PreferenceKeys;

public class FrequencyManager {
    public static void setFreq(Context ctx, int freq_id) {
        PreferenceManager preferenceManager = new PreferenceManager(ctx);
        preferenceManager.setPreferenceInt(PreferenceKeys.ifl_ota_freq, freq_id);
    }

    public static int getFreqId(Context ctx) {
        PreferenceManager preferenceManager = new PreferenceManager(ctx);
        return preferenceManager.getPreferenceInt(PreferenceKeys.ifl_ota_freq, FreqLabels.EVERY_OPEN);
    }

    public static String getFreq(Context ctx) {
        PreferenceManager preferenceManager = new PreferenceManager(ctx);
        int data = preferenceManager.getPreferenceInt(PreferenceKeys.ifl_ota_freq, FreqLabels.EVERY_OPEN);

        if (data != FreqLabels.EVERY_OPEN) {
            switch (data) {
                case 1:
                    return ctx.getString(R.string.ifl_a5_sub_freq_01);
                case 2:
                    return ctx.getString(R.string.ifl_a5_sub_freq_02);
                case 3:
                    return ctx.getString(R.string.ifl_a5_sub_freq_03);
                case 4:
                    return ctx.getString(R.string.ifl_a5_sub_freq_04);
                case 5:
                    return ctx.getString(R.string.ifl_a5_sub_freq_05);
            }
        } else {
            return ctx.getString(R.string.ifl_a5_sub_freq_00);
        }

        return ctx.getString(R.string.ifl_a5_sub_freq_00);
    }
}
