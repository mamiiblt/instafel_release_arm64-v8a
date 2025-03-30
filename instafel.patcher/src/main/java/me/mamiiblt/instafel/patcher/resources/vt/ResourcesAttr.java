package me.mamiiblt.instafel.patcher.resources.vt;

import java.util.ArrayList;
import java.util.List;

import me.mamiiblt.instafel.patcher.resources.types.TAttr;

public class ResourcesAttr {
    private List<TAttr> attrs = new ArrayList<>();

    public void add(TAttr attr) {
        attrs.add(attr);
    }

    public List<TAttr> getAll() {
        return attrs;
    }

    public List<TAttr> getByName(String name) {
        List<TAttr> results = new ArrayList<>();
        for (TAttr tAttr : attrs) {
            if (tAttr.getName().contains(name)) {
                results.add(tAttr);
            }
        }
        return results;
    }

    public List<TAttr> getByFormat(String value) {
        List<TAttr> results = new ArrayList<>();
        for (TAttr tAttr : attrs) {
            if (tAttr.getFormat().contains(value)) {
                results.add(tAttr);
            }
        }
        return results;
    }
}
