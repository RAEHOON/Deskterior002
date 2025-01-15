package com.example.desk0018.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.desk0018.R;

public class MiniMenuDialog extends Dialog {

    // 미니메뉴다이어로그 상속

    private Button btnShowTags;
    private Button btnEditPost;
    private Button btnDeletePost;

    public MiniMenuDialog(Context context, String loggedInNickname, String postNickname, OnMiniMenuActionListener listener) {
        super(context);

        setContentView(R.layout.activity_mini_menu_dialog);
        Log.d("MiniMenuDialog", "내부클래스 생성");

        // 버튼 연결
        btnShowTags = findViewById(R.id.btn_show_tags);
        btnEditPost = findViewById(R.id.btn_edit_post);
        btnDeletePost = findViewById(R.id.btn_delete_post);

        // 본인의 글인지 확인
        if (postNickname != null && postNickname.equals(loggedInNickname)) {
            btnEditPost.setVisibility(View.VISIBLE);
            btnDeletePost.setVisibility(View.VISIBLE);
            Log.d("MiniMenuDialog", "본인의 글입니다. 수정 및 삭제 버튼 표시");
        } else {
            btnEditPost.setVisibility(View.GONE);
            btnDeletePost.setVisibility(View.GONE);
            Log.d("MiniMenuDialog", "본인의 글이 아닙니다. 수정 및 삭제 버튼 숨김");
        }

        // 버튼 클릭 이벤트 설정
        btnShowTags.setOnClickListener(v -> {
            listener.onShowTags();
            dismiss();
        });

        btnEditPost.setOnClickListener(v -> {
            listener.onEditPost();
            dismiss();
        });

        btnDeletePost.setOnClickListener(v -> {
            listener.onDeletePost();
            dismiss();
        });
    }

    // MiniMenuDialog 버튼 클릭 이벤트 리스너
    public interface OnMiniMenuActionListener {
        void onShowTags();
        void onEditPost();
        void onDeletePost();
    }
}