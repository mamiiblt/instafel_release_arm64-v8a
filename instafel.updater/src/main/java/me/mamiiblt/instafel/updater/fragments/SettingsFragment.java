package me.mamiiblt.instafel.updater.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.widget.Toast;

import com.github.tttt55.materialyoupreferences.preferences.MaterialListPreference;
import com.github.tttt55.materialyoupreferences.preferences.MaterialPreference;
import com.github.tttt55.materialyoupreferences.preferences.MaterialSwitchGooglePreference;

import me.mamiiblt.instafel.updater.BuildConfig;
import me.mamiiblt.instafel.updater.MainActivity;
import me.mamiiblt.instafel.updater.R;
import me.mamiiblt.instafel.updater.update.UpdateWorkHelper;
import me.mamiiblt.instafel.updater.utils.LocalizationUtils;
import me.mamiiblt.instafel.updater.utils.Utils;

public class SettingsFragment extends PreferenceFragmentCompat {
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.app_options, rootKey);

        MaterialSwitchGooglePreference dynamicColorPreference = findPreference("material_you");
        if (dynamicColorPreference != null) {
            dynamicColorPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isDynamicColorEnabled = (Boolean) newValue;

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putBoolean("material_you", isDynamicColorEnabled).apply();

                getActivity().recreate();
                return true;
            });
        }

        MaterialListPreference checkerInterval = findPreference("checker_interval");
        checkerInterval.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putString("checker_interval", String.valueOf(newValue)).apply();
                Toast.makeText(getContext(), getActivity().getString(R.string.work_restarted), Toast.LENGTH_SHORT).show();
                UpdateWorkHelper.restartWork(getActivity());
                return true;
            }
        });

        MaterialListPreference language = findPreference("language");
        language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putString("language", String.valueOf(newValue)).apply();
                LocalizationUtils localizationUtils = new LocalizationUtils(getActivity().getApplicationContext());
                localizationUtils.updateAppLanguage();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                // getActivity().recreate();
                return true;
            }
        });

        MaterialPreference sourceCode = findPreference("source_code");
        sourceCode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/mamiiblt/instafel"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                return false;
            }
        });

        MaterialPreference createIssue = findPreference("create_issue");
        createIssue.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/mamiiblt/instafel/issues"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                return false;
            }
        });

        MaterialPreference updaterGuide = findPreference("updater_guide");
        updaterGuide.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://instafel.mamiiblt.me/about_updater"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                return false;
            }
        });
        MaterialPreference appVersion = findPreference("app_version");
        String[] versionName = BuildConfig.VERSION_NAME.split("-");
        String version = versionName[0];
        String tag = versionName[1];
        appVersion.setSummary(getActivity().getString(R.string.app_version_s, version, tag));
        appVersion.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Utils.showAppInfoDialog(getActivity());
                return false;
            }
        });
    }

    @Override
    public void onDisplayPreferenceDialog(@NonNull Preference preference) {
        if (preference instanceof ListPreference) {
            showPreferenceDialog((ListPreference) preference);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    private void showPreferenceDialog(ListPreference preference) {
        DialogFragment dialogFragment = new me.mamiiblt.instafel.updater.utils.MaterialListPreference();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", preference.getKey());
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getParentFragmentManager(), "androidx.preference.PreferenceFragment.DIALOG");
    }
}