package me.mamiiblt.instafel.patcher.resources.types;

public class TString {
    private String name, value;

    public TString(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
