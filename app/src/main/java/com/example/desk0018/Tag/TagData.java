package com.example.desk0018.Tag;

public class TagData {
    private int x; // 태그 X 좌표
    private int y; // 태그 Y 좌표
    private String name;
    private String url; // 태그 URL

    // 생성자
    public TagData(int x, int y, String name, String url) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.url = url;
    }

    // Getter 메서드
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getUrl() {
        return url;
    }
    public String getName(){ return name;}

    @Override
    public String toString() {
        return "TagData{name='" + name + "', url='" + url + "'}";
    }
}
