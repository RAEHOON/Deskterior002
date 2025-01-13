package com.example.desk0018.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desk0018.Adapter.SearchAdapter;
import com.example.desk0018.R;
import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.RetrofitClient;
import com.example.desk0018.Tag.TagData;
import com.example.desk0018.Users.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private EditText edittext_search;
    private RecyclerView recyclerView_search_results;
    private SearchAdapter searchAdapter;
    private List<Object> searchResults; // 검색 결과 리스트 (닉네임과 태그 혼합)

    private static final String TAG = "SearchFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Fragment 시작");
        View view = inflater.inflate(R.layout.activity_search_fragment, container, false);

        edittext_search = view.findViewById(R.id.edittext_search);
        Button buttonSearch = view.findViewById(R.id.button_search); // 검색 버튼
        recyclerView_search_results = view.findViewById(R.id.recyclerView_search_results);

        // RecyclerView 초기화
        searchResults = new ArrayList<>();
        searchAdapter = new SearchAdapter(searchResults);
        recyclerView_search_results.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_search_results.setAdapter(searchAdapter);

        Log.d(TAG, "onCreateView: RecyclerView 초기화 완료");

        // 검색 버튼 클릭 리스너
        buttonSearch.setOnClickListener(v -> {
            String query = edittext_search.getText().toString().trim();
            Log.d(TAG, "onCreateView: 검색어 입력 = " + query);
            if (!query.isEmpty()) {
                search(query); // 검색 실행
            } else {
                Toast.makeText(getContext(), "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCreateView: 검색어가 비어있음");
            }
        });

        return view;
    }

    private void search(String query) {
        Log.d(TAG, "search: 검색 시작, Query = " + query);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // 닉네임 검색
        Call<List<User>> nicknameCall = apiService.searchByNickname(query);
        Log.d(TAG, "search: 닉네임 검색 API 호출 준비 완료");
        nicknameCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d(TAG, "search: 닉네임 검색 응답 수신");
                if (response.isSuccessful() && response.body() != null) {
                    List<User> nicknameResults = response.body();
                    Log.d(TAG, "search: 닉네임 검색 성공, 결과 수 = " + nicknameResults.size());

                    searchResults.clear();
                    searchResults.addAll(nicknameResults); // 닉네임 결과 추가
                    searchAdapter.notifyDataSetChanged();
                    Log.d(TAG, "search: 닉네임 결과 RecyclerView 업데이트 완료");

                    // 태그 검색
                    searchTags(query);
                } else {
                    Log.e(TAG, "search: 닉네임 검색 실패, 응답 코드 = " + response.code());
                    Log.e(TAG, "search: 닉네임 검색 실패, 에러 메시지 = " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG, "search: 닉네임 검색 API 호출 실패, 에러 메시지 = " + t.getMessage());
            }
        });
    }

    private void searchTags(String query) {
        Log.d(TAG, "searchTags: 태그 검색 시작, Query = " + query);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<TagData>> tagCall = apiService.searchByTagName(query);
        Log.d(TAG, "searchTags: 태그 검색 API 호출 준비 완료");
        tagCall.enqueue(new Callback<List<TagData>>() {
            @Override
            public void onResponse(Call<List<TagData>> call, Response<List<TagData>> response) {
                Log.d(TAG, "searchTags: 태그 검색 응답 수신");
                if (response.isSuccessful() && response.body() != null) {
                    List<TagData> tagResults = response.body();
                    Log.d(TAG, "searchTags: 태그 검색 성공, 결과 수 = " + tagResults.size());

                    searchResults.addAll(tagResults); // 태그 결과 추가
                    searchAdapter.notifyDataSetChanged();
                    Log.d(TAG, "searchTags: 태그 결과 RecyclerView 업데이트 완료");
                } else {
                    Log.e(TAG, "searchTags: 태그 검색 실패, 응답 코드 = " + response.code());
                    Log.e(TAG, "searchTags: 태그 검색 실패, 에러 메시지 = " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<TagData>> call, Throwable t) {
                Log.e(TAG, "searchTags: 태그 검색 API 호출 실패, 에러 메시지 = " + t.getMessage());
            }
        });
    }
}