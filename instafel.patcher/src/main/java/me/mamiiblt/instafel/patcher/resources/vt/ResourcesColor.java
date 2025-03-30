package me.mamiiblt.instafel.patcher.resources.vt;

import java.util.ArrayList;
import java.util.List;

import me.mamiiblt.instafel.patcher.resources.types.TColor;

public class ResourcesColor {
    private List<TColor> colors = new ArrayList<>();

    public void add(TColor string) {
        colors.add(string);
    }

    public List<TColor> getAll() {
        return colors;
    }

    public List<TColor> getByName(String name) {
        List<TColor> results = new ArrayList<>();
        for (TColor tColor : colors) {
            if (tColor.getName().contains(name)) {
                results.add(tColor);
            }
        }
        return results;
    }

    public List<TColor> getByValue(String value) {
        List<TColor> results = new ArrayList<>();
        for (TColor tColor : colors) {
            if (tColor.getValue().contains(value)) {
                results.add(tColor);
            }
        }
        return results;
    }
}
