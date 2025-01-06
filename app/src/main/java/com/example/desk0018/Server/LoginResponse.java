package com.example.desk0018.Server;

public class LoginResponse {
    //로그인응답 클래스
    private boolean success;
    //로그인 성공 실패 확인하는 변수
    private String message;
    // 메세지 저장 변수
    private String nickname;
    // 닉네임 저장 변수


    public boolean isSuccess() {
        return success;
    }
    // 성공했을시 성공 반환
    public String getMessage() {
        return message;
    }
    // 겟메세지 받아오면 메세지 반환
    public String getNickname(){ return nickname;}

    //겟닉네임 받아오면 닉네임 반환


}
