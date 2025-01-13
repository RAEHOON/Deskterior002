package com.example.desk0018.Topmenu;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desk0018.Adapter.CombinedAdapter;
import com.example.desk0018.R;

import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.RetrofitClient;
import com.example.desk0018.Tag.ImageData;
import com.example.desk0018.Tag.TagData;
import com.example.desk0018.Users.Feed;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Keyboard extends AppCompatActivity {

    private RecyclerView recyclerViewKeyboard;
    private List<Feed> feedListkeyboard;
    private SharedPreferences sharedPreferences;
    private CombinedAdapter combinedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("KeyboardActivity", "onCreate: 액티비티 시작됨");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_keyboard);

        Log.d("KeyboardActivity", "onCreate: UI 초기화 시작");
        recyclerViewKeyboard = findViewById(R.id.recyclerView_keyboard);
        sharedPreferences = getSharedPreferences("로그인정보", Context.MODE_PRIVATE);
        feedListkeyboard = new ArrayList<>();

        Log.d("KeyboardActivity", "onCreate: Adapter 설정 시작");
        combinedAdapter = new CombinedAdapter(this, new ArrayList<>(), feedListkeyboard, recyclerViewKeyboard);
        recyclerViewKeyboard.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewKeyboard.setAdapter(combinedAdapter);

        Log.d("KeyboardActivity", "onCreate: RecyclerView 구분선 추가");
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerViewKeyboard.addItemDecoration(dividerItemDecoration);

        Log.d("KeyboardActivity", "onCreate: loadFeedsFromServer 호출");
        loadFeedsFromServer();
    }

    private void loadFeedsFromServer() {
        String userId = sharedPreferences.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Feed>> call = apiService.getFeeds(userId);

        call.enqueue(new Callback<List<Feed>>() {
            @Override
            public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    feedListkeyboard.clear();
                    feedListkeyboard.addAll(response.body());

                    for (Feed feed : response.body()) {
                        Log.d("홈프레그먼트", "피드 내용: " + feed.getCaption());
                        Log.d("홈프레그먼트", "닉네임: " + feed.getNickname());
                        Log.d("홈프레그먼트", "좋아요 상태: " + feed.isLiked());
                        Log.d("홈프레그먼트", "좋아요 수: " + feed.getLikeCount());
                    }

                    combinedAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Keyboard.this, "피드를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("홈프레그먼트", "응답 실패: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Feed>> call, Throwable t) {
                Toast.makeText(Keyboard.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("홈프레그먼트", "네트워크 오류: ", t);
            }
        });
    }
}