package com.example.desk0018.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desk0018.Adapter.TagListAdapter;
import com.example.desk0018.R;
import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.RetrofitClient;
import com.example.desk0018.Tag.TagData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TagListDialog extends Dialog {
    private static final String TAG = "TagListDialog";
    private RecyclerView recyclerView;
    private TagListAdapter adapter;
    private Context context;
    private List<TagData> tagList;
    private int feedCount;
    private int imageCount;

    public TagListDialog(@NonNull Context context, int feedCount, int imageCount) {
        super(context);
        Log.d(TAG, "TagListDialog 생성자: feedCount=" + feedCount + ", imageCount=" + imageCount);
        this.context = context;
        this.feedCount = feedCount;
        this.imageCount = imageCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: 다이얼로그 생성 시작");
        setContentView(R.layout.activity_tag_list_dialog);

        recyclerView = findViewById(R.id.recyclerview_tags);
        Log.d(TAG, "onCreate: RecyclerView 초기화 완료");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d(TAG, "onCreate: LayoutManager 설정 완료");

        if (getWindow() != null) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            //다이어로그 레이아웃 속성 가져오기
            params.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
            //화면 너비 전체의 90프로로 설정하기
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            // 화면 높이를 크기에 맞게 조절해주기
            getWindow().setAttributes(params);
            //다이어로그 창에 위 설정한거 적용하기
            Log.d(TAG, "onCreate: 다이얼로그 너비 및 높이 설정 완료");
        }

        // 데이터 불러오기
        loadTagData();
    }

    private void loadTagData() {
        Log.d(TAG, "loadTagData: 로드태그데이터 시작");

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Log.d(TAG, "loadTagData: 레트로핏으로 ApiService 객체 생성하기");

        Call<List<TagData>> call = apiService.getTags(feedCount, imageCount);
        Log.d(TAG, "loadTagData: 리스트 태그데이타로 call해서 카운트들 불러오기");

        call.enqueue(new Callback<List<TagData>>() {
            @Override
            public void onResponse(Call<List<TagData>> call, Response<List<TagData>> response) {
                Log.d(TAG, "onResponse: 서버에서 응답을 받았고, ");

                if (response.isSuccessful() && response.body() != null) {

                    tagList = response.body();
                    Log.d(TAG, "서버에서 받은거로 tagList 설정하기" + tagList.size());

                    for (int i = 0; i < tagList.size(); i++) {
                        TagData tag = tagList.get(i);
                        Log.d(TAG, "onResponse :  name=" + tag.getName() + ", url=" + tag.getUrl());
                    }
                    // 어댑터 설정
                    adapter = new TagListAdapter(tagList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    Log.d(TAG, "어댑터 설정 완료 및 데이터 표시");


                } else {
                    Log.e(TAG, "onResponse: 서버 응답 실패 - " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<TagData>> call, Throwable t) {
                Log.e(TAG, "onFailure: 네트워크 오류 발생 - " + t.getMessage());
            }
        });
    }
}
