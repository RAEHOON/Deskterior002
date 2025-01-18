package com.example.desk0018.Tag;

public class TagData {
    private String category; // 유형
    private int x; // X 좌표
    private int y; // Y 좌표
    private String name; // 태그 이름
    private String url; // 태그 URL


    public TagData(String category, int x, int y, String name, String url) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.name = name;
        this.url = url;

    }

    // Getter 및 Setter
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getX() {
        return x;
    }


    public void setX(int x) { this.x = x; }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
