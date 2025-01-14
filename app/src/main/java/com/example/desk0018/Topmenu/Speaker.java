package com.example.desk0018.Topmenu;

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
import com.example.desk0018.Users.Feed;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Speaker extends AppCompatActivity {

    private RecyclerView recyclerViewSpeaker;
    private List<Feed> feedListSpeaker;
    private SharedPreferences sharedPreferences;
    private CombinedAdapter combinedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_speaker);

        recyclerViewSpeaker = findViewById(R.id.recyclerView_speaker);
        sharedPreferences = getSharedPreferences("로그인정보", Context.MODE_PRIVATE);
        feedListSpeaker = new ArrayList<>();

        combinedAdapter = new CombinedAdapter(this, new ArrayList<>(), feedListSpeaker, recyclerViewSpeaker);
        recyclerViewSpeaker.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSpeaker.setAdapter(combinedAdapter);

        // 구분선 추가
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerViewSpeaker.addItemDecoration(dividerItemDecoration);


        loadFeedsFromServer();


    }

    private void loadFeedsFromServer() {
        String userId = sharedPreferences.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String tagFilter = "스피커"; //

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Feed>> call = apiService.getFeedsByTag(userId, tagFilter);

        call.enqueue(new Callback<List<Feed>>() {
            @Override
            public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    feedListSpeaker.clear();
                    feedListSpeaker.addAll(response.body());
                    combinedAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Speaker.this, "피드를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("SpeakerActivity", "응답 실패: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Feed>> call, Throwable t) {
                Toast.makeText(Speaker.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("SpeakerActivity", "네트워크 오류: ", t);
            }
        });
    }

}