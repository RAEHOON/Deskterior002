package com.example.desk0018.Server;

public class ServerResponse {
    //서버 응답 클래스
    private boolean success;
    //성공여부 반환
    private boolean liked;
    private String message;
    //메세지 저장 변수
    public boolean isLiked(){ return liked;}

    public boolean isSuccess() {
        return success;
    }
    // 성공시 성공반환

    public String getMessage() {
        return message;
    }
    //메세지 오면 메세지 반환
}