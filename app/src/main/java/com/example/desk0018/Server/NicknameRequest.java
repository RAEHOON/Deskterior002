package com.example.desk0018.Server;

public class NicknameRequest {
    //닉네임채크 요청 클래스
    private String nickname;
    //닉네임저장 변수

    public NicknameRequest(String nickname) {
        this.nickname = nickname;
    }
    //닉네임요청 클래스에 닉네임 초기화해주기

    public String getNickname() {
        return nickname;
    }
    // 닉네임가져오면 닉네임반환

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    // 닉네임설정하면 닉네임 설정하는 메서드
}
