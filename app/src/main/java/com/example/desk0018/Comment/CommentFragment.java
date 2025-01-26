package com.example.desk0018.Comment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.desk0018.Adapter.CommentAdapter;
import com.example.desk0018.R;
import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.CommentResponse;
import com.example.desk0018.Server.RetrofitClient;
import com.example.desk0018.Server.ServerResponse;
import com.example.desk0018.Server.UserProfileResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends Fragment {
    private static final String TAG = "CommentFragment";

    private RecyclerView recyclerView;
    private EditText editTextComment;
    private ImageView btnSendComment, imageViewUserProfile;
    private List<Comment> commentList;
    private CommentAdapter adapter;
    private int feedCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: 시작됨");
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_comments);
        editTextComment = view.findViewById(R.id.edittext_comment);
        btnSendComment = view.findViewById(R.id.btn_send_comment);
        imageViewUserProfile = view.findViewById(R.id.imageView_user_profile);

        Log.d(TAG, "onCreateView: UI 초기화 완료");

        commentList = new ArrayList<>();
        adapter = new CommentAdapter(getContext(), commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "onCreateView: RecyclerView 설정 완료");

        if (getArguments() != null) {
            feedCount = getArguments().getInt("feed_count", -1);
            Log.d(TAG, "onCreateView: 전달된 feed_count = " + feedCount);
            if (feedCount != -1) {
                loadComments(feedCount);
            }
        }

        loadUserProfile();

        btnSendComment.setOnClickListener(v -> {
            Log.d(TAG, "onCreateView: 댓글 전송 버튼 클릭됨");
            addNewComment();
        });

        return view;
    }

    private void loadUserProfile() {
        Log.d(TAG, "loadUserProfile: 시작됨");
        SharedPreferences prefs = getContext().getSharedPreferences("로그인정보", Context.MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);

        if (userId == null) {
            Log.e(TAG, "loadUserProfile: 로그인되지 않음");
            return;
        }

        Log.d(TAG, "loadUserProfile: 사용자 ID = " + userId);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<UserProfileResponse> call = apiService.getUserProfile(userId);

        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                Log.d(TAG, "loadUserProfile: 서버 응답 수신됨");
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    String profileImageUrl = "http://43.203.234.99/" + response.body().getProfileImage();
                    Log.d(TAG, "loadUserProfile: 프로필 이미지 URL = " + profileImageUrl);
                    if (profileImageUrl != null) {
                        Glide.with(CommentFragment.this)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.loding)
                                .apply(RequestOptions.circleCropTransform())
                                .error(R.drawable.error)
                                .into(imageViewUserProfile);
                        Log.d(TAG, "loadUserProfile: 프로필 이미지 로드 성공");
                    }
                } else {
                    Log.e(TAG, "loadUserProfile: 프로필 정보를 가져오지 못함");
                    Toast.makeText(getActivity(), "프로필 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.e(TAG, "loadUserProfile: 네트워크 오류", t);
                Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComments(int feedCount) {
        Log.d("CommentFragment", "loadComments: 요청을 보냄, feedCount = " + feedCount);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<CommentResponse> call = apiService.getComments(feedCount);

        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    commentList.clear();
                    commentList.addAll(response.body().getComments());
                    adapter.notifyDataSetChanged();
                    Log.d("CommentFragment", "loadComments: 댓글 개수 = " + commentList.size());
                } else {
                    Log.e("CommentFragment", "loadComments: 서버 응답 실패. 응답: " + response.toString());
                    Toast.makeText(getActivity(), "댓글을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Log.e("CommentFragment", "loadComments: 네트워크 오류", t);
                Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewComment() {
        String commentText = editTextComment.getText().toString().trim();
        Log.d(TAG, "addNewComment: 댓글 입력됨 = " + commentText);

        if (commentText.isEmpty()) {
            Toast.makeText(getActivity(), "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "addNewComment: 댓글이 비어 있음");
            return;
        }

        SharedPreferences prefs = getContext().getSharedPreferences("로그인정보", Context.MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);

        if (userId == null) {
            Log.e(TAG, "addNewComment: 로그인 필요");
            Toast.makeText(getActivity(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "addNewComment: userId = " + userId + ", feedCount = " + feedCount);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ServerResponse> call = apiService.addComment(feedCount, userId, commentText);

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.d(TAG, "addNewComment: 서버 응답 수신됨");
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(getActivity(), "댓글이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "addNewComment: 댓글 추가 성공");
                    loadComments(feedCount);
                    editTextComment.setText("");
                } else {
                    Log.e(TAG, "addNewComment: 댓글 추가 실패");
                    Toast.makeText(getActivity(), "댓글 추가 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e(TAG, "addNewComment: 네트워크 오류", t);
                Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
