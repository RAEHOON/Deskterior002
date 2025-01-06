package com.example.desk0018.Adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
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
                // 로그인된 사용자의 닉네임 가져오기
                SharedPreferences sharedPreferences = context.getSharedPreferences("로그인정보", Context.MODE_PRIVATE);
                String loggedInNickname = sharedPreferences.getString("nickname", null); // 로그인된 사용자 닉네임

                Log.d(TAG, "로그인된 닉네임: " + loggedInNickname);
                Log.d(TAG, "게시글 닉네임: " + feed.getNickname());

                // 태그 추가 버튼 가시성 설정
                if (feed.getNickname() != null && feed.getNickname().equals(loggedInNickname)) {
                    feedViewHolder.addTagButton.setVisibility(View.VISIBLE);
                    Log.d(TAG, "본인의 글입니다. 태그 추가 버튼 표시");
                } else {
                    feedViewHolder.addTagButton.setVisibility(View.GONE);
                    Log.d(TAG, "본인의 글이 아닙니다. 태그 추가 버튼 숨김");
                }

                feedViewHolder.buttonMinimenu.setOnClickListener(v -> {

                    String postNickname = feed.getNickname();

                    MiniMenuDialog miniMenuDialog = new MiniMenuDialog(holder.itemView.getContext(),loggedInNickname, postNickname, new MiniMenuDialog.OnMiniMenuActionListener() {
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

                        @Override
                        public void onEditPost() {

                        }

                        @Override
                        public void onDeletePost() {

                        }
                    });
                    // MiniMenuDialog 표시
                    miniMenuDialog.show();

                });


                // 태그 추가 버튼 클릭 이벤트
                feedViewHolder.addTagButton.setOnClickListener(v -> {
                    Log.d(TAG, "addTagButton 클릭됨");
                    int scrollPosition = ((LinearLayoutManager) recyclerView_homefragment.getLayoutManager()).findFirstVisibleItemPosition();
                    Log.d(TAG, "현재 스크롤 위치: " + scrollPosition);

                    AddTagDialog dialog = new AddTagDialog(context, (name, url) -> {
                        Log.d(TAG, "AddTagDialog에서 URL 입력 완료: " + name + url);


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
                            tagList.add(new TagData(tagX, tagY, name, url)); // 태그 데이터를 배열 형식으로 리스트에 추가

                            Gson gson = new Gson(); // Gson 라이브러리 객체 생성
                            String tagsJson = gson.toJson(tagList); // 리스트를 JSON 문자열로 변환

                            Log.d(TAG, "태그 JSON 데이터 생성: " + tagsJson);

                            TagData newTag = new TagData(tagX, tagY,name, url);
                            Log.d(TAG, "태그 데이터 생성: X=" + tagX + ", Y=" + tagY);

                            // 클라이언트 캐시에 태그 추가
                            feedViewHolder.addTagToClient(clientTagLists, newTag);


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
                    });

                    dialog.show();
                    Log.d(TAG, "AddTagDialog 표시됨");
                });

            } else {
                Log.e(TAG, "onBindViewHolder: 잘못된 feedPosition. position=" + position + ", feedPosition=" + feedPosition + ", feedList.size=" + feedList.size());
            }
        }
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
        TextView tvNickname, tvFeed, tvLikeCount, tvCommentCount;
        ViewPager2 viewPagerImages;
        CircleIndicator3 indicator;
        Button btnFollow;
        ImageView addTagButton;
        ImageView buttonMinimenu;
        FrameLayout tagContainer;

        public FeedViewHolder(@NonNull View itemView) {

            //피드 뷰홀더 실행
            super(itemView);

            Log.d(TAG, "FeedViewHolder : 실행");
            imgProfile = itemView.findViewById(R.id.img_profile);
            //프로필이미지 레이아웃연결
            Log.d(TAG, "FeedViewHolder : imgProfile연결 ");
            tvNickname = itemView.findViewById(R.id.tv_nickname);
            //닉네임넥스트뷰 연결
            Log.d(TAG, "FeedViewHolder : tvNickname연결" + (tvNickname != null));
            tvFeed = itemView.findViewById(R.id.tv_feed);
            //피드설명 텍뷰연결
            Log.d(TAG, "FeedViewHolder : tvFeed 연결" + (tvFeed != null));
            tvLikeCount = itemView.findViewById(R.id.tv_likecount);
            //좋아요수 텍뷰연결
            Log.d(TAG, "FeedViewHolder : tvLikeCount 연결" + (tvLikeCount != null));
            tvCommentCount = itemView.findViewById(R.id.tv_thatgelcount);
            //댓글수 텍뷰연결
            Log.d(TAG, "FeedViewHolder : tvthatgelcount 연결" + (tvCommentCount != null));
            viewPagerImages = itemView.findViewById(R.id.viewpager_images);
            //뷰페이저2 레이아웃 연결
            Log.d(TAG, "FeedViewHolder : viewPager_images 연결");
            indicator = itemView.findViewById(R.id.indicator);
            //인디케이터 레이아웃연결
            Log.d(TAG, "FeedViewHolder : indicator 연결");
            btnFollow = itemView.findViewById(R.id.follow);
            //팔로우버튼 버튼연결
            Log.d(TAG, "FeedViewHolder : follow 연결");
            addTagButton = itemView.findViewById(R.id.btn_add_tag);
            //태그추가버튼 연결
            Log.d(TAG, "FeedViewHolder : btn_add_tag 연결");
            tagContainer = itemView.findViewById(R.id.tag_container);
            //프레임레이아웃 연결
            Log.d(TAG, "FeedViewHolder: tag_container 연결");
            buttonMinimenu = itemView.findViewById(R.id.button_minimenu);
            tagViews = new ArrayList<>();
        }

        public void bind(Feed feed, List<List<TagData>> clientTagLists) {
            Log.d(TAG, "bind: 피드 데이터를 연결하기 시작");


            // 닉네임 설정
            tvNickname.setText(feed.getNickname());
            Log.d(TAG, "bind: 닉네임 설정 완료: " + feed.getNickname());

            // 피드 내용 설정
            tvFeed.setText(feed.getFeed());
            Log.d(TAG, "bind: 피드 내용 설정 완료: " + feed.getFeed());

            // 좋아요 수 설정
            tvLikeCount.setText(String.valueOf(feed.getLikeCount()));
            Log.d(TAG, "bind: 좋아요 수 설정 완료: " + feed.getLikeCount());

            // 댓글 수 설정
            tvCommentCount.setText(String.valueOf(feed.getCommentCount()));
            Log.d(TAG, "bind: 댓글 수 설정 완료: " + feed.getCommentCount());

            // 프로필 이미지 설정
            String profileImageUrl = "http://43.203.234.99/" + feed.getProfileImage();
            Log.d(TAG, "bind: 프로필 이미지 URL: " + profileImageUrl);

            Glide.with(itemView.getContext())
                    .load(profileImageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
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
        public void addTagToClient(List<List<TagData>> clientTagLists, TagData newTag) {
            int currentPage = viewPagerImages.getCurrentItem(); // 현재 페이지 가져오기
            Log.d(TAG, "addTagToClient: 현재 페이지: " + currentPage);


            while (clientTagLists.size() <= currentPage) {
                // 클라이언트 캐시에 태그 추가
                clientTagLists.add(new ArrayList<>()); // 빈 리스트 추가
            }
            clientTagLists.get(currentPage).add(newTag);
            Log.d(TAG, "addTagToCache: 클라이언트 캐시에 태그 추가 완료");
        }


    }

}