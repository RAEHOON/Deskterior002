package com.example.desk0018.Adapter;

import android.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
    private static final String TAG = "TagEditAdapter";
    private final int imageCount; // 이미지 카운트 저장


    public TagEditAdapter(List<TagData> tagList, int imageCount) {
        Log.d(TAG, "TagEditAdapter 생성자 호출 - 이미지 카운트: " + imageCount);
        this.tagList = tagList;
        this.imageCount = imageCount; // 생성자에서 이미지 카운트 전달받기
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder 호출");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        TagData tag = tagList.get(position);
        Log.d(TAG, "onBindViewHolder 호출 - 태그 이름: " + tag.getName());

        holder.tagName.setText(tag.getName());
        holder.tagCategory.setText(tag.getCategory());

        holder.textView_edit_tag.setOnClickListener(v -> {
            Log.d(TAG, "태그 수정 버튼 클릭 - 태그 이름: " + tag.getName());
            showEditTagDialog(holder, tag);
        });
        holder.textView_delete_tag.setOnClickListener(v -> {
            Log.d(TAG, "태그 삭제 버튼 클릭 - 태그 이름: " + tag.getName());
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                deleteTag(tagList.get(adapterPosition), adapterPosition, holder);
            }
        });
    }
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount 호출 - 태그 리스트 크기: " + tagList.size());
        return tagList.size();
    }


    private void deleteTag(TagData tag, int position, TagViewHolder holder) {
        String tagName = tag.getName(); // 태그 이름 가져오기
        Log.d(TAG, "deleteTag 호출 - 태그 이름: " + tag.getName());

        // API 호출로 태그 삭제
        ApiService apiService = RetrofitClient.getApi();
        Call<ServerResponse> call = apiService.deleteTag(imageCount, tagName); // imageCount 사용
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.d(TAG, "deleteTag 응답 수신 - 성공 여부: " + response.isSuccessful());
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        // RecyclerView에서 태그 삭제
                        tagList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, tagList.size());
                        Log.d(TAG, "태그 삭제 성공 - 현재 태그 리스트 크기: " + tagList.size());
                        Toast.makeText(holder.itemView.getContext(), "태그 삭제 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "태그 삭제 실패: " + response.message());
                        Toast.makeText(holder.itemView.getContext(), "태그 삭제 실패: " + response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(holder.itemView.getContext(), "서버 응답 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e(TAG, "태그 삭제 요청 실패", t);
                Toast.makeText(holder.itemView.getContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        TextView tagName;
        TextView textView_edit_tag;
        TextView textView_delete_tag;
        TextView tagCategory; // 유형 표시
        FrameLayout tagContainer;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tag_name);
            tagCategory = itemView.findViewById(R.id.tag_category);
            textView_edit_tag = itemView.findViewById(R.id.textView_edit_tag);
            textView_delete_tag = itemView.findViewById(R.id.textView_delete_tag);
            tagContainer = itemView.findViewById(R.id.tag_container);
        }
    }

    private void showEditTagDialog(TagViewHolder holder, TagData tag) {
        Log.d(TAG, "showEditTagDialog 호출 - 태그 이름: " + tag.getName());

        // 기존 카테고리 목록
        String[] categories = {"키보드", "마우스, 패드", "모니터", "스피커", "조명", "케이스", "테이블", "선정리", "기타"};
        Log.d(TAG, "카테고리 목록 생성 완료");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.itemView.getContext(), android.R.layout.simple_spinner_item, categories);
        Log.d(TAG, "카테고리 어댑터 생성 완료");

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.d(TAG, "카테고리 드롭다운 설정 완료");

        Spinner spinner = new Spinner(holder.itemView.getContext());
        Log.d(TAG, "Spinner 객체 생성 완료");

        spinner.setAdapter(adapter);
        Log.d(TAG, "Spinner 어댑터 적용 완료");

        spinner.setSelection(getCategoryIndex(categories, tag.getCategory())); // 기존 선택 설정
        Log.d(TAG, "Spinner 기존 선택값 설정 완료 - 선택된 카테고리: " + tag.getCategory());

        // 다이얼로그 UI 구성
        LinearLayout dialogLayout = new LinearLayout(holder.itemView.getContext());
        Log.d(TAG, "LinearLayout 생성 완료");

        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        Log.d(TAG, "LinearLayout 방향 설정 완료");

        TextView categoryLabel = new TextView(holder.itemView.getContext());
        Log.d(TAG, "TextView 생성 완료 (유형 선택)");

        categoryLabel.setText("유형 선택:");
        Log.d(TAG, "TextView 텍스트 설정 완료");

        dialogLayout.addView(categoryLabel);
        Log.d(TAG, "categoryLabel 다이얼로그 레이아웃에 추가 완료");

        dialogLayout.addView(spinner);
        Log.d(TAG, "spinner 다이얼로그 레이아웃에 추가 완료");

        EditText nameInput = new EditText(holder.itemView.getContext());
        Log.d(TAG, "EditText 생성 완료 (태그 이름 입력)");

        nameInput.setText(tag.getName()); // 기존 이름 설정
        Log.d(TAG, "태그 이름 설정 완료: " + tag.getName());

        dialogLayout.addView(nameInput);
        Log.d(TAG, "nameInput 다이얼로그 레이아웃에 추가 완료");

        EditText urlInput = new EditText(holder.itemView.getContext());
        Log.d(TAG, "EditText 생성 완료 (URL 입력)");

        urlInput.setText(tag.getUrl()); // 기존 URL 설정
        Log.d(TAG, "URL 설정 완료: " + tag.getUrl());

        dialogLayout.addView(urlInput);
        Log.d(TAG, "urlInput 다이얼로그 레이아웃에 추가 완료");

        new AlertDialog.Builder(holder.itemView.getContext())
                .setTitle("태그 수정")
                .setView(dialogLayout)
                .setPositiveButton("다음", (dialog, which) -> {
                    Log.d(TAG, "태그 수정 다이얼로그 확인 버튼 클릭");

                    String updatedName = nameInput.getText().toString();
                    Log.d(TAG, "수정된 태그 이름 입력: " + updatedName);

                    String updatedUrl = urlInput.getText().toString();
                    Log.d(TAG, "수정된 URL 입력: " + updatedUrl);

                    String updatedCategory = spinner.getSelectedItem().toString();
                    Log.d(TAG, "수정된 카테고리 선택: " + updatedCategory);

                    // 태그 이동을 이미지 위에서 가능하게
                    moveTagdrag(holder, tag, updatedCategory, updatedName, updatedUrl);
                })
                .setNegativeButton("취소", (dialog, which) -> Log.d(TAG, "태그 수정 다이얼로그 취소 버튼 클릭"))
                .show();
        Log.d(TAG, "태그 수정 다이얼로그 표시 완료");
    }
    private void moveTagdrag(TagViewHolder holder, TagData tag, String updatedCategory, String updatedName, String updatedUrl) {
        Log.d(TAG, "moveTagdrag 호출 - 태그 수정 시작");

        // 태그 아이콘 추가
        ImageView tagIcon = new ImageView(holder.itemView.getContext());
        Log.d(TAG, "태그 아이콘 생성 완료");

        tagIcon.setImageResource(R.drawable.plusssscircle);
        Log.d(TAG, "태그 아이콘 리소스 설정 완료");

        // 태그의 현재 위치 설정
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(40, 40);
        params.leftMargin = tag.getX();
        params.topMargin = tag.getY();
        Log.d(TAG, "태그 현재 위치 설정 - X: " + tag.getX() + ", Y: " + tag.getY());

        tagIcon.setLayoutParams(params);
        Log.d(TAG, "태그 아이콘 레이아웃 파라미터 설정 완료");

        // 기존 이미지 위에 태그 추가
        holder.tagContainer.addView(tagIcon);
        Log.d(TAG, "태그 아이콘 tagContainer에 추가 완료");

        // 완료 버튼 추가
        TextView btnDone = new TextView(holder.itemView.getContext());
        Log.d(TAG, "완료 버튼 생성 완료");

        btnDone.setText("수정 완료");
        Log.d(TAG, "완료 버튼 텍스트 설정 완료");

        btnDone.setPadding(20, 20, 20, 20);
        Log.d(TAG, "완료 버튼 패딩 설정 완료");

        btnDone.setBackgroundResource(R.drawable.save);
        Log.d(TAG, "완료 버튼 배경 설정 완료");

        FrameLayout.LayoutParams btnParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnParams.gravity = Gravity.BOTTOM | Gravity.END;
        btnParams.bottomMargin = 50;
        btnParams.rightMargin = 50;
        Log.d(TAG, "완료 버튼 레이아웃 파라미터 설정 완료");

        holder.tagContainer.addView(btnDone, btnParams);
        Log.d(TAG, "완료 버튼 tagContainer에 추가 완료");

        // 태그 드래그 기능
        tagIcon.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    v.setX(event.getRawX() - v.getWidth() / 2);
                    v.setY(event.getRawY() - v.getHeight() / 2);
                    Log.d(TAG, "태그 이동 중 - X: " + v.getX() + ", Y: " + v.getY());
                    break;
            }
            return true;
        });

        Log.d(TAG, "태그 드래그 리스너 설정 완료");

        btnDone.setOnClickListener(v -> {
            int newX = (int) tagIcon.getX();
            int newY = (int) tagIcon.getY();
            Log.d(TAG, "태그 위치 수정 완료 - 새 위치 X: " + newX + ", Y: " + newY);

            // 태그 업데이트 API 호출
            updateTagOnServer(imageCount, updatedCategory, updatedName, updatedUrl, newX, newY, holder);

            // UI에서 태그 제거
            holder.tagContainer.removeView(tagIcon);
            Log.d(TAG, "태그 아이콘 제거 완료");

            holder.tagContainer.removeView(btnDone);
            Log.d(TAG, "완료 버튼 제거 완료");
        });

        Log.d(TAG, "moveTagdrag 설정 완료");
    }

    private void updateTagOnServer(int imageCount, String category, String name, String url, int x, int y, TagViewHolder holder) {
        Log.d(TAG, "updateTagOnServer 호출 - 이미지 카운트: " + imageCount + ", 카테고리: " + category + ", 이름: " + name + ", URL: " + url + ", 위치(X, Y): " + x + ", " + y);

        ApiService apiService = RetrofitClient.getApi();
        Call<ServerResponse> call = apiService.editTag(imageCount, category, name, url, x, y);
        Log.d(TAG, "API 호출 준비 완료");

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.d(TAG, "서버 응답 수신 - 성공 여부: " + response.isSuccessful());

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Log.d(TAG, "태그 수정 성공: " + response.body().getMessage());
                    Toast.makeText(holder.itemView.getContext(), "태그 수정 성공", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                } else {
                    Log.e(TAG, "태그 수정 실패 - 응답 메시지: " + response.message());
                    Toast.makeText(holder.itemView.getContext(), "태그 수정 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e(TAG, "서버 오류 발생", t);
                Toast.makeText(holder.itemView.getContext(), "서버 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d(TAG, "API 호출 실행 완료");
    }


    private int getCategoryIndex(String[] categories, String category) {
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(category)) {
                return i;
            }
        }
        return 0; // 기본값
    }


}
