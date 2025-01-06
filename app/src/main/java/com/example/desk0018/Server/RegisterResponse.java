package com.example.desk0018.Server;

public class RegisterResponse {
    //회원가입 응답 클래스
    private boolean success;
    //성공여부 반환
    private String message;
    //메세지 저장 변수


    public boolean isSuccess() {
        return success;
    }
    // 성공시 성공반환
    public String getMessage() {
        return message;
    }
    // 메세지 오며 ㄴ메세지 반환


}
