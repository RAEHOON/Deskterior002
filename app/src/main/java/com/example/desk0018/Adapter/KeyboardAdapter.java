package com.example.desk0018.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class KeyboardAdapter extends RecyclerView.Adapter<KeyboardAdapter.KeyboardViewHolder> {



    @NonNull
    @Override
    public KeyboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull KeyboardAdapter.KeyboardViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class KeyboardViewHolder extends RecyclerView.ViewHolder{

        public KeyboardViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
