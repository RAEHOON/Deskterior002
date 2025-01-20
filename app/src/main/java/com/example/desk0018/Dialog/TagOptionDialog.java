package com.example.desk0018.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.desk0018.Adapter.CombinedAdapter;
import com.example.desk0018.R;
import com.example.desk0018.Users.Feed;

public class TagOptionDialog extends Dialog {

    private Button btnAddTag;
    private Button btnDeleteTag;
    private Button btnShowTags;

    public TagOptionDialog(Context context, Feed feed, CombinedAdapter.FeedViewHolder feedViewHolder, OnTagActionListener listener) {
        super(context);
        setContentView(R.layout.layout_tag_options); // 다이얼로그 레이아웃 설정
        Log.d("TagOptionDialog", "TagOptionDialog 생성");

        // 버튼 연결
        btnAddTag = findViewById(R.id.btn_add_tag);
        btnDeleteTag = findViewById(R.id.btn_delete_tag);
        btnShowTags = findViewById(R.id.btn_show_tags);

        // 로그인 정보 가져오기
        SharedPreferences sharedPreferences = context.getSharedPreferences("로그인정보", Context.MODE_PRIVATE);
        String loggedInNickname = sharedPreferences.getString("nickname", null); // 로그인된 사용자의 닉네임
        Log.d("TagOptionDialog", "로그인된 닉네임: " + loggedInNickname);

        // 게시물 작성자 확인
        String postNickname = feed.getNickname(); // 게시물 작성자 닉네임
        Log.d("TagOptionDialog", "게시물 작성자 닉네임: " + postNickname);

        if (postNickname != null && postNickname.equals(loggedInNickname)) {
            // 본인의 게시물이면 버튼 표시
            btnAddTag.setVisibility(View.VISIBLE);
            btnDeleteTag.setVisibility(View.VISIBLE);
            Log.d("TagOptionDialog", "본인의 게시물입니다. 태그 추가/삭제 버튼 표시");
        } else {
            // 본인의 게시물이 아니면 버튼 숨김
            btnAddTag.setVisibility(View.GONE);
            btnDeleteTag.setVisibility(View.GONE);
            Log.d("TagOptionDialog", "본인의 게시물이 아닙니다. 태그 추가/삭제 버튼 숨김");
        }

        // 태그 추가 버튼 클릭 이벤트
        btnAddTag.setOnClickListener(view -> {
            dismiss(); // 다이얼로그 닫기
            if (listener != null) {
                listener.onAddTag(feed, feedViewHolder);
            }
        });

        // 태그 삭제 버튼 클릭 이벤트
        btnDeleteTag.setOnClickListener(view -> {
            dismiss(); // 다이얼로그 닫기
            if (listener != null) {
                listener.onDeleteTag(feed);
            }
        });

        // 버튼 클릭 이벤트 설정
        btnShowTags.setOnClickListener(v -> {
            listener.onShowTags();
            dismiss();
        });

    }


    // TagOptionDialog 버튼 클릭 이벤트 리스너
    public interface OnTagActionListener {
        void onAddTag(Feed feed, CombinedAdapter.FeedViewHolder feedViewHolder);
        void onDeleteTag(Feed feed);
        void onShowTags();
    }
}
