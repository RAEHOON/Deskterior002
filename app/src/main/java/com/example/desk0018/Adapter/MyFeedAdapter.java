package com.example.desk0018.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.desk0018.R;
import com.example.desk0018.Users.Feed;

import java.util.List;

public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.MyFeedViewHolder> {

    private static final String TAG = "MyFeedAdapter";
    private Context context; // Context를 저장하는 변수
    private List<Feed> feedList; // 피드 데이터를 저장하는 리스트
    private OnFeedClickListener onFeedClickListener; // 클릭 이벤트 인터페이스

    public MyFeedAdapter(Context context, List<Feed> feedList, OnFeedClickListener onFeedClickListener) {
        this.context = context;
        this.feedList = feedList;
        this.onFeedClickListener = onFeedClickListener;
        Log.d(TAG, "MyFeedAdapter 생성됨");
    }

    @NonNull
    @Override
    public MyFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 각 아이템의 ViewHolder를 생성
        Log.d(TAG, "onCreateViewHolder 호출됨");
        View view = LayoutInflater.from(context).inflate(R.layout.activity_my_feed_item, parent, false);
        return new MyFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyFeedViewHolder holder, int position) {
        // 뷰홀더에 데이터를 바인딩
        Log.d(TAG, "onBindViewHolder 호출됨, position: " + position);

        Feed feed = feedList.get(position);
        Log.d(TAG, "Feed 데이터: " + feed.getCaption());



        // Glide를 사용해 이미지 로드
        String ImageUrl = "http://43.203.234.99/" + feed.getImageList().get(0).getImageUrl();
        Glide.with(context)
                .load(ImageUrl) // 첫 번째 이미지만 로드
                .placeholder(R.drawable.loding) // 로딩 중 표시할 이미지
                .error(R.drawable.error) // 로드 실패 시 표시할 이미지
                .into(holder.imageView);
        Log.d(TAG, "이미지 로드 시도, URL: " + feed.getImageList().get(0).getImageUrl());

        // 클릭 이벤트 처리
        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "피드 클릭됨, position: " + position + ", caption: " + feed.getCaption());
            if (onFeedClickListener != null) {
                onFeedClickListener.onFeedClick(feed);
            }
        });
    }

    @Override
    public int getItemCount() {
        // 피드 리스트의 크기 반환
        Log.d(TAG, "getItemCount 호출됨, 리스트 크기: " + feedList.size());
        return feedList.size();
    }

    public static class MyFeedViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView; // 피드 이미지를 표시할 ImageView

        public MyFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_feed_item);
            Log.d(TAG, "MyFeedViewHolder 생성됨");
        }
    }

    public interface OnFeedClickListener {
        void onFeedClick(Feed feed); // 클릭 이벤트를 처리할 메서드
    }

}