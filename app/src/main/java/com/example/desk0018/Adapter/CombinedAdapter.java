package com.example.desk0018.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.desk0018.Comment.CommentFragment;
import com.example.desk0018.Dialog.TagEditDialog;
import com.example.desk0018.R;
import com.example.desk0018.Server.ApiService;
import com.example.desk0018.Server.RetrofitClient;
import com.example.desk0018.Server.ServerResponse;
import com.example.desk0018.Tag.ImageData;
import com.example.desk0018.Tag.TagData;
import com.example.desk0018.Tag.TagDragListener;
import com.example.desk0018.Dialog.TagListDialog;
import com.example.desk0018.Dialog.TagOptionDialog;
import com.example.desk0018.Tag.WebViewActivity;
import com.example.desk0018.Topmenu.Buttontop;
import com.example.desk0018.Users.Feed;
import com.example.desk0018.Dialog.MiniMenuDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***
 * 홈프레그먼트에 상단 버튼 리스트 와 피드를 합친 어댑터
 * 두 가지 뷰타입(ViewType)을  RecyclerView 에 넣어줌
 * 깃테스트테스트테스트
 */

public class CombinedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CombinedAdapter";
    private static final int VIEW_TYPE_TOPBTN = 0;
    // 탑버튼 쪽 숫자 지정 (포지션)
    private static final int VIEW_TYPE_FEED = 1;
    // 피드 쪽 숫자 지정 (포지션)
    private final RecyclerView recyclerView_homefragment;
    private final Context context;
    // 컨텍스트 객체생성
    private List<Buttontop> btnList1;
    // 버튼탑으로 구성된 리스트 생성
    private List<Feed> feedList;
    // 피드로 구성된 리스트 생성
    private List<List<TagData>> allTagLists = new ArrayList<>();
    private List<List<TagData>> clientTagLists = new ArrayList<>();
    public CombinedAdapter(Context context, List<Buttontop> btnList1, List<Feed> feedList, RecyclerView recyclerView_homefragment) {
        Log.d(TAG, "CombinedAdapter 초기화, context, btnList1, feedList");
        this.context = context;
        this.btnList1 = btnList1;
        this.feedList = feedList;
        this.recyclerView_homefragment = recyclerView_homefragment;
        //컨텍스트랑 리스트를 어뎁터에 넣고 객체 초기화. this 붙은게 외부것들
    }
    @Override
    public int getItemViewType(int position) {
        // 지금 쓰는 뷰타입이 어떤건지 포지션으로 확인
        Log.d(TAG, "getItemViewType : 지금 쓰는 뷰타입이 어떤건지 포지션으로 확인");
        return position == 0 ? VIEW_TYPE_TOPBTN : VIEW_TYPE_FEED;
        // 포지션 0이면 탑버튼 뷰타입, 그게 아니면 피드 뷰타입
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //리사이클러뷰에 대한 새로운 뷰 홀더 생성 메서드
        Log.d(TAG, "onCreateViewHolder : 메서드 생성");

        if (viewType == VIEW_TYPE_TOPBTN) {
            // 뷰타입이 탑버튼이라면
            Log.d(TAG, "onCreateViewHolder : 뷰타입이 탑버튼이라면(0), " + viewType);
            View view = LayoutInflater.from(context).inflate(R.layout.horizontal_list_item, parent, false);
            //레이아웃 인플레이터로 레이아웃 horizontal_list_item 이걸 view 객체에 적용
            Log.d(TAG, "onCreateViewHolder: 레이아웃 인플레이터로 레이아웃 horizontal_list_item 이걸 view 객체에 적용");
            return new BtnTopViewHolder(view);
            //위에 view 를 새로운 뷰홀더에 적용
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.feed, parent, false);
            //레이아웃 인플레이터로 피드 를 뷰에 적용
            Log.d(TAG, "onCreateViewHolder: 레이아웃 인플레이터로 피드 를 뷰에 적용");
            return new FeedViewHolder(view);
            //위 뷰를 피드뷰홀더에 적용
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // onBindViewHolder: RecyclerView의 각 아이템 뷰에 데이터를 연결
        Log.d(TAG, "onBindViewHolder 호출됨. position: " + position);

        if (holder.getItemViewType() == VIEW_TYPE_TOPBTN) {
            // ViewType이 TOP 버튼인 경우
            Log.d(TAG, "onBindViewHolder: ViewType이 TOP 버튼일 때");

            BtnTopViewHolder btnTopViewHolder = (BtnTopViewHolder) holder; // TOP 버튼 뷰홀더
            Log.d(TAG, "onBindViewHolder: BtnTopViewHolder 생성 완료");

            BtnAdaptertop btnAdaptertop = new BtnAdaptertop(context, btnList1); // 어댑터 생성
            Log.d(TAG, "onBindViewHolder: BtnAdaptertop 어댑터 생성 완료");

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            Log.d(TAG, "onBindViewHolder: LinearLayoutManager 가로 레이아웃 생성 완료");

            btnTopViewHolder.recyclerView.setLayoutManager(layoutManager);
            Log.d(TAG, "onBindViewHolder: RecyclerView에 LayoutManager 연결");

            btnTopViewHolder.recyclerView.setAdapter(btnAdaptertop);
            Log.d(TAG, "onBindViewHolder: RecyclerView에 Adapter 연결");

        } else {
            // 피드 아이템 처리
            int feedPosition = position - 1; // TOP 버튼 뷰 하나를 제외한 위치
            Log.d(TAG, "onBindViewHolder: feedPosition 계산 완료. feedPosition: " + feedPosition);

            if (feedPosition >= 0 && feedPosition < feedList.size()) {
                FeedViewHolder feedViewHolder = (FeedViewHolder) holder; // Feed 뷰홀더 생성
                Log.d(TAG, "onBindViewHolder: FeedViewHolder 생성 완료");
                Feed feed = feedList.get(feedPosition); // 피드 데이터 가져오기
                Log.d(TAG, "onBindViewHolder: feed 데이터 가져오기 완료. feedPosition: " + feedPosition);
                feedViewHolder.bind(feed, clientTagLists); // 피드 데이터를 뷰에 연결
                Log.d(TAG, "onBindViewHolder: feedViewHolder에 데이터 바인딩 완료");
                // 버튼 클릭 이벤트 추가

                // btn_thatgel 객체화 및 클릭 이벤트 추가
                feedViewHolder.btnThatgel.setOnClickListener(v -> {
                    openCommentFragment(feed.getFeedCount());
                });


                // 로그인된 사용자의 닉네임 가져오기
                SharedPreferences sharedPreferences = context.getSharedPreferences("로그인정보", Context.MODE_PRIVATE);
                String loggedInNickname = sharedPreferences.getString("nickname", null); // 로그인된 사용자 닉네임
                Log.d(TAG, "로그인된 닉네임: " + loggedInNickname);
                Log.d(TAG, "게시글 닉네임: " + feed.getNickname());
                // 태그 추가 버튼 가시성 설정
                if (feed.getNickname() != null && feed.getNickname().equals(loggedInNickname)) {
                    feedViewHolder.buttonMinimenu.setVisibility(View.VISIBLE);
                    Log.d(TAG, "본인의 글입니다. 태그 추가 버튼 표시");
                } else {
                    feedViewHolder.buttonMinimenu.setVisibility(View.GONE);
                    Log.d(TAG, "본인의 글이 아닙니다. 태그 추가 버튼 숨김");
                }

                feedViewHolder.buttonKinchat.setOnClickListener(v -> {
                    String userId = sharedPreferences.getString("user_id", null);
                    if (userId == null) {
                        Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int feedCount = feed.getFeedCount();
                    boolean isFavorited = feed.isFavorited();

                    // UI 즉각 업데이트: 즐겨찾기 상태 및 이미지 변경
                    feed.setFavorited(!isFavorited);
                    feedViewHolder.buttonKinchat.setImageResource(!isFavorited ? R.drawable.kinchat222 : R.drawable.kinchat);

                    ApiService apiService = RetrofitClient.getApi();
                    Call<ServerResponse> call = apiService.updateFavorite(userId, feedCount);

                    // 서버 통신 시작 (UI는 이미 변경된 상태)
                    call.enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                Log.d(TAG, "즐겨찾기 상태 서버 업데이트 성공");
                            } else {
                                // 서버 실패 시 UI 상태 복구
                                feed.setFavorited(isFavorited);
                                feedViewHolder.buttonKinchat.setImageResource(isFavorited ? R.drawable.kinchat222 : R.drawable.kinchat);
                                Log.e(TAG, "즐겨찾기 상태 서버 업데이트 실패");
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                            // 서버 연결 실패 시 UI 상태 복구
                            feed.setFavorited(isFavorited);
                            feedViewHolder.buttonKinchat.setImageResource(isFavorited ? R.drawable.kinchat222 : R.drawable.kinchat);
                            Log.e(TAG, "서버 연결 실패", t);
                        }
                    });
                });



                feedViewHolder.btnLike.setOnClickListener(v -> {
                    String userId = sharedPreferences.getString("user_id", null);
                    if (userId == null) {
                        Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int feedCount = feed.getFeedCount();
                    boolean isLiked = feed.isLiked();
                    feed.setLiked(!isLiked);
                    feed.setLikeCount(feed.getLikeCount() + (isLiked ? -1 : 1));
                    ApiService apiService = RetrofitClient.getApi();
                    Call<ServerResponse> call = apiService.updateLikeStatus(userId, feedCount);
                    call.enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                notifyItemChanged(feedPosition + 1);
                                Log.d(TAG, "좋아요 상태 업데이트 성공");
                            } else {
                                feed.setLiked(isLiked); // 원래 상태로 복구
                                feed.setLikeCount(feed.getLikeCount() - (isLiked ? -1 : 1));
                                notifyItemChanged(feedPosition + 1);
                                Log.e(TAG, "좋아요 상태 업데이트 실패");
                            }
                        }
                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                            feed.setLiked(isLiked); // 원래 상태로 복구
                            feed.setLikeCount(feed.getLikeCount() - (isLiked ? -1 : 1));
                            notifyItemChanged(feedPosition + 1);
                            Log.e(TAG, "서버 연결 실패", t);
                        }
                    });
                });
                feedViewHolder.buttonMinimenu.setOnClickListener(v -> {
                    String postNickname = feed.getNickname();
                    MiniMenuDialog miniMenuDialog = new MiniMenuDialog(holder.itemView.getContext(),loggedInNickname, postNickname, new MiniMenuDialog.OnMiniMenuActionListener() {

                        @Override
                        public void onEditPost() {
                            // EditText 생성 및 초기화
                            EditText input = new EditText(context);
                            Log.d(TAG, "EditText 생성 완료");
                            input.setText(feed.getCaption()); // 기존 캡션 설정
                            Log.d(TAG, "EditText에 기존 캡션 설정: " + feed.getCaption());
                            // 현재 피드의 고유 ID 가져오기
                            int feedCount = feed.getFeedCount();
                            Log.d(TAG, "현재 피드 feedCount 가져오기: " + feedCount);
                            // 현재 스크롤 위치 가져오기 (스크롤 고정을 위해)
                            int scrollPosition = ((LinearLayoutManager) recyclerView_homefragment.getLayoutManager()).findFirstVisibleItemPosition();
                            Log.d(TAG, "현재 스크롤 위치: " + scrollPosition);
                            // AlertDialog 생성 및 설정
                            new AlertDialog.Builder(context)
                                    .setTitle("게시글 수정")
                                    .setMessage("새로운 내용을 입력하세요:")
                                    .setView(input)
                                    .setPositiveButton("수정완료", (dialog, which) -> {
                                        // 수정 버튼 클릭 시 동작
                                        String newCaption = input.getText().toString();
                                        Log.d(TAG, "사용자가 입력한 새로운 캡션: " + newCaption);
                                        if (!newCaption.isEmpty()) {
                                            // Retrofit 객체 생성
                                            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                                            Log.d(TAG, "ApiService 객체 생성 완료");
                                            // API 호출 준비
                                            Call<ServerResponse> call = apiService.editFeed(feed.getFeedCount(), newCaption);
                                            Log.d(TAG, "editFeed API 호출 준비 완료: feedCount=" + feed.getFeedCount() + ", newCaption=" + newCaption);
                                            // API 호출 실행
                                            call.enqueue(new Callback<ServerResponse>() {
                                                @Override
                                                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                                    Log.d(TAG, "API 응답 수신: " + response.toString());
                                                    if (response.isSuccessful() && response.body() != null) {
                                                        Log.d(TAG, "API 응답 성공: " + response.body().toString());
                                                        if (response.body().isSuccess()) {
                                                            // 로컬 데이터 업데이트
                                                            feed.setCaption(newCaption);
                                                            Log.d(TAG, "로컬 피드 캡션 업데이트 완료: " + newCaption);
                                                            // RecyclerView 갱신 (스크롤 위치 복원 포함)
                                                            recyclerView_homefragment.post(() -> {
                                                                notifyItemChanged(feedPosition + 1);
                                                                recyclerView_homefragment.scrollToPosition(scrollPosition);
                                                                Log.d(TAG, "RecyclerView 갱신 및 스크롤 위치 유지 완료: " + scrollPosition);
                                                            });
                                                            Toast.makeText(context, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Log.e(TAG, "게시글 수정 실패: " + response.body().getMessage());
                                                            Toast.makeText(context, "게시글 수정 실패: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Log.e(TAG, "response.isSuccessful() 실패 또는 response.body()가 null");
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<ServerResponse> call, Throwable t) {
                                                    Log.e(TAG, "네트워크 오류 발생: " + t.getMessage(), t);
                                                    Toast.makeText(context, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            Log.d(TAG, "사용자가 입력한 캡션이 비어있음");
                                        }
                                    })
                                    .setNegativeButton("취소", (dialog, which) -> Log.d(TAG, "AlertDialog 취소 버튼 클릭"))
                                    .show();
                            Log.d(TAG, "AlertDialog 표시 완료");
                        }
                        @Override
                        public void onDeletePost() {
                            int feedCount = feed.getFeedCount(); // 해당 피드의 feed_count
                            // Retrofit API 호출
                            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                            Call<ServerResponse> call = apiService.deleteFeed(feedCount);
                            call.enqueue(new Callback<ServerResponse>() {
                                @Override
                                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        if (response.body().isSuccess()) {
                                            Toast.makeText(context, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                            // RecyclerView 데이터 갱신
                                            feedList.remove(feedPosition); // 해당 게시글을 리스트에서 제거
                                            notifyItemRemoved(feedPosition + 1); // RecyclerView 갱신 (TOP 버튼 제외)
                                        } else {
                                            Toast.makeText(context, "게시글 삭제 실패: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, "서버 응답 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<ServerResponse> call, Throwable t) {
                                    Toast.makeText(context, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    // MiniMenuDialog 표시
                    miniMenuDialog.show();
                });
                feedViewHolder.addTagButton.setOnClickListener(v -> {
                    Log.d(TAG, "addTagButton 클릭됨");
                    TagOptionDialog dialog = new TagOptionDialog(context, feed, feedViewHolder, new TagOptionDialog.OnTagActionListener() {
                        @Override
                        public void onAddTag(Feed feed, FeedViewHolder feedViewHolder) {

                            Log.d(TAG, "showAddTagDialog 호출됨");
                            String[] categories = {"키보드", "마우스, 패드", "모니터", "스피커", "조명", "케이스", "테이블", "선정리", "기타"};
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categories);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            Spinner spinner = new Spinner(context);
                            spinner.setAdapter(adapter);
                            Log.d(TAG, "Spinner 생성 및 유형 목록 추가 완료");
                            // AlertDialog로 유형, 이름, URL 입력
                            LinearLayout dialogLayout = new LinearLayout(context);
                            dialogLayout.setOrientation(LinearLayout.VERTICAL);

                            TextView categoryLabel = new TextView(context);
                            categoryLabel.setText("유형 선택:");
                            dialogLayout.addView(categoryLabel);
                            dialogLayout.addView(spinner);
                            EditText nameInput = new EditText(context);
                            nameInput.setHint("태그 이름 입력");
                            dialogLayout.addView(nameInput);
                            EditText urlInput = new EditText(context);
                            urlInput.setHint("URL 입력");
                            dialogLayout.addView(urlInput);
                            new AlertDialog.Builder(context)
                                    .setTitle("태그 추가")
                                    .setView(dialogLayout)
                                    .setPositiveButton("확인", (dialog, which) -> {
                                        String name = nameInput.getText().toString();
                                        String url = urlInput.getText().toString();
                                        String selectedCategory = spinner.getSelectedItem().toString();

                                        Log.d(TAG, "AddTagDialog 입력 완료: Name=" + name + ", URL=" + url + ", Category=" + selectedCategory);
                                        SharedPreferences sharedPreferences = context.getSharedPreferences("로그인정보", Context.MODE_PRIVATE);
                                        String userId = sharedPreferences.getString("user_id", null); // 로그인 정보 가져오기
                                        Log.d(TAG, "SharedPreferences에서 user_id 가져오기: " + userId);

                                        int feedCount = feed.getFeedCount(); // 현재 피드의 고유 ID
                                        Log.d(TAG, "현재 피드 feedCount 가져오기: " + feedCount);

                                        // 현재 선택된 이미지 카운트 가져오기
                                        final int currentPage = feedViewHolder.viewPagerImages.getCurrentItem(); // ViewPager2의 현재 페이지
                                        final int imgCount;
                                        if (feed.getImageList() != null && currentPage < feed.getImageList().size()) {
                                            imgCount = feed.getImageList().get(currentPage).getImageCount(); // 현재 이미지의 imageCount 가져오기
                                            Log.d(TAG, "현재 이미지의 imageCount 가져오기: " + imgCount);
                                        } else {
                                            imgCount = -1; // 기본값
                                            Log.d(TAG, "ImageList가 비어있거나 유효하지 않습니다. 기본값 설정: " + imgCount);
                                        }
                                        // 태그 아이콘 생성
                                        ImageView tag = new ImageView(context);
                                        tag.setImageResource(R.drawable.plusssscircle);
                                        Log.d(TAG, "태그 ImageView 생성 및 리소스 설정");
                                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(40, 40);
                                        params.leftMargin = 100;
                                        params.topMargin = 100;
                                        tag.setLayoutParams(params);
                                        feedViewHolder.tagContainer.addView(tag);
                                        Log.d(TAG, "태그를 tagContainer에 추가");
                                        // 드래그 리스너 설정
                                        tag.setOnTouchListener(new TagDragListener());
                                        Log.d(TAG, "태그에 드래그 리스너 설정");
                                        // 완료 버튼 생성
                                        ImageView doneButton = new ImageView(context);
                                        doneButton.setImageResource(R.drawable.save);
                                        FrameLayout.LayoutParams doneParams = new FrameLayout.LayoutParams(200, 200);
                                        doneParams.gravity = Gravity.BOTTOM | Gravity.END;
                                        doneParams.bottomMargin = 20;
                                        doneParams.rightMargin = 20;
                                        feedViewHolder.tagContainer.addView(doneButton, doneParams);
                                        Log.d(TAG, "완료 버튼 생성 및 tagContainer에 추가");
                                        // 완료 버튼 클릭 이벤트
                                        doneButton.setOnClickListener(done -> {
                                            Log.d(TAG, "완료 버튼 클릭됨");
                                            doneButton.setVisibility(View.GONE); // 완료 버튼 숨기기
                                            tag.setVisibility(View.GONE); // 기존 태그 버튼 숨기기
                                            tag.setOnTouchListener(null); // 드래그 비활성화
                                            Log.d(TAG, "태그 드래그 리스너 비활성화");
                                            int tagX = (int) tag.getX(); // 태그 X좌표
                                            int tagY = (int) tag.getY(); // 태그 Y좌표
                                            Log.d(TAG, "태그 좌표 가져오기. X: " + tagX + ", Y: " + tagY);
                                            List<TagData> tagList = new ArrayList<>(); // TagData 객체를 저장할 리스트 생성
                                            tagList.add(new TagData(selectedCategory, tagX, tagY, name, url)); // 범주 포함 태그 데이터 추가
                                            Gson gson = new Gson(); // Gson 라이브러리 객체 생성
                                            String tagsJson = gson.toJson(tagList); // 리스트를 JSON 문자열로 변환
                                            Log.d(TAG, "태그 JSON 데이터 생성: " + tagsJson);
                                            // API 호출로 태그 정보 전송
                                            ApiService apiService = RetrofitClient.getApi();
                                            Call<ServerResponse> call = apiService.uploadTags(userId, feedCount, imgCount, tagsJson);
                                            Log.d(TAG, "API 호출 준비: userId=" + userId + ", feedCount=" + feedCount + ", imgCount=" + imgCount + ", tagsJson=" + tagsJson);

                                            call.enqueue(new Callback<ServerResponse>() {
                                                @Override
                                                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                                    if (response.isSuccessful() && response.body() != null) {
                                                        Log.d(TAG, "API 응답 성공: " + response.body().getMessage());
                                                        Toast.makeText(context, "태그 저장 성공", Toast.LENGTH_SHORT).show();
                                                        updateTagsFromServer(feedCount, imgCount, feedViewHolder);
                                                        // 저장 성공 시 서버에서 최신 태그 데이터를 다시 불러와서 업데이트
                                                    } else {
                                                        Log.e(TAG, "API 응답 실패");
                                                        Toast.makeText(context, "태그 저장 실패", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<ServerResponse> call, Throwable t) {
                                                    Log.e(TAG, "API 호출 실패", t);
                                                    Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        });
                                        // 태그 클릭 이벤트 (웹뷰로 이동)
                                        tag.setOnClickListener(t -> {
                                            Log.d(TAG, "태그 클릭됨: URL 이동 - " + url);
                                            Intent intent = new Intent(context, WebViewActivity.class);
                                            intent.putExtra("url", url);
                                            context.startActivity(intent);
                                        });
                                    })
                                    .setNegativeButton("취소", null)
                                    .show();
                            Log.d(TAG, "AddTagDialog 표시됨");
                        }
                        @Override
                        public void onDeleteTag(Feed feed) {
                            Log.d(TAG, "태그 삭제 선택됨");
                            int feedCount = feed.getFeedCount(); // 해당 피드의 feed_count
                            int imageCount = -1; // 초기값 설정
                            if (feed.getImageList() != null) {
                                // ViewPager2의 현재 페이지 위치에서 이미지 카운트 가져오기2
                                int currentImagePosition = feedViewHolder.viewPagerImages.getCurrentItem();
                                Log.d(TAG, "현재 ViewPager 이미지 position: " + currentImagePosition);
                                if (currentImagePosition < feed.getImageList().size()) {
                                    imageCount = feed.getImageList().get(currentImagePosition).getImageCount();
                                }
                            }
                            // TagEditDialog를 띄움
                            TagEditDialog dialog = new TagEditDialog(holder.itemView.getContext(), feedCount, imageCount);
                            dialog.show();
                        }

                        @Override
                        public void onShowTags() {
                            int feedCount = feed.getFeedCount(); // 해당 피드의 feed_count
                            int imageCount = -1; // 초기값 설정
                            if (feed.getImageList() != null) {
                                // ViewPager2의 현재 페이지 위치에서 이미지 카운트 가져오기2
                                int currentImagePosition = feedViewHolder.viewPagerImages.getCurrentItem();
                                Log.d(TAG, "현재 ViewPager 이미지 position: " + currentImagePosition);
                                if (currentImagePosition < feed.getImageList().size()) {
                                    imageCount = feed.getImageList().get(currentImagePosition).getImageCount();
                                }
                            }
                            // TagListDialog를 띄움
                            TagListDialog dialog = new TagListDialog(holder.itemView.getContext(), feedCount, imageCount);
                            dialog.show();
                        }
                    });
                    dialog.show();
                    Log.d(TAG, "TagOptionsDialog 표시됨");
                });
            } else {
                Log.e(TAG, "onBindViewHolder: 잘못된 feedPosition. position=" + position + ", feedPosition=" + feedPosition + ", feedList.size=" + feedList.size());
            }
        }
    }

    private void openCommentFragment(int feedCount) {
        AppCompatActivity activity = (AppCompatActivity) context;
        CommentFragment commentFragment = new CommentFragment();

        // 피드 ID 전달을 위한 번들 생성
        Bundle args = new Bundle();
        args.putInt("feed_count", feedCount);
        commentFragment.setArguments(args);

        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, commentFragment)
                .addToBackStack(null)
                .commit();
    }
    private void updateTagsFromServer(int feedCount, int imgCount, FeedViewHolder holder) {
        ApiService apiService = RetrofitClient.getApi();
        Call<List<TagData>> call = apiService.getTags(feedCount, imgCount); // 서버에서 태그 불러오기 API
        call.enqueue(new Callback<List<TagData>>() {
            @Override
            public void onResponse(Call<List<TagData>> call, Response<List<TagData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "updateTagsFromServer: 서버에서 태그 불러오기 성공");
                    List<TagData> updatedTags = response.body();
                    int currentPage = holder.viewPagerImages.getCurrentItem();
                    while (allTagLists.size() <= currentPage) {
                        allTagLists.add(new ArrayList<>()); // 빈 리스트 추가
                    }
                    // 태그 업데이트
                    holder.updateTags(updatedTags);
                    Log.d(TAG, "updateTagsFromServer: 태그 업데이트 완료");
                } else {
                    Log.e(TAG, "updateTagsFromServer: 서버 응답 실패");
                }
            }
            @Override
            public void onFailure(Call<List<TagData>> call, Throwable t) {
                Log.e(TAG, "updateTagsFromServer: 서버 호출 실패", t);
            }
        });
    }
    @Override
    public int getItemCount() {
        //크기를 가져온다
        int size = feedList.size() + 1;
        Log.d(TAG, "getItemCount 호출: 총 아이템 수=" + size);
        return size;
        //리스트 크기를 가져온다 + 1 (리스트 한개더 )
    }
    public static class BtnTopViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        public BtnTopViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view_horizontal);
            // 버튼탑뷰홀더 리사이클러뷰에 리사이클러뷰 레이아웃 연결
            Log.d(TAG, "BtnTopViewHolder: 리사이클러뷰에 리사이클러뷰 레이아웃 연결");
        }
    }
    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        //내부 클래스 피드뷰홀더
        private List<View> tagViews;
        ImageView imgProfile;
        TextView tvNickname, tvFeed, tvLikeCount, tvCommentEa;
        ViewPager2 viewPagerImages;
        CircleIndicator3 indicator;
        ImageView btnLike;
        ImageView buttonKinchat;
        ImageView btnFollow;
        ImageView addTagButton;
        ImageView buttonMinimenu;
        ImageView btnThatgel;
        FrameLayout tagContainer;
        public FeedViewHolder(@NonNull View itemView) {

            //피드 뷰홀더 실행
            super(itemView);
            Log.d(TAG, "FeedViewHolder : 실행");
            imgProfile = itemView.findViewById(R.id.img_profile);
            //프로필이미지 레이아웃연결
            Log.d(TAG, "FeedViewHolder : imgProfile연결 ");
            btnLike = itemView.findViewById(R.id.btn_like);
            buttonKinchat = itemView.findViewById(R.id.btn_kinchat);
            tvNickname = itemView.findViewById(R.id.tv_nickname);
            //닉네임넥스트뷰 연결
            Log.d(TAG, "FeedViewHolder : tvNickname연결" + (tvNickname != null));
            tvFeed = itemView.findViewById(R.id.tv_feed);
            //피드설명 텍뷰연결
            Log.d(TAG, "FeedViewHolder : tvFeed 연결" + (tvFeed != null));
            tvLikeCount = itemView.findViewById(R.id.tv_likecount);
            //좋아요수 텍뷰연결
            Log.d(TAG, "FeedViewHolder : tvLikeCount 연결" + (tvLikeCount != null));
            tvCommentEa = itemView.findViewById(R.id.tv_thatgelcount);
            //댓글수 텍뷰연결
            Log.d(TAG, "FeedViewHolder : tvthatgelcount 연결" + (tvCommentEa != null));
            viewPagerImages = itemView.findViewById(R.id.viewpager_images);
            //뷰페이저2 레이아웃 연결
            Log.d(TAG, "FeedViewHolder : viewPager_images 연결");
            indicator = itemView.findViewById(R.id.indicator);
            //인디케이터 레이아웃연결
            Log.d(TAG, "FeedViewHolder : indicator 연결");
            btnFollow = itemView.findViewById(R.id.button_follow);
            //팔로우버튼 버튼연결
            Log.d(TAG, "FeedViewHolder : follow 연결");
            addTagButton = itemView.findViewById(R.id.btn_add_tag);
            //태그추가버튼 연결
            Log.d(TAG, "FeedViewHolder : btn_add_tag 연결");
            tagContainer = itemView.findViewById(R.id.tag_container);
            //프레임레이아웃 연결
            Log.d(TAG, "FeedViewHolder: tag_container 연결");
            buttonMinimenu = itemView.findViewById(R.id.button_minimenu);

            btnThatgel = itemView.findViewById(R.id.btn_thatgel);
            tagViews = new ArrayList<>();
        }

        public void bind(Feed feed, List<List<TagData>> clientTagLists) {
            Log.d(TAG, "bind: 피드 데이터를 연결하기 시작");
            // 닉네임 설정
            tvNickname.setText(feed.getNickname());
            Log.d(TAG, "bind: 닉네임 설정 완료: " + feed.getNickname());
            // 피드 내용 설정
            tvFeed.setText(feed.getCaption());
            Log.d(TAG, "bind: 피드 내용 설정 완료: " + feed.getCaption());
            // 좋아요 수 설정 및 좋아요 상태 반영
            if (feed.isLiked()) {
                btnLike.setImageResource(R.drawable.heartfill); // 좋아요 이미지
            } else {
                btnLike.setImageResource(R.drawable.heart); // 기본 이미지
            }
            tvLikeCount.setText(String.valueOf(feed.getLikeCount()));
            Log.d(TAG, "bind: 좋아요 상태 및 좋아요 수 설정 완료"+ "좋아요상태 = " + feed.isLiked() + "좋아요 수 = " + feed.getLikeCount());

            // **즐겨찾기 상태 반영**
            if (feed.isFavorited()) {
                buttonKinchat.setImageResource(R.drawable.kinchat222); // 즐겨찾기된 상태
            } else {
                buttonKinchat.setImageResource(R.drawable.kinchat); // 기본 상태
            }
            Log.d(TAG, "bind: 즐겨찾기 상태 설정 완료: " + feed.isFavorited());

            // 댓글 수 설정
            tvCommentEa.setText(String.valueOf(feed.getCommentEa()));
            Log.d(TAG, "bind: 댓글 수 설정 완료: " + feed.getCommentEa());
            // 프로필 이미지 설정
            String profileImageUrl = "http://43.203.234.99/" + feed.getProfileImage();
            Log.d(TAG, "bind: 프로필 이미지 URL: " + profileImageUrl);
            Glide.with(itemView.getContext())
                    .load(profileImageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgProfile);
            Log.d(TAG, "bind: Glide로 프로필 이미지 로드 완료");
            // 이미지 URL과 태그 리스트 초기화
            List<String> imageUrlList = new ArrayList<>();
            List<List<TagData>> allTagLists = new ArrayList<>();
            Log.d(TAG, "bind: 이미지 URL과 태그 리스트 초기화 완료");
            // imageList에서 이미지 URL과 태그 데이터 추출
            for (ImageData imageData : feed.getImageList()) {
                String fullImageUrl = "http://43.203.234.99/" + imageData.getImageUrl();
                Log.d(TAG, "bind: 이미지 URL 추가: " + fullImageUrl);
                imageUrlList.add(fullImageUrl);
                // 태그 데이터 확인
                List<TagData> tags = imageData.getTags();
                if (tags != null) {
                    Log.d(TAG, "bind: 태그 리스트 추가 (크기: " + tags.size() + ")");
                } else {
                    Log.d(TAG, "bind: 태그 리스트가 NULL입니다.");
                }
                allTagLists.add(tags);
            }
            // ImagePagerAdapter 설정
            ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(itemView.getContext(), imageUrlList);
            viewPagerImages.setAdapter(imagePagerAdapter);
            Log.d(TAG, "bind: ViewPager2에 ImagePagerAdapter 연결 완료");
            // 인디케이터 설정
            indicator.setViewPager(viewPagerImages);
            Log.d(TAG, "bind: 인디케이터에 ViewPager2 연결 완료");

            // 페이지 번호 설정
            TextView tvPageNumber = itemView.findViewById(R.id.tv_page_number);
            int totalPages = imageUrlList.size();
            tvPageNumber.setText("1/" + totalPages);
            Log.d(TAG, "bind: 초기 페이지 번호 설정 완료, 총 페이지 수: " + totalPages+ "지금 페이지 번호 = " + tvPageNumber.getText().toString());


            while (clientTagLists.size() < feed.getImageList().size()) {
                clientTagLists.add(new ArrayList<>());
            }


            viewPagerImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                // 페이지 변경 시 태그 업데이트
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    Log.d(TAG, "onPageSelected: 페이지 변경, position = " + position);


                    int currentPageNumber = position + 1; // ViewPager2는 0부터 시작하므로 +1
                    tvPageNumber.setText(currentPageNumber + "/" + totalPages);
                    // 페이지 번호 업데이트
                    Log.d(TAG, "onPageSelected: 페이지 번호 업데이트 완료, 현재 페이지 = " + currentPageNumber);


                    int imgCount = -1;
                    if (feed.getImageList() != null && position < feed.getImageList().size()) {
                        imgCount = feed.getImageList().get(position).getImageCount();
                        // 현재 선택된 이미지의 이미지 카운트 가져오기
                        Log.d(TAG, "onPageSelected: 현재 이미지의 imageCount 가져오기: " + imgCount);
                    }

                    List<TagData> combinedTags = new ArrayList<>();
                    //combinedTags 리스트 초기화

                    if(allTagLists.size() > position && allTagLists.get(position) != null){
                        combinedTags.addAll(allTagLists.get(position));
                    }
                    //allTagList에 서버에서 가져올 태그 리스트를 확인해서 병합할 리스트에 추가함
                    Log.d(TAG, "onPageSelected: 태그리스트 병합할 리스트에 추가하기 " + combinedTags);

                    if (clientTagLists.size() > position && clientTagLists.get(position) != null) {
                        combinedTags.addAll(clientTagLists.get(position));
                    }
                    // 클라이언트에 저장된 리스트가 존재한다면 클라이언트에 저장된 태그리스트를 확인하고 병합할 리스트에 추가함
                    Log.d(TAG, "onPageSelected: 클라이언트 태그 데이터 추가 완료" + combinedTags);


                    removeExistingTags();
                    updateTags(combinedTags);
                    // 태그 업데이트
                    Log.d(TAG, "onPageSelected: 새 이미지 태그 업데이트 완료, imgCount = " + imgCount);
                }
            });
            if (!allTagLists.isEmpty()) {
                updateTags(allTagLists.get(0));
            }

        }


        private void updateTags(List<TagData> tagList) {
            // 태그를 업데이트하는 메서드
            Log.d(TAG, "updateTags: 태그 업데이트 시작");

            removeExistingTags();

            if (tagList != null && !tagList.isEmpty()) {
                Log.d(TAG, "updateTags: 태그 리스트 크기: " + tagList.size());
                for (TagData tag : tagList) {
                    Log.d(TAG, "updateTags: 태그 추가 - X: " + tag.getX() + ", Y: " + tag.getY() + ", URL: " + tag.getUrl());

                    ImageView savedTag = new ImageView(itemView.getContext());
                    savedTag.setImageResource(R.drawable.plusssscircle);
                    FrameLayout.LayoutParams tagParams = new FrameLayout.LayoutParams(40, 40);
                    tagParams.leftMargin = tag.getX();
                    tagParams.topMargin = tag.getY();
                    savedTag.setLayoutParams(tagParams);

                    savedTag.setOnClickListener(v -> {
                        Log.d(TAG, "태그 클릭됨: URL 이동: " + tag.getUrl());
                        Intent intent = new Intent(itemView.getContext(), WebViewActivity.class);
                        intent.putExtra("url", tag.getUrl());
                        itemView.getContext().startActivity(intent);
                    });

                    tagContainer.addView(savedTag);
                    Log.d(TAG, "updateTags: 태그 뷰 추가 완료");
                    tagViews.add(savedTag);
                }
            } else {
                Log.d(TAG, "updateTags: 태그 리스트가 비어 있습니다.");
            }
            Log.d(TAG, "updateTags: 태그 업데이트 완료");
        }
        private void removeExistingTags() {
            Log.d(TAG, "removeExistingTags: 기존 태그 뷰 삭제 시작");
            for (View tagView : tagViews) {
                tagContainer.removeView(tagView);
            }
            tagViews.clear();
            Log.d(TAG, "removeExistingTags: 기존 태그 뷰 삭제 완료");
        }


    }
    public void updateFeedList(List<Feed> newFeedList) {
        feedList.clear(); // 기존 데이터 초기화
        feedList.addAll(newFeedList); // 새로운 데이터 추가
        notifyDataSetChanged(); // 어댑터 갱신
        Log.d(TAG, "CombinedAdapter 데이터 업데이트 완료, 새 리스트 크기: " + feedList.size());
    }

}