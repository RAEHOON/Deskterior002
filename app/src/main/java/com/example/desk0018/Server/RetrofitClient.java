package com.example.desk0018.Server;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Retrofit 통신 로그를 출력하기 위해 인터셉터 추가
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();


            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor) // 로그 인터셉터 추가
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://43.203.234.99/") // 서버 URL 설정
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 변환기
                    .client(client) // 로그가 포함된 OkHttpClient 추가
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApi() {
        return getClient().create(ApiService.class);
    }
}
