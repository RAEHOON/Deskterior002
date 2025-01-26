package com.example.desk0018.Tag;

import android.os.Parcel;
import android.os.Parcelable;

public class TagData implements Parcelable {
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

    // Parcelable을 위한 생성자 (Parcel에서 읽어오기)
    protected TagData(Parcel in) {
        category = in.readString();
        x = in.readInt();
        y = in.readInt();
        name = in.readString();
        url = in.readString();
    }

    public static final Creator<TagData> CREATOR = new Creator<TagData>() {
        @Override
        public TagData createFromParcel(Parcel in) {
            return new TagData(in);
        }

        @Override
        public TagData[] newArray(int size) {
            return new TagData[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeString(name);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public void setX(int x) {
        this.x = x;
    }

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
