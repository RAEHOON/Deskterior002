package com.example.desk0018.Adapter;

import android.content.Intent;
import android.net.Uri;
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

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_NICKNAME = 0;
    private static final int VIEW_TYPE_TAG = 1;

    private List<Object> searchResults;

    public SearchAdapter(List<Object> searchResults) {
        this.searchResults = searchResults;
    }

    @Override
    public int getItemViewType(int position) {
        if (searchResults.get(position) instanceof User) {
            return VIEW_TYPE_NICKNAME;
        } else if (searchResults.get(position) instanceof TagData) {
            return VIEW_TYPE_TAG;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NICKNAME) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nickname, parent, false);
            return new NicknameViewHolder(view);
        } else if (viewType == VIEW_TYPE_TAG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
            return new TagViewHolder(view);
        }
        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NicknameViewHolder) {
            User user = (User) searchResults.get(position);
            ((NicknameViewHolder) holder).bind(user);
        } else if (holder instanceof TagViewHolder) {
            TagData tag = (TagData) searchResults.get(position);
            ((TagViewHolder) holder).bind(tag);
        }
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    static class NicknameViewHolder extends RecyclerView.ViewHolder {
        private ImageView imvProfile;
        private TextView tvNickname;

        public NicknameViewHolder(@NonNull View itemView) {
            super(itemView);
            imvProfile = itemView.findViewById(R.id.imv_profile);
            tvNickname = itemView.findViewById(R.id.tv_nickname);
        }

        public void bind(User user) {
            tvNickname.setText(user.getNickname());
            String profileImageUrl = user.getProfileImage();

            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                // Glide를 사용해 프로필 이미지 로드
                Glide.with(itemView.getContext())
                        .load(profileImageUrl)
                        .placeholder(R.drawable.loding) // 로딩 중 표시할 이미지
                        .apply(RequestOptions.circleCropTransform()) // 원형 이미지
                        .error(R.drawable.error) // 로드 실패 시 표시할 이미지
                        .into(imvProfile);
            } else {
                // 프로필 이미지가 없을 경우 기본 이미지 표시
                imvProfile.setImageResource(R.drawable.error);
            }
        }
    }

    static class TagViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTagName;
        private Button btnGoSite;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTagName = itemView.findViewById(R.id.tag_name);
            btnGoSite = itemView.findViewById(R.id.btn_go_site);
        }

        public void bind(TagData tag) {
            tvTagName.setText(tag.getName());
            btnGoSite.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tag.getUrl()));
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
