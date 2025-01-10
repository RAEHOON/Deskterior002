package com.example.desk0018;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.desk0018.Fragment.HomeFragment;
import com.example.desk0018.Fragment.MyPageFragment;
import com.example.desk0018.Fragment.SearchFragment;
import com.example.desk0018.Fragment.UploadFragment;
import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.RetrofitClient;
import com.example.desk0018.Server.UserProfileResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Mainpage extends AppCompatActivity {
    // Mainpage 클래스 생성

    private LinearLayout linearLayout;
    //LinearLayout 변수생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //액티비티 시작 온크리에이트
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        //레이아웃 연결

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        //네비게이션 바 연결
        bottomNav.setOnItemSelectedListener(navListener);
        //네비게이션 바에 클릭(선택)리스너 설정

        if (savedInstanceState == null) {
            //세이브된거 없으면
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            //기본 화면을 홈프레그먼트가 되게 설정.
        }

        SharedPreferences sharedPreferences = getSharedPreferences("로그인정보", MODE_PRIVATE);
        // 셰어드를 써서 "로그인정보"라는 이름의 파일에서 데이터 가져오기
        String userId = sharedPreferences.getString("user_id", null);
        // 셰어드에서 "user_id" 값을 가져와 userId 에 저장

        if (userId != null) {
            // userId가 null이 아닐 경우 (로그인했을경우?)
            loadUserProfile(userId, bottomNav);
            // 유저 프로필을 불러와 네비게이션바에 설정
        }

        linearLayout = findViewById(R.id.LinerLayouttop);
        // 위 레이아웃 연결
        linearLayout.setBackgroundColor(Color.WHITE);
        // 배경색을 흰색으로 설정
    }

    private NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        //네비게이션 바에 클릭(선택)리스너 설정
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            //선택한 프래그먼트가 없을시
            int id = item.getItemId();
            //선택한 아이템의 Id를 가져오기

            if (id == R.id.nav_home) {
                // 홈 메뉴 선택 시
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_search) {
                // 검색 메뉴 선택 시
                selectedFragment = new SearchFragment();
            } else if (id == R.id.nav_upload) {
                // 업로드 메뉴 선택 시
                selectedFragment = new UploadFragment();
            } else if (id == R.id.nav_mypage) {
                // 마이페이지 메뉴 선택 시
                selectedFragment = new MyPageFragment();
            }

            if (selectedFragment != null) {
                //선택한 프래그먼트가 있을 때
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                // 해당 프래그먼트로 넘어가기
            }

            return true;
            //트루 반환
        }
    };

    private void loadUserProfile(String userId, BottomNavigationView bottomNav) {
        // 서버에서 프로필 데이터를 불러오는 메서드, 유저아이디랑 바텀네이게이션을 섞은
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        //레트로핏으로 apiservice 객체 생성하기
        Call<UserProfileResponse> call = apiService.getUserProfile(userId);
        //유저프로파일리스펀스로로 call 객체 만들어서 유저아이디정보 서버에서 불러옴

        call.enqueue(new Callback<UserProfileResponse>() {
            //응답오는거를 비동기적으로 처리하기 위해 콜백 설정
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                // 서버 응답 성공 시 실행
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    // 응답이 성공적이고 데이터가 null이 아니며 성공 상태인 경우
                    String profileImageUrl = "http://43.203.234.99/" + response.body().getProfileImage();
                    // 프로필 이미지 URL 생성
                    loadProfileImageWithGlide(profileImageUrl, bottomNav);
                    //글라이드로 네비게이션바 버튼에 이미지 설정

                } else {
                    Toast.makeText(Mainpage.this, "프로필 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    // 프로필 정보 불러오기 실패 시 사용자 알림
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                // 서버 요청 실패 시 실행
                Log.e("API Error", "API 호출 실패: ", t);
                // 실패 로그 출력
                Toast.makeText(Mainpage.this, "API 호출 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                // 사용자에게 네트워크 오류 알림
            }
        });
    }

    private void loadProfileImageWithGlide(String profileImageUrl, BottomNavigationView bottomNav) {
        // 글라이드를 사용해 네비게이션 아이콘에 프로필 이미지를 설정하는 메서드
        bottomNav.setItemIconTintList(null);
        // 네비게이션 아이콘의 틴트 제거 (색)

        Glide.with(this)
                .asBitmap()
                // 이미지를 Bitmap으로 로드
                .apply(RequestOptions.circleCropTransform())
                .load(profileImageUrl)
                // 서버에서 받은 프로필 이미지 URL 로드
                .placeholder(R.drawable.loding)
                //로딩이미지
                .error(R.drawable.error)
                //에러이미지
                .into(new CustomTarget<Bitmap>() {
                    // 커스텀 타겟으로 비트맵 이미지를 네비게이션 아이콘으로 설정
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // 이미지 로드 성공 시 실행
                        Drawable profileDrawable = new BitmapDrawable(getResources(), resource);
                        // 로드된 비트맵을 드로우어블로 변환
                        bottomNav.getMenu().findItem(R.id.nav_mypage).setIcon(profileDrawable);
                        //마이페이지 버튼의 아이콘을 프로필 이미지로 설정
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        //글라이드가 리소스를 정리할 때 실행
                        bottomNav.getMenu().findItem(R.id.nav_mypage).setIcon(R.drawable.loding);
                        // 기본 로딩 이미지로 설정
                    }
                });
    }
}
