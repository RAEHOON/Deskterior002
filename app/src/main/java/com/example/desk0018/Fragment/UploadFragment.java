package com.example.desk0018.Fragment;


import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.desk0018.R;
import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.RetrofitClient;
import com.example.desk0018.Server.ServerResponse;
import com.example.desk0018.Server.UserProfileResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***
 * 업로드프레그먼트
 * 업로드 버튼을 눌렀을 때 나오는 화면으로 피드를 업로드하는 기능을 구현해주는 클래스
 */

public class UploadFragment extends Fragment {
    // UploadFragment 클래스 생성 프래그먼트
    String TAG = "UploadFragment";
    private final int REQUEST_IMAGE_PICK = 1;
    //갤러리에서 이미지를 고를때 쓰는 값?
    private final int TOTAL_CELLS = 9;
    // 이미지 선택 그리드의 총 셀 개수

    private String profileImageUrl;
    // 프로필 이미지 URL 저장 변수
    private GridLayout gridImagePicker;
    //GridLayout
    private EditText etDescription;
    //EditText
    private Button btnUpload;
    //업로드 버튼
    private ImageView ivProfileImage;
    //ImageView 변수
    private TextView tvNickname;
    //extView 변수
    private List<Uri> selectedImageUris = new ArrayList<>();
    //선택된 이미지 URI를 저장할 리스트

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //프래그먼트의 뷰를 생성하는 메서드

        View view = inflater.inflate(R.layout.activity_upload_fragment, container, false);
        //레이아웃을 바꿔서 뷰로 받기
        ivProfileImage = view.findViewById(R.id.iv_profile_image);
        //프로필 이미지 레이아웃 연결
        tvNickname = view.findViewById(R.id.tv_nickname);
        //닉네임 레이아웃 연결
        etDescription = view.findViewById(R.id.et_description);
        //설명 입력 EditText 연결
        btnUpload = view.findViewById(R.id.btn_upload);
        //업로드 버튼 연결
        gridImagePicker = view.findViewById(R.id.grid_image_picker);
        //이미지 선택 그리드레이아웃 연결

        resetEmptyGrid();
        // 빈 이미지 그리드 초기화
        gridImagePicker.setOnClickListener(v -> selectImagesFromGallery());
        //그리드 클릭하면 갤러리에서 이미지 선택 메서드 호출
        btnUpload.setOnClickListener(v -> uploadFeedToServer());
        // 업로드 버튼 클릭하면 서버로 데이터 업로드 메서드 호출

        loadUserProfile();
        // 사용자 프로필 데이터 로드 메서드 호출

