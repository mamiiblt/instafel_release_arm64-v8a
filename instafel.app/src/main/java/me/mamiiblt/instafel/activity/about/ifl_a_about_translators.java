package me.mamiiblt.instafel.activity.about;

import static me.mamiiblt.instafel.utils.GeneralFn.updateIflUi;
import static me.mamiiblt.instafel.utils.Localizator.updateIflLocale;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import me.mamiiblt.instafel.R;
import me.mamiiblt.instafel.utils.GeneralFn;

public class ifl_a_about_translators extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateIflUi(this);
        updateIflLocale(this, false);
        setContentView(R.layout.ifl_at_about_translators);
        findViewById(R.id.ifl_tile_lang_zan).setOnClickListener(new View.OnClickListener() { // from class: me.mamiiblt.instafel.activity.about.ifl_a_about_translators.1
            @Override 
            public void onClick(View view) {
                ifl_a_about_translators.this.openUrlInWeb("https://youtsit.ee/zan1456");
            }
        });
        findViewById(R.id.ifl_tile_lang_deutch).setOnClickListener(new View.OnClickListener() { // from class: me.mamiiblt.instafel.activity.about.ifl_a_about_translators.2
            @Override 
            public void onClick(View view) {
                ifl_a_about_translators.this.openUrlInWeb("https://github.com/RuesanG");
            }
        });
        findViewById(R.id.ifl_tile_lang_france).setOnClickListener(new View.OnClickListener() { // from class: me.mamiiblt.instafel.activity.about.ifl_a_about_translators.3
            @Override 
            public void onClick(View view) {
                ifl_a_about_translators.this.openUrlInWeb("https://www.instagram.com/sincrypt.hemk651");
            }
        });
        findViewById(R.id.ifl_tile_lang_john).setOnClickListener(new View.OnClickListener() { // from class: me.mamiiblt.instafel.activity.about.ifl_a_about_translators.4
            @Override
            public void onClick(View view) {
                ifl_a_about_translators.this.openUrlInWeb("https://www.instagram.com/ioannisxirr");
            }
        });
        findViewById(R.id.ifl_tile_lang_espanol).setOnClickListener(new View.OnClickListener() { // from class: me.mamiiblt.instafel.activity.about.ifl_a_about_translators.5
            @Override 
            public void onClick(View view) {
                ifl_a_about_translators.this.openUrlInWeb("https://github.com/nubesurrealista");
            }
        });
        findViewById(R.id.ifl_tile_lang_portugal).setOnClickListener(new View.OnClickListener() { // from class: me.mamiiblt.instafel.activity.about.ifl_a_about_translators.6
            @Override
            public void onClick(View view) {
                ifl_a_about_translators.this.openUrlInWeb("https://github.com/Suburbanno");
            }
        });
        findViewById(R.id.ifl_tile_lang_indian).setOnClickListener(new View.OnClickListener() { // from class: me.mamiiblt.instafel.activity.about.ifl_a_about_translators.7
            @Override
            public void onClick(View view) {
                ifl_a_about_translators.this.openUrlInWeb("https://github.com/imsahilansarii");
            }
        });
    }


    public void openUrlInWeb(String url) {
        GeneralFn.openInWebBrowser(this, url);
    }
}