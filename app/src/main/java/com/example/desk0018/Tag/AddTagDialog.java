package com.example.desk0018.Tag;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.desk0018.R;

public class AddTagDialog extends Dialog {
    private EditText etName, etUrl;  // 이름과 URL 입력 필드
    private Button btnConfirm;  // 확인 버튼
    private AddTagInterface listener;  // 태그 추가 리스너

    public AddTagDialog(@NonNull Context context, AddTagInterface listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag_dialog);  // 다이얼로그 레이아웃 설정

        etName = findViewById(R.id.et_name);  // 이름 입력 필드
        etUrl = findViewById(R.id.et_url);    // URL 입력 필드
        btnConfirm = findViewById(R.id.btn_confirm);  // 확인 버튼

        btnConfirm.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String url = etUrl.getText().toString();

            if (!name.isEmpty() && !url.isEmpty()) {
                listener.onTagAdded(name, url);  // 이름과 URL 전달
                dismiss();
            } else {
                if (name.isEmpty()) etName.setError("이름을 입력해주세요.");
                if (url.isEmpty()) etUrl.setError("주소를 입력해주세요.");
            }
        });

        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        }
    }

    public interface AddTagInterface {
        void onTagAdded(String name, String url);
    }
}