package me.mamiiblt.instafel.utils.models;

public class CrashData {
    private Object msg, trace, className, date;

    public CrashData(Object msg, Object trace, Object className) {
        this.msg = msg;
        this.trace = trace;
        this.className = className;
    }

    public Object getMsg() {
        return msg;
    }

    public Object getTrace() {
        return trace;
    }

    public Object getClassName() {
        return className;
    }

    public Object getDate() {
        return date;
    }
}
