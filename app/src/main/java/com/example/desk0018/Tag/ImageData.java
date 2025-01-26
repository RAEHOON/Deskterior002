package com.example.desk0018.Tag;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ImageData implements Parcelable {
    private int imageCount; // 이미지 고유 식별자
    private String imageUrl; // 이미지 URL
    private List<TagData> tags; // 태그 리스트

    // 생성자
    public ImageData(int imageCount, String imageUrl, List<TagData> tags) {
        this.imageCount = imageCount;
        this.imageUrl = imageUrl;
        this.tags = tags;
    }

    // Parcelable을 위한 생성자 (Parcel에서 읽어오기)
    protected ImageData(Parcel in) {
        imageCount = in.readInt();
        imageUrl = in.readString();
        tags = in.createTypedArrayList(TagData.CREATOR);  // TagData도 Parcelable 구현 필요
    }

    public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
        @Override
        public ImageData createFromParcel(Parcel in) {
            return new ImageData(in);
        }

        @Override
        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageCount);
        dest.writeString(imageUrl);
        dest.writeTypedList(tags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getter 메서드
    public int getImageCount() {
        return imageCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<TagData> getTags() {
        return tags;
    }
}
