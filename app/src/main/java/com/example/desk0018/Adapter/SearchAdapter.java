package com.example.desk0018.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.desk0018.R;
import com.example.desk0018.Tag.TagData;
import com.example.desk0018.Users.User;
import com.example.desk0018.Users.UserDetailActivity;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SearchAdapter";
    private static final int VIEW_TYPE_NICKNAME = 0;
    private static final int VIEW_TYPE_TAG = 1;

    private List<Object> searchResults;

    public SearchAdapter(List<Object> searchResults) {
        this.searchResults = searchResults;
        Log.d(TAG, "SearchAdapter 생성됨. 검색 결과 수: " + searchResults.size());
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: position = " + position);
        if (searchResults.get(position) instanceof User) {
            Log.d(TAG, "getItemViewType: User 객체");
            return VIEW_TYPE_NICKNAME;
        } else if (searchResults.get(position) instanceof TagData) {
            Log.d(TAG, "getItemViewType: TagData 객체");
            return VIEW_TYPE_TAG;
        }
        Log.e(TAG, "getItemViewType: 잘못된 객체 타입");
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: viewType = " + viewType);
        if (viewType == VIEW_TYPE_NICKNAME) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nickname, parent, false);
            Log.d(TAG, "onCreateViewHolder: NicknameViewHolder 생성");
            return new NicknameViewHolder(view);
        } else if (viewType == VIEW_TYPE_TAG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
            Log.d(TAG, "onCreateViewHolder: TagViewHolder 생성");
            return new TagViewHolder(view);
        }
        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: position = " + position + ", holder = " + holder.getClass().getSimpleName());
        if (holder instanceof NicknameViewHolder) {
            User user = (User) searchResults.get(position);
            Log.d(TAG, "onBindViewHolder: User 객체 바인딩, 닉네임 = " + user.getNickname());
            Log.d(TAG, "onBindViewHolder: 프로필 이미지 URL = " + user.getProfileImage());
            ((NicknameViewHolder) holder).bind(user);
        } else if (holder instanceof TagViewHolder) {
            TagData tag = (TagData) searchResults.get(position);
            Log.d(TAG, "onBindViewHolder: TagData 객체 바인딩, 태그 이름 = " + tag.getName());
            Log.d(TAG, "onBindViewHolder: 태그 URL = " + tag.getUrl());
            ((TagViewHolder) holder).bind(tag);
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: 검색 결과 수 = " + searchResults.size());
        return searchResults.size();
    }

    static class NicknameViewHolder extends RecyclerView.ViewHolder {
        private ImageView imvProfile;
        private TextView tvNickname;

        public NicknameViewHolder(@NonNull View itemView) {
            super(itemView);
            imvProfile = itemView.findViewById(R.id.imv_profile);
            tvNickname = itemView.findViewById(R.id.tv_nickname);
            Log.d(TAG, "NicknameViewHolder: ViewHolder 생성 완료");
        }

        public void bind(User user) {
            Log.d(TAG, "NicknameViewHolder.bind: 닉네임 설정 = " + user.getNickname());
            tvNickname.setText(user.getNickname());
            String profileImageUrl = "http://43.203.234.99/"+ user.getProfileImage();

            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                Log.d(TAG, "NicknameViewHolder.bind: Glide로 프로필 이미지 로드 시작, URL = " + profileImageUrl);

                Glide.with(itemView.getContext())
                        .load(profileImageUrl)
                        .placeholder(R.drawable.loding) // 로딩 중 표시할 이미지
                        .apply(RequestOptions.circleCropTransform()) // 원형 이미지
                        .error(R.drawable.error) // 로드 실패 시 표시할 이미지
                        .into(imvProfile);
            } else {
                Log.e(TAG, "NicknameViewHolder.bind: 프로필 이미지 URL이 비어 있음");
                imvProfile.setImageResource(R.drawable.error); // 기본 이미지 표시
            }
            // 프로필 클릭 이벤트
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), UserDetailActivity.class);
                intent.putExtra("nickname", user.getNickname());
                intent.putExtra("profileImage", user.getProfileImage());
                itemView.getContext().startActivity(intent);
            });
        }
    }

    static class TagViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTagName;
        private Button btnGoSite;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTagName = itemView.findViewById(R.id.tag_name);
            btnGoSite = itemView.findViewById(R.id.btn_go_site);
            Log.d(TAG, "TagViewHolder: ViewHolder 생성 완료");
        }

        public void bind(TagData tag) {
            Log.d(TAG, "TagViewHolder.bind: 태그 이름 설정 = " + tag.getName());
            tvTagName.setText(tag.getName());
            btnGoSite.setOnClickListener(v -> {
                Log.d(TAG, "TagViewHolder.bind: 태그 URL 열기 = " + tag.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tag.getUrl()));
                itemView.getContext().startActivity(intent);
            });
        }
    }
}