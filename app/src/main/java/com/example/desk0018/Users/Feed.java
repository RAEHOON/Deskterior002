package com.example.desk0018.Users;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.desk0018.Tag.ImageData;


import java.util.List;

public class Feed implements Parcelable {
    private String nickname;
    private List<ImageData> imageList;
    private int likeCount;
    private String caption;
    private int commentCount;
    private String profileImage;
    private int feedCount;
    private int commentEa;
    private boolean isLiked;
    private boolean isFavorited;

    // 생성자
    public Feed(String nickname, List<ImageData> imageList, String caption, int likeCount, int commentCount, int commentEa, String profileImage, int feedCount, boolean isLiked, boolean isFavorited) {
        this.nickname = nickname;
        this.imageList = imageList;
        this.caption = caption;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.profileImage = profileImage;
        this.feedCount = feedCount;
        this.isLiked = isLiked;
        this.isFavorited = isFavorited;
        this.commentEa = commentEa;
    }

    // Parcelable을 위한 생성자 (Parcel에서 읽어오기)
    protected Feed(Parcel in) {
        nickname = in.readString();
        imageList = in.createTypedArrayList(ImageData.CREATOR);  // ImageData도 Parcelable 구현 필요
        likeCount = in.readInt();
        caption = in.readString();
        commentCount = in.readInt();
        commentEa = in.readInt();
        profileImage = in.readString();
        feedCount = in.readInt();
        isLiked = in.readByte() != 0;  // boolean을 byte로 저장
        isFavorited = in.readByte() != 0;
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickname);
        dest.writeTypedList(imageList);
        dest.writeInt(likeCount);
        dest.writeString(caption);
        dest.writeInt(commentCount);
        dest.writeInt(commentEa);
        dest.writeString(profileImage);
        dest.writeInt(feedCount);
        dest.writeByte((byte) (isLiked ? 1 : 0));
        dest.writeByte((byte) (isFavorited ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getter 메서드
    public String getNickname() {
        return nickname;
    }

    public List<ImageData> getImageList() {
        return imageList;
    }

    public String getCaption() {
        return caption;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }
    public int getCommentEa() {
        return commentEa;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public int getFeedCount() {
        return feedCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    // Setter 메서드
    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    public void setCommentEa(int commentEa) {
        this.commentEa = commentEa;
    }
}