package com.example.desk0018.Adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.TagViewHolder> {
    private List<TagData> tagList;

    public TagListAdapter(List<TagData> tagList) {
        this.tagList = tagList;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_list, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        TagData tag = tagList.get(position);
        holder.tagName.setText(tag.getName());
        holder.btnGoSite.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), WebViewActivity.class);
            intent.putExtra("url", tag.getUrl());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        TextView tagName;
        Button btnGoSite;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tag_name);
            btnGoSite = itemView.findViewById(R.id.btn_go_site);
        }
    }
}
