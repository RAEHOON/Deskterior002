package com.example.desk0018.Tag;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.desk0018.Adapter.CombinedAdapter;
import com.example.desk0018.R;
import com.example.desk0018.Users.Feed;

public class TagOptionDialog extends Dialog {

    private Button btnAddTag;
    private Button btnEditTag;
    private Button btnDeleteTag;

    public TagOptionDialog(Context context, Feed feed, CombinedAdapter.FeedViewHolder feedViewHolder, OnTagActionListener listener) {
        super(context);
        setContentView(R.layout.layout_tag_options); // 다이얼로그 레이아웃 설정
        Log.d("TagOptionDialog", "TagOptionDialog 생성");

        // 버튼 연결
        btnAddTag = findViewById(R.id.btn_add_tag);
        btnEditTag = findViewById(R.id.btn_edit_tag);
        btnDeleteTag = findViewById(R.id.btn_delete_tag);

        // 태그 추가 버튼 클릭 이벤트
        btnAddTag.setOnClickListener(view -> {
            dismiss(); // 다이얼로그 닫기
            if (listener != null) {
                listener.onAddTag(feed, feedViewHolder);
            }
        });

        // 태그 수정 버튼 클릭 이벤트
        btnEditTag.setOnClickListener(view -> {
            dismiss(); // 다이얼로그 닫기
            if (listener != null) {
                listener.onEditTag(feed);
            }
        });

        // 태그 삭제 버튼 클릭 이벤트
        btnDeleteTag.setOnClickListener(view -> {
            dismiss(); // 다이얼로그 닫기
            if (listener != null) {
                listener.onDeleteTag(feed);
            }
        });
    }


    // TagOptionDialog 버튼 클릭 이벤트 리스너
    public interface OnTagActionListener {
        void onAddTag(Feed feed, CombinedAdapter.FeedViewHolder feedViewHolder);
        void onEditTag(Feed feed);
        void onDeleteTag(Feed feed);
    }
}
