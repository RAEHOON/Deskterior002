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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desk0018.Adapter.CombinedAdapter;
import com.example.desk0018.R;
import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.RetrofitClient;
import com.example.desk0018.Tag.ImageData;
import com.example.desk0018.Tag.TagData;
import com.example.desk0018.Topmenu.Buttontop;
import com.example.desk0018.Users.Feed;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewCombined;
    private CombinedAdapter combinedAdapter;
    private List<Buttontop> buttontopList;
    private List<Feed> feedList;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        recyclerViewCombined = view.findViewById(R.id.recyclerview_combined);

        sharedPreferences = requireContext().getSharedPreferences("로그인정보", Context.MODE_PRIVATE);

        buttontopList = new ArrayList<>();
        buttontopList.add(new Buttontop(R.drawable.key1, "키보드"));
        buttontopList.add(new Buttontop(R.drawable.mouspadddd, "마우스, 패드"));
        buttontopList.add(new Buttontop(R.drawable.monitorrrr, "모니터"));
        buttontopList.add(new Buttontop(R.drawable.speakerrrr, "스피커"));
        buttontopList.add(new Buttontop(R.drawable.lighttttt, "조명"));
        buttontopList.add(new Buttontop(R.drawable.caseeee, "케이스"));
        buttontopList.add(new Buttontop(R.drawable.deskkkk, "테이블"));
        buttontopList.add(new Buttontop(R.drawable.multititi, "선 정리"));
        buttontopList.add(new Buttontop(R.drawable.etccccc, "기타"));

        feedList = new ArrayList<>();

        combinedAdapter = new CombinedAdapter(getContext(), buttontopList, feedList, recyclerViewCombined);
        recyclerViewCombined.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCombined.setAdapter(combinedAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewCombined.addItemDecoration(dividerItemDecoration);

        loadFeedsFromServer();

        return view;
    }

    private void loadFeedsFromServer() {
        String userId = sharedPreferences.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(getContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Feed>> call = apiService.getFeeds(userId);

        call.enqueue(new Callback<List<Feed>>() {
            @Override
            public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    feedList.clear();
                    feedList.addAll(response.body());

                    for (Feed feed : response.body()) {
                        Log.d("홈프레그먼트", "피드 내용: " + feed.getCaption());
                        Log.d("홈프레그먼트", "닉네임: " + feed.getNickname());
                        Log.d("홈프레그먼트", "좋아요 상태: " + feed.isLiked());
                        Log.d("홈프레그먼트", "좋아요 수: " + feed.getLikeCount());
                    }

                    combinedAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "피드를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("홈프레그먼트", "응답 실패: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Feed>> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("홈프레그먼트", "네트워크 오류: ", t);
            }
        });
    }
}
