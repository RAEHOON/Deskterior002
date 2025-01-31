package com.example.desk0018.Server;


import com.example.desk0018.Comment.Comment;
import com.example.desk0018.Tag.TagData;
import com.example.desk0018.Users.Feed;
import com.example.desk0018.Users.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    // API 호출을 정의하는 인터페이스

    @Multipart
    @POST("register.php")
    Call<RegisterResponse> registerUserWithImage(
            //회원가입api전송 이미지포함
            @Part("user_id") RequestBody email,
            //유저아이디 전송 (이메일)
            @Part("nickname") RequestBody nickname,
            //닉네임을 서버에 전송
            @Part("password") RequestBody password,
            //비밀번호를 서버에 전송
            @Part MultipartBody.Part profileImage
            //프로필 이미지를 멀티파트 형식으로 서버에 전송
    );

    @POST("check_nickname.php")
    Call<NicknameCheckResponse> checkNickname(@Body NicknameRequest nicknameRequest);
    //닉네임 중복 확인 API 호출 (JSON 형식으로 닉네임 데이터 전송)

    @POST("Login.php")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
    // 사용자 로그인 API 호출 (JSON 형식으로 로그인 데이터 전송)

    @GET("get_user_profile.php")
    Call<UserProfileResponse> getUserProfile(@Query("user_id") String userId);
    //프로필 가져오기 API 호출
    //user_id , userID 로 정보받음

    @Multipart
    @POST("upload_feed.php")
    Call<ServerResponse> uploadFeed(
            //업로드 API 호출
            @Part("user_id") RequestBody userId,
            //유저아이디 전송 (유저아이디)
            @Part("nickname") RequestBody nickname,
            //닉네임 전송 (닉네임)
            @Part("profile_image") RequestBody profileImage,
            // 프로필 이미지 서버에 전송
            @Part("caption") RequestBody caption,
            //피드 설명을 서버에 전송
            @Part List<MultipartBody.Part> images
            //업로드할 이미지 파일을 멀티파트 형식으로 서버에 전송
    );

    @GET("get_feeds.php")
    Call<List<Feed>> getFeeds(@Query("user_id") String userId);
    //피드 데이터를 가져오는 API 호출
    // 겟피드로

    @FormUrlEncoded
    @POST("upload_tag.php")
    Call<ServerResponse> uploadTags(
            // 태그 업로드 API 호출
            @Field("user_id") String userId,
            // 유저 아이디를 서버에 전송
            @Field("feed_count") int feedCount,
            // 피드 카운트를 서버에 전송
            @Field("image_count") int imageCount,
            @Field("tags_json") String tagsJson
            // 태그 데이터를 JSON 문자열로 전송

    );
    @GET("get_tags.php")
    Call<List<TagData>> getTags(
            @Query("feed_count") int feedCount,
            @Query("image_count") int imageCount
    );

    @FormUrlEncoded
    @POST("delete_feed.php")
    Call<ServerResponse> deleteFeed(@Field("feed_count") int feedCount);

    @FormUrlEncoded
    @POST("edit_feed.php")
    Call<ServerResponse> editFeed(@Field("feed_count") int feedCount, @Field("caption") String newCaption);

    @POST("update_like_status.php")
    @FormUrlEncoded
    Call<ServerResponse> updateLikeStatus(
            @Field("user_id") String userId,
            @Field("feed_count") int feedCount
    );

    @GET("get_feeds_by_tag.php")
    Call<List<Feed>> getFeedsByTag(
            @Query("user_id") String userId,
            @Query("tag") String tag
    );

    @GET("search_by_nickname.php")
    Call<List<User>> searchByNickname(@Query("query") String query);

    @GET("search_by_tag_name.php")
    Call<List<TagData>> searchByTagName(@Query("query") String query);

    @GET("get_feeds_by_user.php")
    Call<List<Feed>> getFeedsByNickname(@Query("nickname") String nickname);

    @FormUrlEncoded
    @POST("update_favorite.php")
    Call<ServerResponse> updateFavorite(
            @Field("user_id") String userId,
            @Field("feed_count") int feedCount
    );

    @GET("get_feeds_by_favorite.php")
    Call<List<Feed>> getFavoriteFeedsByNickname(@Query("nickname") String nickname);

    @POST("delete_tag.php")
    @FormUrlEncoded
    Call<ServerResponse> deleteTag(
            @Field("image_count") int imageCount,
            @Field("tag_name") String tagName
    );

    @POST("edit_tag.php")
    @FormUrlEncoded
    Call<ServerResponse> editTag(
            @Field("image_count") int imageCount,
            @Field("category") String category,
            @Field("name") String name,
            @Field("url") String url,
            @Field("x") int x,
            @Field("y") int y
    );

    @GET("get_comments.php")
    Call<CommentResponse> getComments(@Query("feed_count") int feedCount);


    @FormUrlEncoded
    @POST("add_comment.php")
    Call<ServerResponse> addComment(
            @Field("feed_count") int feedCount,
            @Field("user_id") String userId,
            @Field("comment") String comment
    );








}