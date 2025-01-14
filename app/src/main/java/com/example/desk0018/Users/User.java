package com.example.desk0018.Users;

public class User {
    private String nickname;  // 닉네임
    private String profileImage; // 프로필 이미지 URL

    public User(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public String getNickname() {
        return nickname;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
