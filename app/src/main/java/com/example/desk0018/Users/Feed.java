package com.example.desk0018.Users;

import com.example.desk0018.Tag.ImageData;
import com.example.desk0018.Tag.TagData;

import java.util.List;

public class Feed {
    private String nickname; // 닉네임
    private List<ImageData> imageList; // 이미지 URL 및 태그 리스트
    private int likeCount; // 좋아요 수
    private String caption; // 피드 내용
    private int commentCount; // 댓글 수
    private String profileImage; // 프로필 이미지 URL
    private int feedCount; // 피드 고유 ID
    private boolean isLiked; // 좋아요 상태 추가

    // 생성자
    public Feed(String nickname, List<ImageData> imageList, String caption, int likeCount, int commentCount, String profileImage, int feedCount, boolean isLiked) {
        this.nickname = nickname;
        this.imageList = imageList;
        this.caption = caption;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.profileImage = profileImage;
        this.feedCount = feedCount;
        this.isLiked = isLiked; // 좋아요 상태 초기화
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

    public boolean isLiked() {
        return isLiked; // 현재 좋아요 상태 반환
    }

    // Setter 메서드
    public void setCaption(String caption) {
        this.caption = caption; // feed 대신 caption 수정
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount; // 좋아요 수 수정
    }

    public void setLiked(boolean liked) {
        isLiked = liked; // 좋아요 상태 수정
    }

    public boolean containsTag(String tag) {
        if (imageList != null) {
            for (ImageData imageData : imageList) {
                // ImageData의 tags 리스트에서 태그 검사
                List<TagData> tags = imageData.getTags();
                if (tags != null) {
                    for (TagData tagData : tags) {
                        if (tag.equals(tagData.getName())) { // 태그 이름 비교
                            return true; // 태그가 포함되어 있으면 true 반환
                        }
                    }
                }
            }
        }
        return false; // 태그가 없으면 false 반환
    }
}