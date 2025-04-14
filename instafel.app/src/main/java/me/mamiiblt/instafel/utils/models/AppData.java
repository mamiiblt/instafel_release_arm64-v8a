package me.mamiiblt.instafel.utils.models;

public class AppData {
    private Object ifl_ver, ig_ver, ig_ver_code, ig_itype;

    public AppData(Object ifl_ver, Object ig_ver, Object ig_ver_code, Object ig_itype) {
        this.ifl_ver = ifl_ver;
        this.ig_ver = ig_ver;
        this.ig_ver_code = ig_ver_code;
        this.ig_itype = ig_itype;
    }

    public Object getIfl_ver() {
        return ifl_ver;
    }

    public Object getIg_ver() {
        return ig_ver;
    }

    public Object getIg_ver_code() {
        return ig_ver_code;
    }

    public Object getIg_itype() {
        return ig_itype;
    }
}
