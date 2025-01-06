package com.example.desk0018.Server;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    //로그인요청 클래스

    @SerializedName("user_id") // JSON변환, 서버요구값하고 같이
    private String userId;
    private String password;


    public LoginRequest(String userId, String password) {

        this.userId = userId;

        this.password = password;

    }
    public String getuserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }



}

