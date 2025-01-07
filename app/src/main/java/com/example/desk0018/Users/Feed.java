package com.example.desk0018.Users;

import com.example.desk0018.Tag.ImageData;

import java.util.List;

public class Feed {
    private String nickname; // 닉네임
    private List<ImageData> imageList; // 이미지 URL 및 태그 리스트
    private int likeCount; // 좋아요 수
    private String caption; // 피드 내용
    private int commentCount; // 댓글 수
    private String profileImage; // 프로필 이미지 URL
    private int feedCount; // 피드 고유 ID

    // 생성자
    public Feed(String nickname, List<ImageData> imageList, String caption,  int likeCount, int commentCount, String profileImage, int feedCount) {
        this.nickname = nickname;
        this.imageList = imageList;
        this.caption = caption;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.profileImage = profileImage;
        this.feedCount = feedCount;
    }

    // Getter 메서드
    public String getNickname() {
        return nickname;
    }

    public List<ImageData> getImageList() {
        return imageList;
    }

    public String getCaption() {
        return caption; // feed 대신 caption 반환
    }
    // Setter 메서드
    public void setCaption(String caption) {
        this.caption = caption; // feed 대신 caption 수정
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public int getFeedCount() {
        return feedCount;
    }
}
