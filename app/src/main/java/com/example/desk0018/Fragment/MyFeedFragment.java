package com.example.desk0018.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desk0018.Adapter.CombinedAdapter;
import com.example.desk0018.R;
import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.RetrofitClient;
import com.example.desk0018.Users.Feed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFeedFragment extends Fragment {
    private static final String TAG = "MyFeedFragment";
    private RecyclerView recyclerView;
    private CombinedAdapter combinedAdapter;
    private List<Feed> myFeeds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_feed_fragment, container, false);

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recyclerView_feed);
        myFeeds = new ArrayList<>();
        combinedAdapter = new CombinedAdapter(getContext(), new ArrayList<>(), myFeeds, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(combinedAdapter);

        // 로그인된 사용자 정보 가져오기
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("로그인정보", Context.MODE_PRIVATE);
                String nickname = sharedPreferences.getString("nickname", null);

        if (nickname != null) {
            // 사용자 피드 로드
            loadUserFeeds(nickname);
        } else {
            Log.e(TAG, "nickname가 null입니다.");
        }

        return view;
    }

    private void loadUserFeeds(String nickname) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Feed>> call = apiService.getFeedsByNickname(nickname);

        call.enqueue(new Callback<List<Feed>>() {
            @Override
            public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    myFeeds.clear();
                    myFeeds.addAll(response.body());
                    combinedAdapter.notifyDataSetChanged();

                    Log.d(TAG, "사용자 피드 로드 성공");
                    for (Feed feed : myFeeds) {
                        Log.d(TAG, "피드 내용: " + feed.getCaption());
                    }
                } else {
                    try {
                        String error = response.errorBody().string(); // 에러 메시지 읽기
                        Log.e(TAG, "사용자 피드 로드 실패: " + error);
                    } catch (IOException e) {
                        Log.e(TAG, "errorBody 읽기 실패: " + e.getMessage());
                    }
                    Toast.makeText(getContext(), "사용자 피드를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Feed>> call, Throwable t) {
                Log.e(TAG, "네트워크 오류 또는 서버 요청 실패", t);
                Toast.makeText(getContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}