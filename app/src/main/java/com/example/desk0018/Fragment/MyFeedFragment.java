package com.example.desk0018.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desk0018.Adapter.MyFeedAdapter;
import com.example.desk0018.R;
import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.RetrofitClient;
import com.example.desk0018.Users.Feed;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFeedFragment extends Fragment {
    private static final String TAG = "MyFeedFragment";
    private RecyclerView recyclerView; // RecyclerView를 저장하는 변수
    private MyFeedAdapter myFeedAdapter; // 어댑터를 저장하는 변수
    private List<Feed> myFeeds; // 피드 데이터를 저장하는 리스트

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 프래그먼트의 뷰를 생성
        Log.d(TAG, "onCreateView 호출됨");
        View view = inflater.inflate(R.layout.activity_my_feed_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_feed); // RecyclerView 연결
        Log.d(TAG, "RecyclerView 연결됨");

        myFeeds = new ArrayList<>();
        Log.d(TAG, "myFeeds 리스트 초기화됨");

        // 어댑터 생성
        myFeedAdapter = new MyFeedAdapter(getContext(), myFeeds, this::onFeedClick);
        Log.d(TAG, "MyFeedAdapter 생성됨");

        // RecyclerView에 LayoutManager와 Adapter 설정
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 3열 그리드 레이아웃
        Log.d(TAG, "RecyclerView LayoutManager 설정됨");

        recyclerView.setAdapter(myFeedAdapter);
        Log.d(TAG, "RecyclerView Adapter 설정됨");

        // SharedPreferences에서 닉네임 가져오기
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("로그인정보", Context.MODE_PRIVATE);
        String nickname = sharedPreferences.getString("nickname", null);
        Log.d(TAG, "SharedPreferences에서 닉네임 읽기, nickname: " + nickname);

        if (nickname != null) {
            // 사용자 피드 로드
            loadUserFeeds(nickname);
        } else {
            Log.e(TAG, "nickname이 null입니다.");
        }

        return view;
    }

    private void loadUserFeeds(String nickname) {
        // 사용자 피드를 서버에서 로드
        Log.d(TAG, "loadUserFeeds 호출됨, nickname: " + nickname);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Feed>> call = apiService.getFeedsByNickname(nickname);

        call.enqueue(new Callback<List<Feed>>() {
            @Override
            public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    myFeeds.clear();
                    myFeeds.addAll(response.body());
                    myFeedAdapter.notifyDataSetChanged();
                    Log.d(TAG, "사용자 피드 로드 성공, 피드 개수: " + myFeeds.size());
                } else {
                    try {
                        String error = response.errorBody().string(); // 에러 메시지 읽기
                        Log.d(TAG, "사용자 피드 로드 실패: " + error);
                    } catch (IOException e) {
                        Log.d(TAG, "errorBody 읽기 실패: " + e.getMessage());
                    }
                    Toast.makeText(getContext(), "사용자 피드를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Feed>> call, Throwable t) {
                Log.d(TAG, "네트워크 오류 또는 서버 요청 실패: " + t.getMessage());
                Toast.makeText(getContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onFeedClick(Feed feed) {
        // 특정 피드 클릭 시 동작
        int position = myFeeds.indexOf(feed);
        Log.d(TAG, "onFeedClick 호출됨, position: " + position);

        // FeedDetailFragment로 데이터 전달
        FeedDetailFragment fragment = new FeedDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("FEED_LIST", (Serializable) myFeeds); // 전체 피드 리스트 전달
        bundle.putInt("START_POSITION", position); // 클릭한 위치 전달
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        Log.d(TAG, "FeedDetailFragment로 데이터 전달 및 화면 전환 완료");
    }
}