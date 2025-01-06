package com.example.desk0018.Tag;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.desk0018.R;

public class WebViewActivity extends AppCompatActivity {
    //웹뷰엑티비티

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 클래스시작 온크리에이트
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        // 레이아웃 지정

        WebView webView = findViewById(R.id.web_view);
        //웹뷰 레이아웃 지정
        String url = getIntent().getStringExtra("url");
        // url 설정하기 인텐트

        if (url != null && !url.isEmpty()) {
            // URL이 http로 시작하는 경우 https로 변경
            if (url.startsWith("http://")) {
                url = url.replaceFirst("http://", "https://");
            } else if (!url.startsWith("http://") && !url.startsWith("https://")) {
                // URL이 http나 https로 시작하지 않으면 https를 추가
                url = "https://" + url;
            }
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}