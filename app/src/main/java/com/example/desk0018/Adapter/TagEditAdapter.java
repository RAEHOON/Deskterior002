package com.example.desk0018.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.desk0018.R;
import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.RetrofitClient;
import com.example.desk0018.Server.ServerResponse;
import com.example.desk0018.Tag.TagData;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagEditAdapter extends RecyclerView.Adapter<TagEditAdapter.TagViewHolder> {

    private final List<TagData> tagList;
    private final int imageCount; // 이미지 카운트 저장

    public TagEditAdapter(List<TagData> tagList, int imageCount) {
        this.tagList = tagList;
        this.imageCount = imageCount; // 생성자에서 이미지 카운트 전달받기
    }

    @NonNull
    @Override
    public TagEditAdapter.TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        TagData tag = tagList.get(position);

        holder.tagName.setText(tag.getName()); // 태그 이름 설정
        holder.tagCategory.setText(tag.getCategory()); // 유형 설정

        // 삭제 버튼 클릭 이벤트
        holder.textView_delete_tag.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                deleteTag(tagList.get(adapterPosition), adapterPosition, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    private void deleteTag(TagData tag, int position, TagViewHolder holder) {
        String tagName = tag.getName(); // 태그 이름 가져오기

        // API 호출로 태그 삭제
        ApiService apiService = RetrofitClient.getApi();
        Call<ServerResponse> call = apiService.deleteTag(imageCount, tagName); // imageCount 사용
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        // RecyclerView에서 태그 삭제
                        tagList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, tagList.size());
                        Toast.makeText(holder.itemView.getContext(), "태그 삭제 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(holder.itemView.getContext(),
                                "태그 삭제 실패: " + response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(holder.itemView.getContext(), "서버 응답 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(holder.itemView.getContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        TextView tagName;
        TextView textView_edit_tag;
        TextView textView_delete_tag;
        TextView tagCategory; // 유형 표시

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tag_name);
            tagCategory = itemView.findViewById(R.id.tag_category);
            textView_edit_tag = itemView.findViewById(R.id.textView_edit_tag);
            textView_delete_tag = itemView.findViewById(R.id.textView_delete_tag);
        }
    }
}