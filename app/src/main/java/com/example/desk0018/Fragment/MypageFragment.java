package com.example.desk0018.Fragment;


import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.desk0018.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageFragment extends Fragment {
    // MyPageFragment 클래스 생성 프래그먼트 상속

    Button btnLogout;
    // 로그아웃 버튼 변수 생성
    ImageView imv_profile;
    // 프로필 이미지를 표시할 ImageView 변수 생성
    TextView tv_nickname;
    // 닉네임을 표시할 TextView 변수 생성

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //프래그먼트의 뷰 를 생성하는 메서드

        View view = inflater.inflate(R.layout.activity_mypage_fragment, container, false);
        //레이아웃을 바꿔서 뷰로 받기
        btnLogout = view.findViewById(R.id.btn_logout);
        // 레이아웃에서 로그아웃 버튼 연결
        imv_profile = view.findViewById(R.id.imv_profile);
        // 레이아웃에서 프로필 이미지를 표시할 이미지뷰연결
        tv_nickname = view.findViewById(R.id.tv_nickname);
        // 레이아웃에서 닉네임을 표시할 텍스트뷰 연결

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("로그인정보", MODE_PRIVATE);
        // 셰어드를 써서 "로그인정보"라는 이름의 파일에서 데이터 가져오기
        String userId = sharedPreferences.getString("user_id", null);
        // 셰어드에서 "user_id" 값을 가져와 userId 에 저장

        if (userId != null) {
            // userId가 null이 아닐 경우 (로그인했을경우?)
            loadUserProfile();
            //유저프로파일 데이터 호출
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            //로그아웃 버튼 클릭 이벤트 설정
            @Override
            public void onClick(View v) {
                //버튼 클릭 시 실행되는 코드

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("로그인정보", MODE_PRIVATE);
                // 셰어드를 써서 "로그인정보"라는 이름의 파일에서 데이터 가져오기
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //셰어드 에디터 객체 생성
                editor.clear();
                //에디터 초기화
                editor.apply();
                // 변경사항 저장

                Intent intent = new Intent(getActivity(), Login.class);
                //로그인으로 가는 인텐트 생성
                startActivity(intent);
                //로그인엑티비티 실행하기
                getActivity().finish();
                //현재 액티비티 종료
            }
        });

        return view;
        //View 반환
    }

    private void loadUserProfile() {
        // 프로필 데이터를 서버에서 가져오는 메서드

        SharedPreferences prefs = getContext().getSharedPreferences("로그인정보", MODE_PRIVATE);
        // 셰어드를 써서 "로그인정보"라는 이름의 파일에서 데이터 가져오기
        String userId = prefs.getString("user_id", null);
        // 셰어드에서 "user_id" 값을 가져와 userId 에 저장

        if (userId != null) {
            // userId가 null이 아닐 경우 (로그인했을경우?)
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            //레트로핏으로 apiservice 객체 생성하기
            Call<UserProfileResponse> call = apiService.getUserProfile(userId);
            //유저프로파일리스펀스로로 call 객체 만들어서 유저아이디정보 서버에서 불러옴

            call.enqueue(new Callback<UserProfileResponse>() {
                // 응답오는거를 비동기적으로 처리하기 위해 콜백 설정

                @Override
                public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                    // 서버 응답 성공 시 실행
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        //응답이 성공적이고, 응답 데이터가 null이 아닐 경우
                        String profileImageUrl = "http://43.203.234.99/" + response.body().getProfileImage();
                        // 프로필 이미지 URL 설정
                        tv_nickname.setText(response.body().getNickname());
                        //닉네임을 텍스트뷰에 설정
                        if (profileImageUrl != null) {
                            loadProfileImageWithGlide(profileImageUrl);
                            // Glide를 사용해 프로필 이미지 로드
                        }
                    } else {
                        // 응답은 성공했지만 데이터가 없거나 실패한 경우
                        Toast.makeText(getActivity(), "프로필 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                        // 사용자에게 오류 메시지 표시
                    }
                }

                @Override
                public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                    // 서버 요청 실패 시 실행되는 메서드
                    Log.e("API Error", "API 호출 실패: ", t);
                    // 에러 로그 출력
                    Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    // 사용자에게 네트워크 오류 메시지 표시
                }
            });
        }
    }

    private void loadProfileImageWithGlide(String imageUrl) {
        // Glide를 사용해 프로필 이미지를 로드하는 메서드
        Glide.with(this)
                .load(imageUrl)
                // 이미지 URL 로드
                .placeholder(R.drawable.loding)
                // 로딩 중 표시할 이미지
                .error(R.drawable.error)
                // 로드 실패 시 표시할 이미지
                .into(imv_profile);

    }

}