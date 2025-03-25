package me.mamiiblt.instafel.patcher.utils.models;

public class LineData {

    private int num;
    private String content;

    public LineData(int num, String content) {
        this.num = num;
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public int getNum() {
        return this.num;
    }
}
