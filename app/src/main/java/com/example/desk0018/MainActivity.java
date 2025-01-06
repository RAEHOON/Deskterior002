package com.example.desk0018;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.desk0018.Users.Getmember;
import com.example.desk0018.Users.Login;

public class MainActivity extends AppCompatActivity {
    //메인엑티비티 클래스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //클래스 시작
        super.onCreate(savedInstanceState);
        //온크리에이트 부모클래스에 불러오기
        EdgeToEdge.enable(this);
        //에지투에지 활성화시키기
        setContentView(R.layout.activity_main);
        //레이아웃 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            //뷰컴팻에 시스템바 크기 적용하는애 레이아웃 설정 네비게이션바 포함
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // 시스템바에 크기 설정
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            // 가져온 크기들로 패딩설정
            return insets;
        });

        Button btn_login = findViewById(R.id.btn_login);
        //로그인버튼 레이아웃 연결
        Button btn_newmember = findViewById(R.id.btn_newmember);
        // 회원가입버튼 연결

        btn_login.setOnClickListener(v -> {
            //로그인버튼 클릭시
            Intent intent = new Intent(MainActivity.this, Login.class);
            //로그인클래스로 인탠트
            startActivity(intent);
            //로그인클래스 시작
        });
        btn_newmember.setOnClickListener(v -> {
            //회원가입버튼 클릭시
            Intent intent = new Intent(MainActivity.this, Getmember.class);
            //겟맴버클래스로 인텐트
            startActivity(intent);
            // 겟맴버클래스 시작
        });
    }
}