package me.mamiiblt.instafel.activity.about;

import static me.mamiiblt.instafel.utils.GeneralFn.updateIflUi;
import static me.mamiiblt.instafel.utils.Localizator.updateIflLocale;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import me.mamiiblt.instafel.R;
import me.mamiiblt.instafel.utils.GeneralFn;

public class ifl_a_about_contributors extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateIflUi(this);
        updateIflLocale(this, false);
        setContentView(R.layout.ifl_at_about_contributors);

        findViewById(R.id.ifl_tile_ct14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifl_a_about_contributors.this.openUrlInWeb("https://t.me/amazingscripts");
            }
        });
        
        findViewById(R.id.ifl_tile_ct01).setOnClickListener(new View.OnClickListener() { 
            @Override
            public void onClick(View view) {
                ifl_a_about_contributors.this.openUrlInWeb("https://t.me/instasmashrepo");
            }
        });
        findViewById(R.id.ifl_tile_ct02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifl_a_about_contributors.this.openUrlInWeb("https://instagram.com/ioannisxirr");
            }
        });
        findViewById(R.id.ifl_tile_ct03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifl_a_about_contributors.this.openUrlInWeb("https://t.me/RelevantUpdates");
            }
        });

        findViewById(R.id.ifl_tile_ct08).setOnClickListener(new View.OnClickListener() { 
            @Override
            public void onClick(View view) {
                ifl_a_about_contributors.this.openUrlInWeb("https://t.me/kemaIist");
            }
        });
    }


    public void openUrlInWeb(String url) {
        GeneralFn.openInWebBrowser(this, url);
    }
}