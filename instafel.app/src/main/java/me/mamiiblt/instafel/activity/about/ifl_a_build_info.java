package me.mamiiblt.instafel.activity.about;

import static me.mamiiblt.instafel.utils.GeneralFn.updateIflUi;
import static me.mamiiblt.instafel.utils.Localizator.updateIflLocale;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import me.mamiiblt.instafel.R;
import me.mamiiblt.instafel.InstafelEnv;
import me.mamiiblt.instafel.ota.IflEnvironment;
import me.mamiiblt.instafel.ui.TileLarge;
import me.mamiiblt.instafel.utils.GeneralFn;

public class ifl_a_build_info extends AppCompatActivity {

    private TileLarge tileGenerationId, tilePatcherCommit, tileAppliedPatches, tileInstallationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateIflUi(this);
        updateIflLocale(this, false);
        setContentView(R.layout.ifl_at_build_info);

        tileGenerationId = findViewById(R.id.ifl_tile_generation_id);
        tilePatcherCommit = findViewById(R.id.ifl_tile_patcher_commit);
        tileAppliedPatches = findViewById(R.id.ifl_tile_applied_patches);
        tileInstallationType = findViewById(R.id.ifl_tile_installation_type);

        tileGenerationId.setSubtitleText(IflEnvironment.getGenerationId(this));
        tileGenerationId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrlInWeb("https://instafel.mamiiblt.me/download_instafel?version=v" + InstafelEnv.IFL_VERSION);
            }
        });

        tilePatcherCommit.setSubtitleText(InstafelEnv.PATCHER_COMMIT);
        tilePatcherCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrlInWeb("https://github.com/mamiiblt/instafel/commit/" + InstafelEnv.PATCHER_COMMIT);
            }
        });

        String appliedPatches = InstafelEnv.APPLIED_PATCHES.replace(",", "\n");
        tileAppliedPatches.setSubtitleText(appliedPatches);

        tileInstallationType.setSubtitleText(IflEnvironment.getTypeString(this, Locale.getDefault()));
    }

    public void openUrlInWeb(String url) {
        GeneralFn.openInWebBrowser(this, url);
    }
}