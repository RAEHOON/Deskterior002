package com.example.desk0018.Server;

public class UserProfileResponse {
    //유저프로필응답 클래스
    private boolean success;
    //성공여부 반환
    private String nickname;
    // 닉네임저장 변수
    private String profile_image;
    // 프로필이미지 저장변수
    private String message;
    //메세지 저장 변수

    public boolean isSuccess() {
        return success;
    }
    // 성공시 성공반환


    public String getNickname() {
        return nickname;
    }
    // 닉네임받을시 닉네임반환

    public String getProfileImage() {
        return profile_image;
    }
    // 프로필이미지를 받을시 이미지 반환


}
