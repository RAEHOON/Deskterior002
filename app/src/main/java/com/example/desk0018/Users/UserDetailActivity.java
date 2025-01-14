package com.example.desk0018.Users;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.desk0018.Adapter.CombinedAdapter;
import com.example.desk0018.R;
import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class UserDetailActivity extends AppCompatActivity {

    private static final String TAG = "UserDetailActivity";
    private ImageView imvProfile;
    private TextView tvNickname;
    private RecyclerView recyclerViewFeed;
    private CombinedAdapter combinedAdapter;
    private List<Feed> userFeeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        Log.d(TAG, "onCreate: Activity 시작");

        // View 초기화
        imvProfile = findViewById(R.id.imageView_profile);
        tvNickname = findViewById(R.id.textView_nickname);
        recyclerViewFeed = findViewById(R.id.recyclerView_feed);
        Log.d(TAG, "onCreate: View 초기화 완료");

        // RecyclerView 초기화
        userFeeds = new ArrayList<>();
        combinedAdapter = new CombinedAdapter(this, new ArrayList<>(), userFeeds, recyclerViewFeed);
        recyclerViewFeed.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFeed.setAdapter(combinedAdapter);
        Log.d(TAG, "onCreate: RecyclerView 초기화 완료");

        // 인텐트로 전달받은 데이터 설정
        String nickname = getIntent().getStringExtra("nickname");
        String profileImage = "http://43.203.234.99/"+ getIntent().getStringExtra("profileImage");

        Log.d(TAG, "onCreate: 전달받은 데이터 - 닉네임: " + nickname + ", 프로필 이미지 URL: " + profileImage);

        tvNickname.setText(nickname);
        if (profileImage != null && !profileImage.isEmpty()) {
            Log.d(TAG, "onCreate: Glide로 프로필 이미지 로드 시작");
            Glide.with(this)
                    .load(profileImage)
                    .placeholder(R.drawable.loding)
                    .apply(RequestOptions.circleCropTransform())
                    .error(R.drawable.error)
                    .into(imvProfile);
            Log.d(TAG, "onCreate: Glide로 프로필 이미지 로드 완료");
        } else {
            Log.e(TAG, "onCreate: 프로필 이미지 URL이 비어있음");
        }

        // 해당 사용자의 피드 로드
        loadUserFeeds(nickname);
    }

    private void loadUserFeeds(String nickname) {
        Log.d(TAG, "loadUserFeeds: 사용자 피드 로드 시작 - 닉네임: " + nickname);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Feed>> call = apiService.getFeedsByNickname(nickname);

        Log.d(TAG, "loadUserFeeds: API 호출 준비 완료");

        call.enqueue(new Callback<List<Feed>>() {
            @Override
            public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {
                Log.d(TAG, "onResponse: API 응답 수신");

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponse: 응답 성공 - 피드 수: " + response.body().size());
                    userFeeds.clear();
                    userFeeds.addAll(response.body());
                    combinedAdapter.notifyDataSetChanged();
                    Log.d(TAG, "onResponse: RecyclerView 업데이트 완료");
                } else {
                    Log.e(TAG, "onResponse: 응답 실패 - 코드: " + response.code());
                    Toast.makeText(UserDetailActivity.this, "피드를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Feed>> call, Throwable t) {
                Log.e(TAG, "onFailure: 네트워크 오류", t);
                Toast.makeText(UserDetailActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}