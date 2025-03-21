package me.mamiiblt.instafel.utils;

import android.widget.TextView;

public class DialogItem {
    private final TextView textView;
    private final String string_name;

    public DialogItem(TextView textView, String string_name) {
        this.textView = textView;
        this.string_name = string_name;
    }

    public String getStringName() {
        return string_name;
    }

    public TextView getTextView() {
        return textView;
    }
}
