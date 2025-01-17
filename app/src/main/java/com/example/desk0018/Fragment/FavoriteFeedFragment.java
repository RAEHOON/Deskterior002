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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desk0018.Adapter.CombinedAdapter;
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

public class FavoriteFeedFragment extends Fragment {
    private static final String TAG = "FavoriteFeedFragment";
    private RecyclerView recyclerView;
    private MyFeedAdapter myFeedAdapter;
    private List<Feed> favoriteFeeds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "FavoriteFeedFragment onCreateView 호출됨");
        View view = inflater.inflate(R.layout.activity_favorite_feed_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_favorite_feed);
        favoriteFeeds = new ArrayList<>();
        Log.d(TAG, "favoriteFeeds 리스트 초기화됨");

        // MyFeedAdapter 생성 및 RecyclerView 설정
        myFeedAdapter = new MyFeedAdapter(getContext(), favoriteFeeds, this::onFeedClick);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 3열 그리드 레이아웃 설정
        recyclerView.setAdapter(myFeedAdapter);
        Log.d(TAG, "RecyclerView에 MyFeedAdapter 설정됨");

        // 로그인된 사용자 정보 가져오기
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("로그인정보", Context.MODE_PRIVATE);
        String nickname = sharedPreferences.getString("nickname", null);
        Log.d(TAG, "SharedPreferences에서 닉네임 읽기, nickname: " + nickname);

        if (nickname != null) {
            // 즐겨찾기한 피드 로드
            loadFavoriteFeeds(nickname);
        } else {
            Log.e(TAG, "nickname이 null입니다.");
        }

        return view;
    }

    private void loadFavoriteFeeds(String nickname) {
        // 서버에서 즐겨찾기 피드 로드
        Log.d(TAG, "loadFavoriteFeeds 호출됨, nickname: " + nickname);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Feed>> call = apiService.getFavoriteFeedsByNickname(nickname);

        call.enqueue(new Callback<List<Feed>>() {
            @Override
            public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    favoriteFeeds.clear();
                    favoriteFeeds.addAll(response.body());
                    myFeedAdapter.notifyDataSetChanged();
                    Log.d(TAG, "즐겨찾기 피드 로드 성공, 피드 개수: " + favoriteFeeds.size());
                } else {
                    try {
                        String error = response.errorBody().string();
                        Log.d(TAG, "즐겨찾기 피드 로드 실패: " + error);
                    } catch (IOException e) {
                        Log.e(TAG, "errorBody 읽기 실패: " + e.getMessage());
                    }
                    Toast.makeText(getContext(), "즐겨찾기 피드를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Feed>> call, Throwable t) {
                Log.e(TAG, "네트워크 오류 또는 서버 요청 실패: " + t.getMessage());
                Toast.makeText(getContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onFeedClick(Feed feed) {
        // 특정 피드 클릭 시 동작
        int position = favoriteFeeds.indexOf(feed);
        Log.d(TAG, "onFeedClick 호출됨, position: " + position);

        // FeedDetailFragment로 데이터 전달
        FeedDetailFragment fragment = new FeedDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("FEED_LIST", (Serializable) favoriteFeeds); // 전체 피드 리스트 전달
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
