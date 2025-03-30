package me.mamiiblt.instafel.patcher.resources.vt;

import java.util.ArrayList;
import java.util.List;

import me.mamiiblt.instafel.patcher.resources.types.TId;

public class ResourcesId {
    private List<TId> ids = new ArrayList<>();

    public void add(TId string) {
        ids.add(string);
    }

    public List<TId> getAll() {
        return ids;
    }

    public List<TId> getByName(String name) {
        List<TId> results = new ArrayList<>();
        for (TId tid : ids) {
            if (tid.getName().contains(name)) {
                results.add(tid);
            }
        }
        return results;
    }

    public List<TId> getByType(String value) {
        List<TId> results = new ArrayList<>();
        for (TId tid : ids) {
            if (tid.getType().contains(value)) {
                results.add(tid);
            }
        }
        return results;
    }
}
