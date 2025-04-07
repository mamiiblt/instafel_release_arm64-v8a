package me.mamiiblt.instafel.activity.about;

import static me.mamiiblt.instafel.utils.GeneralFn.updateIflUi;
import static me.mamiiblt.instafel.utils.Localizator.updateIflLocale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

import me.mamiiblt.instafel.R;
import me.mamiiblt.instafel.ota.IflEnvironment;
import me.mamiiblt.instafel.ui.TileLarge;
import me.mamiiblt.instafel.utils.GeneralFn;

public class ifl_a_about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateIflUi(this);
        updateIflLocale(this, false);
        setContentView(R.layout.ifl_at_about);

        TileLarge tileVersion = findViewById(R.id.ifl_tile_ifl_version);
        TileLarge tileInstagramVersion = findViewById(R.id.ifl_tile_ig_version);
        tileInstagramVersion.setSubtitleText(IflEnvironment.getIgVerAndCodeString(this));
        tileVersion.setSubtitleText(IflEnvironment.getIflVersionString(this));

        tileInstagramVersion.setSubtitleText(IflEnvironment.getIgVerAndCodeString(this));
        tileInstagramVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClipboardManager) ifl_a_about.this.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("ifl_log_clip", tileInstagramVersion.getSubtitle()));
            }
        });

        TileLarge tileBuildInfo = findViewById(R.id.ifl_tile_build_info);
        tileBuildInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneralFn.startIntent(ifl_a_about.this, ifl_a_build_info.class);
            }
        });

        TileLarge tileContributors = findViewById(R.id.ifl_tile_contributors);
        tileContributors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralFn.startIntent(ifl_a_about.this, ifl_a_about_contributors.class);
            }
        });

        TileLarge tileTranslators = findViewById(R.id.ifl_tile_translators);
        tileTranslators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneralFn.startIntent(ifl_a_about.this, ifl_a_about_translators.class);
            }
        });

        TileLarge devTile = findViewById(R.id.ifl_tile_developer);
        TileLarge sourceCode = findViewById(R.id.ifl_tile_source_code);

        devTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrlInWeb("https://" + devTile.getSubtitle());
            }
        });

        sourceCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrlInWeb("https://" + sourceCode.getSubtitle());
            }
        });
    }

    public void openUrlInWeb(String url) {
        GeneralFn.openInWebBrowser(this, url);
    }
}