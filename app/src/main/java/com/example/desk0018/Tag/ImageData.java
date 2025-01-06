package com.example.desk0018.Tag;

import java.util.List;

public class ImageData {
    private int imageCount; // 이미지 고유 식별자
    private String imageUrl; // 이미지 URL
    private List<TagData> tags; // 태그 리스트

    // 생성자
    public ImageData(int imageCount, String imageUrl, List<TagData> tags) {
        this.imageCount = imageCount;
        this.imageUrl = imageUrl;
        this.tags = tags;
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
