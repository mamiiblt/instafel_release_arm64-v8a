package me.mamiiblt.instafel.patcher.resources.vt;

import java.util.ArrayList;
import java.util.List;

import me.mamiiblt.instafel.patcher.resources.types.TString;

public class ResourcesString {
    private List<TString> strings = new ArrayList<>();

    public void add(TString string) {
        strings.add(string);
    }

    public List<TString> getAll() {
        return strings;
    }

    public List<TString> getByName(String name) {
        List<TString> results = new ArrayList<>();
        for (TString tString : strings) {
            if (tString.getName().contains(name)) {
                results.add(tString);
            }
        }
        return results;
    }

    public List<TString> getByValue(String value) {
        List<TString> results = new ArrayList<>();
        for (TString tString : strings) {
            if (tString.getValue().contains(value)) {
                results.add(tString);
            }
        }
        return results;
    }
}
