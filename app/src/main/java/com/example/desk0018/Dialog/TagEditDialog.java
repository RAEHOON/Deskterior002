package com.example.desk0018.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desk0018.Adapter.TagEditAdapter;
import com.example.desk0018.Adapter.TagListAdapter;
import com.example.desk0018.R;
import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.RetrofitClient;
import com.example.desk0018.Tag.TagData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagEditDialog extends Dialog {

    private static final String TAG = "TagEditDialog";
    private RecyclerView recyclerView;
    private TagEditAdapter tagEditAdapter;
    private Context context;
    private List<TagData> tagList;
    private int feedCount;
    private int imageCount;

    public TagEditDialog(@NonNull Context context, int feedCount, int imageCount) {
        super(context);
        Log.d(TAG, "TagEditDialog 생성자: feedCount=" + feedCount + ", imageCount=" + imageCount);
        this.context = context;
        this.feedCount = feedCount;
        this.imageCount = imageCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_edit_dialog);

        recyclerView = findViewById(R.id.recyclerview_tag_edit);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getWindow() != null) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(params);
        }

        // 데이터 불러오기
        loadTagData();
    }

    private void loadTagData() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<TagData>> call = apiService.getTags(feedCount, imageCount);
        call.enqueue(new Callback<List<TagData>>() {
            @Override
            public void onResponse(Call<List<TagData>> call, Response<List<TagData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tagList = response.body();

                    // 어댑터 설정 (imageCount 전달)
                    tagEditAdapter = new TagEditAdapter(tagList, imageCount);
                    recyclerView.setAdapter(tagEditAdapter);
                    tagEditAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "서버 응답 실패 - " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<TagData>> call, Throwable t) {
                Log.e(TAG, "네트워크 오류 발생 - " + t.getMessage());
            }
        });
    }
}
