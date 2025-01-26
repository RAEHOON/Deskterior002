package com.example.desk0018.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.desk0018.Comment.Comment;
import com.example.desk0018.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private static final String TAG = "CommentAdapter";

    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
        Log.d(TAG, "CommentAdapter 생성됨. 댓글 수: " + commentList.size());
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: 새로운 뷰 홀더 생성");
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // 현재 위치의 댓글 데이터 가져오기
        Comment comment = commentList.get(position);
        Log.d(TAG, "onBindViewHolder: 댓글 바인딩 - 위치: " + position + ", 닉네임: " + comment.getNickname());

        // 닉네임, 댓글 내용, 좋아요 수 설정
        holder.textViewNickname.setText(comment.getNickname());
        holder.textViewContent.setText(comment.getContent());
        holder.textViewLikeCount.setText(String.valueOf(comment.getLikeCount()));

        Log.d(TAG, "onBindViewHolder: 닉네임=" + comment.getNickname() + ", 댓글=" + comment.getContent() + ", 좋아요=" + comment.getLikeCount());

        // Glide를 사용해 프로필 이미지 로드
        Glide.with(context)
                .load(comment.getProfileImage())
                .placeholder(R.drawable.loding)
                .apply(RequestOptions.circleCropTransform())
                .error(R.drawable.error)
                .into(holder.imageViewProfile);
        Log.d(TAG, "onBindViewHolder: 프로필 이미지 로드 - URL: " + comment.getProfileImage());

        // 좋아요 버튼 이미지 변경
        holder.imageViewLike.setImageResource(comment.isLiked() ? R.drawable.heartfill : R.drawable.heart);
        Log.d(TAG, "onBindViewHolder: 좋아요 상태 - " + (comment.isLiked() ? "좋아요됨" : "좋아요 안됨"));

        // 좋아요 버튼 클릭 이벤트
        holder.imageViewLike.setOnClickListener(v -> {
            Log.d(TAG, "onBindViewHolder: 좋아요 버튼 클릭됨 - 현재 상태: " + comment.isLiked());
            comment.setLiked(!comment.isLiked());
            if (comment.isLiked()) {
                comment.setLikeCount(comment.getLikeCount() + 1);
                Log.d(TAG, "onBindViewHolder: 좋아요 증가됨, 새로운 좋아요 수: " + comment.getLikeCount());
            } else {
                comment.setLikeCount(comment.getLikeCount() - 1);
                Log.d(TAG, "onBindViewHolder: 좋아요 감소됨, 새로운 좋아요 수: " + comment.getLikeCount());
            }
            notifyItemChanged(position);
        });

        // 답글 달기 클릭 이벤트
        holder.textViewReply.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        int size = commentList.size();
        Log.d(TAG, "getItemCount: 현재 댓글 수 = " + size);
        return size;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProfile, imageViewLike;
        TextView textViewNickname, textViewContent, textViewLikeCount, textViewReply;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "CommentViewHolder: 뷰홀더 초기화");
            imageViewProfile = itemView.findViewById(R.id.imageView_comment_profile);
            imageViewLike = itemView.findViewById(R.id.imageView_comment_like);
            textViewNickname = itemView.findViewById(R.id.textView_comment_nickname);
            textViewContent = itemView.findViewById(R.id.textView_comment_content);
            textViewLikeCount = itemView.findViewById(R.id.textView_comment_like_count);
            textViewReply = itemView.findViewById(R.id.textView_comment_reply);
        }
    }
}
