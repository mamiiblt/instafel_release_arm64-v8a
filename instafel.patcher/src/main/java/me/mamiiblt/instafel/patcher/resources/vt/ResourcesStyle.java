package me.mamiiblt.instafel.patcher.resources.vt;

import java.util.ArrayList;
import java.util.List;

import me.mamiiblt.instafel.patcher.resources.types.TStyle;

public class ResourcesStyle {
    private List<TStyle> styles = new ArrayList<>();

    public void add(TStyle string) {
        styles.add(string);
    }

    public List<TStyle> getAll() {
        return styles;
    }

    public List<TStyle> getByName(String name) {
        List<TStyle> results = new ArrayList<>();
        for (TStyle tStyle : styles) {
            if (tStyle.getName().contains(name)) {
                results.add(tStyle);
            }
        }
        return results;
    }
}
