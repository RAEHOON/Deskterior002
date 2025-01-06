package com.example.desk0018.Server;

public class NicknameCheckResponse {
    //닉네임채크 응답 클래스
    private boolean success;
    //채크 성공여부 확인 변수
    private String message;
    //메세지 저장 변수

    public boolean isSuccess() {
        return success;
    }
    //채크 성공시 성공 반환

    public String getMessage() {
        return message;
    }
    //메세지받으면 메세지 반환
}
