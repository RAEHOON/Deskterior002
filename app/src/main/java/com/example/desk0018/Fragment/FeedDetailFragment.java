package com.example.desk0018.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desk0018.Adapter.CombinedAdapter;
import com.example.desk0018.Adapter.MyFeedAdapter;
import com.example.desk0018.R;
import com.example.desk0018.Users.Feed;

import java.util.ArrayList;
import java.util.List;

public class FeedDetailFragment extends Fragment {
    private static final String TAG = "FeedDetailFragment";
    private RecyclerView recyclerView;
    private CombinedAdapter combinedAdapter;
    private List<Feed> feedList;
    private int startPosition; // 시작 위치를 저장하는 변수

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "FeedDetailFragment onCreateView 호출됨");
        View view = inflater.inflate(R.layout.activity_feed_detail, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_feed_detail); // RecyclerView 연결
        feedList = new ArrayList<>(); // 피드 리스트 초기화
        Log.d(TAG, "feedList 초기화됨");

        // CombinedAdapter를 RecyclerView에 연결
        combinedAdapter = new CombinedAdapter(getContext(), new ArrayList<>(), feedList, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // 세로 레이아웃 설정
        recyclerView.setAdapter(combinedAdapter);
        Log.d(TAG, "RecyclerView에 CombinedAdapter 설정됨");

        // 전달받은 데이터 처리
        if (getArguments() != null) {
            feedList = (List<Feed>) getArguments().getSerializable("FEED_LIST"); // 피드 리스트 전달받기
            startPosition = getArguments().getInt("START_POSITION", 0); // 시작 위치 전달받기
            Log.d(TAG, "getArguments()로 feedList와 startPosition 가져옴, startPosition: " + startPosition);
        } else {
            Log.d(TAG, "getArguments()가 null입니다.");
        }

        // 데이터 설정
        if (feedList != null && !feedList.isEmpty()) {
            Log.d(TAG, "feedList 크기: " + feedList.size());
            combinedAdapter.updateFeedList(feedList); // 어댑터에 데이터 업데이트

            // RecyclerView를 클릭한 위치로 스크롤
            recyclerView.post(() -> {
                recyclerView.scrollToPosition(startPosition);
                Log.d(TAG, "RecyclerView를 startPosition으로 스크롤, startPosition: " + startPosition);
            });
        } else {
            Log.d(TAG, "feedList가 비어있습니다.");
        }

        return view;
    }
}
