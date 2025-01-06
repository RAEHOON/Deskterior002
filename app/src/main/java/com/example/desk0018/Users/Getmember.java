package com.example.desk0018.Users;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.desk0018.MainActivity;
import com.example.desk0018.R;
import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.NicknameCheckResponse;
import com.example.desk0018.Server.NicknameRequest;
import com.example.desk0018.Server.RegisterResponse;
import com.example.desk0018.Server.RetrofitClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Getmember extends AppCompatActivity {

    EditText etEmail, etNickname, etPassword, etPasswordCheck;
    Button btnRegister, btnNicknameCheck, btnProfileImage;
    ImageView ivProfileImg;
    Uri profileImageUri;
    static final int REQUEST_IMAGE_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_getmember);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEmail = findViewById(R.id.et_email);
        etNickname = findViewById(R.id.et_nickname);
        etPassword = findViewById(R.id.et_password);
        etPasswordCheck = findViewById(R.id.et_passwordcheck);
        btnRegister = findViewById(R.id.btn_register);
        btnNicknameCheck = findViewById(R.id.btn_nicknamecheck);
        btnProfileImage = findViewById(R.id.btn_pfimage);
        ivProfileImg = findViewById(R.id.iv_profileimg);

        btnProfileImage.setOnClickListener(view -> pickImageFromGallery());

        ImageButton img_goback = findViewById(R.id.img_goback);
        img_goback.setOnClickListener(v -> {
            Intent intent = new Intent(Getmember.this, MainActivity.class);
            startActivity(intent);
        });

        btnRegister.setOnClickListener(view -> registerUser());

        btnNicknameCheck.setOnClickListener(view -> checkNickname());
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
        Log.d("getmember", "픽이미지프럼갤러리");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            profileImageUri = data.getData();
            ivProfileImg.setImageURI(profileImageUri);
        }
        Log.d("getmember", "프로필이미지설정하기");
    }

    private void registerUser() {
        String userId = etEmail.getText().toString().trim();
        String nickname = etNickname.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordCheck = etPasswordCheck.getText().toString().trim();

        if (!password.equals(passwordCheck)) {
            Toast.makeText(Getmember.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            Log.d("getmember", "비밀번호 일치하지 않음");
            return;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(Getmember.this, "비밀번호는 10자 이상, 영어 소문자, 숫자, 특수문자를 포함해야 합니다.", Toast.LENGTH_SHORT).show();
            Log.d("getmember", "비밀번호 구성 확인하기");
            return;
        }

        if (profileImageUri == null) {
            Toast.makeText(Getmember.this, "프로필 이미지를 선택해주세요.", Toast.LENGTH_SHORT).show();
            Log.d("getmember", "프로필이미지 선택하기");
            return;
        }

        try {
            File profileImageFile = createImageFile(userId, profileImageUri);
            uploadUserProfile(userId, nickname, password, profileImageFile);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "이미지 파일을 처리하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            Log.d("getmember", "이미지파일 처리할때 ㅗ류");
        }
        Log.d("getmember", "레지스터유저부분");
    }

    private File createImageFile(String userId, Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);

        File userDir = new File(getCacheDir(), "uploads/" + userId);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
        Log.d("크리에이트이미지파일", "업로드부분에 아이디이름 폴더만들기");

        String fileName = userId + "_profile.jpg";
        File profileImageFile = new File(userDir, fileName);

        FileOutputStream outputStream = new FileOutputStream(profileImageFile);
        Log.d("크리에이트이미지파일", "업로드부분에 아이디이름 파일만들기");


        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.close();
        inputStream.close();
        Log.d("크리에이트이미지파일", "프로필이미지파일 마무리부분");
        return profileImageFile;

    }

    private void uploadUserProfile(String userId, String nickname, String password, File profileImageFile) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        RequestBody emailPart = RequestBody.create(okhttp3.MediaType.parse("text/plain"), userId);
        RequestBody nicknamePart = RequestBody.create(okhttp3.MediaType.parse("text/plain"), nickname);
        RequestBody passwordPart = RequestBody.create(okhttp3.MediaType.parse("text/plain"), password);
        RequestBody requestFile = RequestBody.create(okhttp3.MediaType.parse("image/*"), profileImageFile);
        Log.d("업로드유저프로파일", "레스큐바디 파드들 나누기");
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_image", profileImageFile.getName(), requestFile);
        Log.d("업로드유저프로파일", "멀피파트에서 프로필이미지 부분");
        Call<RegisterResponse> call = apiService.registerUserWithImage(emailPart, nicknamePart, passwordPart, body);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isSuccess()) {
                            Toast.makeText(Getmember.this, "회원가입 성공! 로그인 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                            Log.d("getmember", "회원가입 성공 로그인페이지로 이동");
                            Intent intent = new Intent(Getmember.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Getmember.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("API Error", "서버 응답 오류: " + response.errorBody().string());
                        Toast.makeText(Getmember.this, "회원가입 실패: 서버 응답 오류", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("JSON Parsing Error", "JSON 파싱 오류: ", e);
                    Toast.makeText(Getmember.this, "서버 응답을 처리하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("API Failure", "API 호출 실패: ", t);
                Toast.makeText(Getmember.this, "회원가입 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkNickname() {
        String nickname = etNickname.getText().toString().trim();

        if (nickname.isEmpty()) {
            Toast.makeText(Getmember.this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        NicknameRequest request = new NicknameRequest(nickname);
        Call<NicknameCheckResponse> call = apiService.checkNickname(request);

        call.enqueue(new Callback<NicknameCheckResponse>() {
            @Override
            public void onResponse(Call<NicknameCheckResponse> call, Response<NicknameCheckResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(Getmember.this, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Getmember.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Getmember.this, "닉네임 확인 실패: 서버 응답 오류", Toast.LENGTH_SHORT).show();
                    Log.d("getmember", "닉네임 확인 실패 서버응답 오류"+ response );
                }
            }

            @Override
            public void onFailure(Call<NicknameCheckResponse> call, Throwable t) {
                Toast.makeText(Getmember.this, "닉네임 확인 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("getmember", "닉네임 확인 실패"+ t.getMessage());
            }
        });
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 10) return false;
        if (!password.matches(".*[a-z].*")) return false;
        if (!password.matches(".*[0-9].*")) return false;
        if (!password.matches(".*[!@#$%^&*()_+].*")) return false;
        return true;
    }
}