        return view;
        //View 반환
    }

    private void loadUserProfile() {
        //프로필 데이터를 서버에서 가져오는 메서드

        SharedPreferences prefs = getContext().getSharedPreferences("로그인정보", MODE_PRIVATE);
        // 셰어드를 써서 "로그인정보"라는 이름의 파일에서 데이터 가져오기
        String userId = prefs.getString("user_id", null);
        // 셰어드에서 "user_id" 값을 가져와 userId 에 저장

        if (userId != null) {
            // userId가 null이 아닐 경우 (로그인했을경우?)
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            //레트로핏으로 apiservice 객체 생성하기
            Call<UserProfileResponse> call = apiService.getUserProfile(userId);
            //유저프로파일리스펀스로로 call 객체 만들어서 유저아이디정보 서버에서 불러옴

            call.enqueue(new Callback<UserProfileResponse>() {
                // 응답오는거를 비동기적으로 처리하기 위해 콜백 설정
                @Override
                public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                    // 서버 응답 성공 시 실행
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        //응답이 성공적이고, 응답 데이터가 null이 아닐 경우
                        profileImageUrl = "http://43.203.234.99/" + response.body().getProfileImage();
                        // 프로필 이미지 URL 설정
                        tvNickname.setText(response.body().getNickname());
                        //닉네임을 텍스트뷰에 설정
                        if (profileImageUrl != null) {
                            loadProfileImageWithGlide(profileImageUrl);
                            // Glide를 사용해 프로필 이미지 로드
                        }
                    } else {
                        Toast.makeText(getContext(), "프로필 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                        // 오류 메시지 표시
                    }
                }

                @Override
                public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                    // 서버 요청 실패 시 실행
                    Log.e(TAG, "API 호출 실패: ", t);
                    // 오류 로그 출력
                    Toast.makeText(getContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    // 사용자에게 네트워크 오류 알림
                }
            });
        }
    }

    private void loadProfileImageWithGlide(String imageUrl) {
        // Glide를 사용해 프로필 이미지를 로드하는 메서드
        Glide.with(this)
                .load(imageUrl)
                // 이미지 URL 로드
                .placeholder(R.drawable.loding)
                // 로딩 중 표시할 이미지
                .error(R.drawable.error)
                // 로드 실패 시 표시할 이미지
                .into(ivProfileImage);
        // 프로필 이미지를 ivProfileImage에 삽입
    }

    private void resetEmptyGrid() {
        //빈그리도 초기화 메서드

        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        //전체 너비 가져오기
        int columnCount = 3;
        // 칼럼 개수 설정 3
        int cellSize = totalWidth / columnCount;
        //셀 크기 = 전체 너비 / 칼럼개수

        for (int i = 0; i < TOTAL_CELLS; i++) {
            // 총 셀 개수만큼 포문실행
            ImageView imageView = new ImageView(getContext());
            // 새로운 ImageView 생성
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            //그리드 레이아웃설정
            params.width = cellSize;
            params.height = cellSize;
            params.setMargins(0, 0, 0, 0);
            // 셀 크기와 여백 설정

            imageView.setLayoutParams(params);
            // 이미지뷰에 레이아웃 params 적용
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // 이미지 크기를 중앙에 맞춤
            imageView.setBackgroundResource(R.drawable.ic_launcher_foreground);
            // 빈 이미지일 때 기본 배경 설정
            imageView.setTag("empty");
            // 태그로 빈 상태 표시
            gridImagePicker.addView(imageView);
            // 그리드에 이미지뷰 추가
        }
    }

    private void selectImagesFromGallery() {
        //갤러리에서 이미지를 선택하는 메서드

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // 인텐트 설정, 갤러리에서  컨텐트 가져오는거로
        intent.setType("image/*");
        // 파일 타입을 이미지로 제한
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        // 다중 선택 허용
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요"), REQUEST_IMAGE_PICK);
        // 인텐트 실행 및 결과 대기
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 갤러리에서 선택한 결과를 처리하는 메서드
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            //결과가 잘 반환됬을때 , 성공?
            if (data.getClipData() != null) {
                //여러개 선택한개 널이 아닐경우
                int count = data.getClipData().getItemCount();
                // 선택된 이미지 개수 가져오기
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    // 선택된 이미지 URI 가져오기
                    addImageToGrid(imageUri);
                    // 그리드에 이미지 추가
                }
            } else if (data.getData() != null) {
                //한개 선택한게 널이 아닐경우
                Uri imageUri = data.getData();
                // 이미지 URI 가져오기
                addImageToGrid(imageUri);
                // 그리드에 이미지 추가
            }
        }
    }

    private void addImageToGrid(Uri imageUri) {
        //선택한 이미지를 그리드에 추가하는 메서드

        for (int i = 0; i < gridImagePicker.getChildCount(); i++) {
            // 그리드의 모든 셀 반복
            View child = gridImagePicker.getChildAt(i);
            // 현재 셀 가져오기
            if (child instanceof ImageView && "empty".equals(child.getTag())) {
                // "empty"인 셀을 찾으면
                ImageView imageView = (ImageView) child;
                // 이미지뷰로 에 child넣기
                Glide.with(this)
                        .load(imageUri)
                        // 선택한 이미지 로드
                        .placeholder(R.drawable.ic_launcher_foreground)
                        // 기본 배경 설정
                        .into(imageView);
                // 이미지를 해당 ImageView에 삽입
                imageView.setTag("filled");
                // 태그를 "filled"로 변경
                selectedImageUris.add(imageUri);
                // 선택된 이미지 URI 리스트에 추가
                break;
                // 반복문 종료
            }
        }
    }

    private void uploadFeedToServer() {
        Log.d(TAG, "uploadFeedToServer 호출"); // 메서드 호출 로그

        SharedPreferences prefs = getContext().getSharedPreferences("로그인정보", MODE_PRIVATE);
        Log.d(TAG, "SharedPreferences 가져오기 완료"); // SharedPreferences 가져오기 확인 로그

        String userId = prefs.getString("user_id", null);
        Log.d(TAG, "userId 가져오기 완료: " + userId); // userId 확인 로그

        String nickname = tvNickname.getText().toString();
        Log.d(TAG, "닉네임 가져오기 완료: " + nickname); // 닉네임 가져오기 확인 로그

        String caption = etDescription.getText().toString();
        Log.d(TAG, "설명 가져오기 완료: " + caption); // 설명 가져오기 확인 로그

        if (userId == null || selectedImageUris.isEmpty() || caption.isEmpty() || profileImageUrl == null) {
            Log.e(TAG, "필수 데이터 누락: userId=" + userId + ", 이미지 개수=" + selectedImageUris.size() + ", 설명=" + caption + ", 프로필 이미지=" + profileImageUrl);
            // 필수 데이터 누락 확인 로그
            Toast.makeText(getContext(), "ID, 사진, 설명을 모두 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), userId);
        Log.d(TAG, "userIdBody 생성 완료"); // userIdBody 생성 확인 로그

        RequestBody nicknameBody = RequestBody.create(MediaType.parse("text/plain"), nickname);
        Log.d(TAG, "nicknameBody 생성 완료"); // nicknameBody 생성 확인 로그

        RequestBody profileImageBody = RequestBody.create(MediaType.parse("text/plain"), profileImageUrl);
        Log.d(TAG, "profileImageBody 생성 완료"); // profileImageBody 생성 확인 로그

        RequestBody captionBody = RequestBody.create(MediaType.parse("text/plain"), caption);
        Log.d(TAG, "captionBody 생성 완료"); // captionBody 생성 확인 로그

        List<MultipartBody.Part> imageParts = new ArrayList<>();
        Log.d(TAG, "imageParts 리스트 초기화 완료"); // imageParts 초기화 확인 로그

        int index = 0;
        for (Uri uri : selectedImageUris) {
            Log.d(TAG, "이미지 처리 시작, URI: " + uri); // 각 이미지 URI 처리 시작 로그

            try (InputStream inputStream = getContext().getContentResolver().openInputStream(uri)) {
                Log.d(TAG, "InputStream 생성 완료"); // InputStream 생성 확인 로그

                byte[] imageBytes = new byte[inputStream.available()];
                Log.d(TAG, "이미지 byte 배열 생성 완료, 크기: " + imageBytes.length); // byte 배열 생성 확인 로그

                inputStream.read(imageBytes);
                Log.d(TAG, "이미지 데이터 읽기 완료"); // 이미지 데이터 읽기 확인 로그

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
                Log.d(TAG, "requestFile 생성 완료"); // RequestBody 생성 확인 로그

                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image_" + index, "image_" + index + ".jpg", requestFile);
                Log.d(TAG, "imagePart 생성 완료: " + imagePart); // MultipartBody.Part 생성 확인 로그

                imageParts.add(imagePart);
                Log.d(TAG, "imagePart 리스트 추가 완료"); // 리스트 추가 확인 로그

                index++;
            } catch (IOException e) {
                Log.e(TAG, "이미지 처리 중 오류 발생", e); // 예외 발생 로그
            }
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Log.d(TAG, "ApiService 생성 완료"); // ApiService 생성 확인 로그

        Call<ServerResponse> call = apiService.uploadFeed(userIdBody, nicknameBody, profileImageBody, captionBody, imageParts);
        Log.d(TAG, "uploadFeed API 호출 준비 완료"); // API 호출 준비 로그

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.d(TAG, "API 응답 수신");
                Log.d(TAG, "HTTP 상태 코드: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "응답 성공: " + response.body().toString());
                    if (response.body().isSuccess()) {
                        Toast.makeText(getContext(), "업로드 성공", Toast.LENGTH_SHORT).show();
                        moveToHomeFragment();
                    } else {
                        Log.e(TAG, "서버 응답 실패, 메시지: " + response.body().getMessage());
                    }
                } else {
                    Log.e(TAG, "response.isSuccessful() 실패 또는 response.body()가 null");
                    if (response.errorBody() != null) {
                        try {
                            Log.e(TAG, "에러 메시지: " + response.errorBody().string());
                        } catch (IOException e) {
                            Log.e(TAG, "에러 메시지 읽기 실패", e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e(TAG, "네트워크 오류 또는 서버 요청 실패", t);
            }
        });
    }

    private void moveToHomeFragment() {
        Log.d(TAG, "moveToHomeFragment 호출"); // 메서드 호출 로그

        HomeFragment homeFragment = new HomeFragment();
        Log.d(TAG, "HomeFragment 객체 생성 완료"); // HomeFragment 생성 확인 로그

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        Log.d(TAG, "FragmentTransaction 생성 완료"); // FragmentTransaction 생성 확인 로그

        transaction.replace(R.id.fragment_container, homeFragment);
        Log.d(TAG, "프래그먼트 교체 완료"); // 프래그먼트 교체 확인 로그

        transaction.addToBackStack(null);
        Log.d(TAG, "백스택에 추가 완료"); // 백스택 추가 확인 로그

        transaction.commit();
        Log.d(TAG, "트랜잭션 커밋 완료"); // 커밋 완료 로그
    }
}