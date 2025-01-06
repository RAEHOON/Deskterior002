package com.example.desk0018.Users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.desk0018.MainActivity;
import com.example.desk0018.Mainpage;
import com.example.desk0018.R;
import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.LoginRequest;
import com.example.desk0018.Server.LoginResponse;
import com.example.desk0018.Server.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    ImageButton img_goback2;
    Button btn_newmember;
    Button btn_lost;
    Button btn_Login;
    EditText et_UserId;
    EditText et_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate: 시작");

        img_goback2 = findViewById(R.id.img_goback2);
        btn_newmember = findViewById(R.id.btn_newmember);
        btn_lost = findViewById(R.id.btn_lostpassword);
        btn_Login = findViewById(R.id.btn_login);
        et_UserId = findViewById(R.id.et_email);
        et_Password = findViewById(R.id.et_password);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("로그인정보", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("로그인됌", false);

        Log.d(TAG, "onCreate: 로그인 상태 확인 - 로그인됨? " + isLoggedIn);

        if (isLoggedIn) {
            Log.d(TAG, "onCreate: 이미 로그인된 상태 - Mainpage로 이동");
            startActivity(new Intent(Login.this, Mainpage.class));
            finish();
        }

        btn_Login.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: 로그인 버튼 클릭됨");
            loginUser();
        });

        btn_lost.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: 비밀번호 찾기 버튼 클릭됨");
            startActivity(new Intent(Login.this, Lostpassword.class));
        });

        btn_newmember.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: 회원가입 버튼 클릭됨");
            startActivity(new Intent(Login.this, Getmember.class));
        });

        img_goback2.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: 뒤로가기 버튼 클릭됨");
            startActivity(new Intent(Login.this, MainActivity.class));
        });
    }

    private void loginUser() {
        String userId = et_UserId.getText().toString().trim();
        String password = et_Password.getText().toString().trim();

        Log.d(TAG, "loginUser: 입력된 ID = " + userId);
        Log.d(TAG, "loginUser: 입력된 비밀번호 = " + password);

        if (userId.isEmpty() || password.isEmpty()) {
            Log.d(TAG, "loginUser: ID 또는 비밀번호가 비어있음");
            Toast.makeText(Login.this, "사용자 ID와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.loginUser(new LoginRequest(userId, password));

        Log.d(TAG, "loginUser: 서버에 로그인 요청 시작");

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d(TAG, "onResponse: 서버 응답 도착");

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponse: 응답 성공 - isSuccess() = " + response.body().isSuccess());

                    if (response.body().isSuccess()) {
                        String nickname = response.body().getNickname();
                        Log.d(TAG, "onResponse: 로그인 성공 - 닉네임 = " + nickname);

                        SharedPreferences sharedPreferences = getSharedPreferences("로그인정보", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("로그인됌", true);
                        editor.putString("user_id", userId);
                        editor.putString("nickname", nickname);
                        editor.apply();

                        startActivity(new Intent(Login.this, Mainpage.class));
                        finish();
                    } else {
                        Log.d(TAG, "onResponse: 로그인 실패 - 메시지: " + response.body().getMessage());
                        Toast.makeText(Login.this, "로그인 실패: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "onResponse: 서버 응답 실패");
                    Toast.makeText(Login.this, "로그인 실패: 서버 응답 오류", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: 서버 요청 실패", t);
                Toast.makeText(Login.this, "로그인 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
