package com.example.desk0018.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import com.example.desk0018.R;

public class MypageDialog extends Dialog {

    private Button button_editmyself;
    private Button button_logout;
    private Button button_deletemyself;

    public MypageDialog(Context context, OnMypageActionListener listener){
        super(context);

        setContentView(R.layout.activity_mypage_dialog);

        button_editmyself = findViewById(R.id.button_editmyself);
        button_logout = findViewById(R.id.button_logout);
        button_deletemyself = findViewById(R.id.button_deletemyself);

        button_editmyself.setOnClickListener(v -> {
            listener.onEditMyself();
            dismiss();
        });

        button_logout.setOnClickListener(v -> {
            listener.onLogOut();
            dismiss();
        });

        button_deletemyself.setOnClickListener(v -> {
            listener.onDeleteMyself();
            dismiss();
        });
    }

    public interface OnMypageActionListener {

        void onEditMyself();
        void onLogOut();
        void onDeleteMyself();
    }
}
