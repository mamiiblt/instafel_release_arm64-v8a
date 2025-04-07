package me.mamiiblt.instafel.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import me.mamiiblt.instafel.R;
import me.mamiiblt.instafel.InstafelEnv;
import me.mamiiblt.instafel.managers.PreferenceManager;
import me.mamiiblt.instafel.ui.TileLargeSwitch;
import me.mamiiblt.instafel.utils.GeneralFn;
import me.mamiiblt.instafel.utils.Localizator;
import me.mamiiblt.instafel.utils.PreferenceKeys;

public class ifl_a_misc extends AppCompatActivity {
    PreferenceManager preferenceManager;
    TileLargeSwitch tileAmoled;
    Switch tileAmoledSwitch;

    @Override 
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GeneralFn.updateIflUi(this);
        Localizator.updateIflLocale(this, false);
        setContentView(R.layout.ifl_at_misc);
        this.tileAmoled = findViewById(R.id.ifl_tile_misc_amoled);
        this.tileAmoledSwitch = tileAmoled.getSwitchView();
        PreferenceManager preferenceManager = new PreferenceManager(this);
        this.preferenceManager = preferenceManager;
        this.tileAmoledSwitch.setChecked(preferenceManager.getPreferenceBoolean(PreferenceKeys.ifl_enable_amoled_theme, false).booleanValue());
        this.tileAmoled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifl_a_misc.this.enableOrDisableAmoledTheme(!tileAmoledSwitch.isChecked());
                ifl_a_misc.this.tileAmoledSwitch.setChecked(!ifl_a_misc.this.tileAmoledSwitch.isChecked());
            }
        });
        this.tileAmoledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
                ifl_a_misc.this.enableOrDisableAmoledTheme(status);
            }
        });

        TileLargeSwitch tileRemoveAds = findViewById(R.id.ifl_tile_remove_ads_section);
        Switch tileRemoveAdsSwitch = tileRemoveAds.getSwitchView();
        boolean removedAdsPatchApplied = InstafelEnv.isPatchApplied("remove_ads");
        if (removedAdsPatchApplied) {
            tileRemoveAdsSwitch.setChecked(true);
            tileRemoveAdsSwitch.setEnabled(false);
            tileRemoveAds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ifl_a_misc.this, ifl_a_misc.this.getString(R.string.ifl_a0_14), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            tileRemoveAds.setVisibility(View.GONE);
        }
    }

    private void enableOrDisableAmoledTheme(boolean z) {
        if (z) {
            this.preferenceManager.setPreferenceBoolean(PreferenceKeys.ifl_enable_amoled_theme, true);
            Toast.makeText(this, getString(R.string.ifl_a0_13), Toast.LENGTH_SHORT).show();
        } else {
            this.preferenceManager.setPreferenceBoolean(PreferenceKeys.ifl_enable_amoled_theme, false);
        }
    }
}