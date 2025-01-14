package com.example.desk0018.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.desk0018.R;
import com.example.desk0018.Users.Feed;

import java.util.ArrayList;
import java.util.List;

public class UserDetailAdapter extends RecyclerView.Adapter<UserDetailAdapter.FeedViewHolder> {

    private static final String TAG = "UserDetailAdapter";
    private Context context;
    private List<Feed> feedList;

    public UserDetailAdapter(Context context, List<Feed> feedList) {
        this.context = context;
        this.feedList = feedList;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        Feed feed = feedList.get(position);

        // 닉네임 설정
        holder.textViewNickname.setText(feed.getNickname());

        // 피드 설명 설정
        holder.textViewfeeds.setText(feed.getCaption());

        // 이미지 로드 (첫 번째 이미지)
        if (feed.getImageList() != null && !feed.getImageList().isEmpty()) {
            Glide.with(context)
                    .load(feed.getImageList().get(0)) // 첫 번째 이미지
                    .placeholder(R.drawable.loding)
                    .error(R.drawable.error)
                    .into(holder.imageViewProfile);
        } else {
            holder.imageViewProfile.setImageResource(R.drawable.error); // 기본 이미지 설정
        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public void setFeedList(List<Feed> feedList) {
        this.feedList = feedList;
        notifyDataSetChanged();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNickname;
        private TextView textViewfeeds;
        private ImageView imageViewProfile;
        private Button buttonLike;
        private TextView textViewLikeCount;
        private TextView CommentCount;
        private ViewPager2 viewpagerImages;
        private View indicator;
        private Button btnFollow;
        private Button addTagButton;
        private ViewGroup tagContainer;
        private ImageView buttonMinimenu;
        private List<View> tagViews;



        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNickname = itemView.findViewById(R.id.tv_nickname);
            textViewfeeds = itemView.findViewById(R.id.tv_feed);
            imageViewProfile = itemView.findViewById(R.id.img_profile);
            indicator = itemView.findViewById(R.id.indicator);
            btnFollow = itemView.findViewById(R.id.follow);
            addTagButton = itemView.findViewById(R.id.btn_add_tag);
            tagContainer = itemView.findViewById(R.id.tag_container);
            buttonMinimenu = itemView.findViewById(R.id.button_minimenu);
            tagViews = new ArrayList<>(); // 태그 리스트 초기화
        }
    }
}
