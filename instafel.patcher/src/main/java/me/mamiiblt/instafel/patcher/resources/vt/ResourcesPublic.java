package me.mamiiblt.instafel.patcher.resources.vt;

import java.util.ArrayList;
import java.util.List;

import me.mamiiblt.instafel.patcher.resources.types.TPublic;

public class ResourcesPublic {
    private List<TPublic> publics = new ArrayList<>();

    public void add(TPublic attr) {
        publics.add(attr);
    }

    public List<TPublic> getAll() {
        return publics;
    }

    public List<TPublic> getByName(String name) {
        List<TPublic> results = new ArrayList<>();
        for (TPublic tPublic : publics) {
            if (tPublic.getName().contains(name)) {
                results.add(tPublic);
            }
        }
        return results;
    }

    public List<TPublic> getByType(String value) {
        List<TPublic> results = new ArrayList<>();
        for (TPublic tPublic : publics) {
            if (tPublic.getType().contains(value)) {
                results.add(tPublic);
            }
        }
        return results;
    }

    public List<TPublic> getById(String value) {
        List<TPublic> results = new ArrayList<>();
        for (TPublic tPublic : publics) {
            if (tPublic.getId().equals(value)) {
                results.add(tPublic);
            }
        }
        return results;
    }
}
