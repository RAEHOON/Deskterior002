package com.example.desk0018.Fragment;

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
    // HomeFragment 클래스 생성 프래그먼트상속

    private RecyclerView recyclerViewCombined;
    // RecyclerView 변수 생성 CombinedAdapter와 연결될 리사이클러뷰
    private CombinedAdapter combinedAdapter;
    // CombinedAdapter 어댑터 변수 생성
    private List<Buttontop> buttontopList;
    //탑버튼 리스트 생성
    private List<Feed> feedList;
    // 피드 리스트 생성

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //프래그먼트의 뷰를 생성하는 메서드

        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);
        //레이아웃을 바꿔서 뷰로 받기

        recyclerViewCombined = view.findViewById(R.id.recyclerview_combined);
        //리싸이클러뷰 레이아웃 연결



        buttontopList = new ArrayList<>();
        //버튼탑 리스트 생성
        buttontopList.add(new Buttontop(R.drawable.key1, "키보드"));
        // 리스트에 "키보드" 버튼 추가
        buttontopList.add(new Buttontop(R.drawable.mouspadddd, "마우스, 패드"));
        // 리스트에 "마우스, 패드" 버튼 추가
        buttontopList.add(new Buttontop(R.drawable.monitorrrr, "모니터"));
        // 리스트에 "모니터" 버튼 추가
        buttontopList.add(new Buttontop(R.drawable.speakerrrr, "스피커"));
        // 리스트에 "스피커" 버튼 추가
        buttontopList.add(new Buttontop(R.drawable.lighttttt, "조명"));
        // 리스트에 "조명" 버튼 추가
        buttontopList.add(new Buttontop(R.drawable.caseeee, "케이스"));
        // 리스트에 "케이스" 버튼 추가
        buttontopList.add(new Buttontop(R.drawable.deskkkk, "테이블"));
        // 리스트에 "테이블" 버튼 추가
        buttontopList.add(new Buttontop(R.drawable.multititi, "선 정리"));
        // 리스트에 "선 정리" 버튼 추가


        feedList = new ArrayList<>();
        // 피드 리스트 초기화


        combinedAdapter = new CombinedAdapter(getContext(), buttontopList, feedList, recyclerViewCombined);
        // 컴바인어뎁터 객체 생성 버튼탑 리스트와 피드 리스트 연결
        recyclerViewCombined.setLayoutManager(new LinearLayoutManager(getContext()));
        // 리사이클러뷰에 레이아웃 매니저 설정

        recyclerViewCombined.setAdapter(combinedAdapter);
        // 리사이클러뷰에 어댑터 설정


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        //경계선 설정해주는거 객체생성해서 컨텍스트 넣어줌. 세로방향
        recyclerViewCombined.addItemDecoration(dividerItemDecoration);
        // 리사이클러뷰에 경계선 적용


        loadFeedsFromServer();
        // 피드 데이터를 서버에서 불러오는 메서드 호출

        return view;
        // 뷰 반환
    }


    private void loadFeedsFromServer() {
        // 서버로부터 피드 데이터를 가져오는 메서드
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        // 레트로핏으로 ApiService 객체 생성하기

        Call<List<Feed>> call = apiService.getFeeds();
        // 리스트 피드로 call 객체 만들어서 피드 데이터를 서버에서 불러옴

        call.enqueue(new Callback<List<Feed>>() {
            @Override
            public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 응답이 성공적이고, 응답 데이터가 null이 아닐 경우
                    feedList.clear();
                    feedList.addAll(response.body());
                    // 응답받은 피드 데이터를 리스트에 추가

                    for (int i = 0; i < response.body().size(); i++) {
                        // 응답받은 데이터 확인용 로그 출력
                        Feed feed = feedList.get(i);

                        Log.d("홈프레그먼트", "피드 내용: " + feed.getFeed());
                        Log.d("홈프레그먼트", "닉네임: " + feed.getNickname());
                        Log.d("홈프레그먼트", "프로필 이미지: " + feed.getProfileImage());
                        Log.d("홈프레그먼트", "댓글 수: " + feed.getCommentCount());
                        Log.d("홈프레그먼트", "피드 카운트: " + feed.getFeedCount());
                        Log.d("홈프레그먼트", "좋아요 수: " + feed.getLikeCount());

                        // 이미지 리스트 확인
                        List<ImageData> imageList = feed.getImageList();
                        if (imageList != null && !imageList.isEmpty()) {
                            for (int j = 0; j < imageList.size(); j++) {
                                ImageData image = imageList.get(j);
                                Log.d("홈프레그먼트", "이미지 URL: " + image.getImageUrl());

                                // 이미지에 연결된 태그 리스트 확인
                                List<TagData> tagList = image.getTags();
                                if (tagList != null && !tagList.isEmpty()) {
                                    for (TagData tag : tagList) {
                                        Log.d("홈프레그먼트", "태그 X: " + tag.getX() + ", 태그 Y: " + tag.getY() + ", URL: " + tag.getUrl());
                                    }
                                } else {
                                    Log.d("홈프레그먼트", "이미지에 태그가 없습니다.");
                                }
                            }
                        } else {
                            Log.d("홈프레그먼트", "이미지가 없습니다.");
                        }
                    }

                    combinedAdapter.notifyDataSetChanged();
                    // 어댑터에 데이터 변경을 알림
                } else {
                    Toast.makeText(getContext(), "피드를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("홈프레그먼트", "응답 실패: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Feed>> call, Throwable t) {
                // API 호출 실패 시 실행되는 메서드
                Toast.makeText(getContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("홈프레그먼트", "네트워크 오류: ", t);
            }
        });
    }

}