package com.example.desk0018.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.desk0018.R;
import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder> {
    // 이미지 페이저 어댑터 클래스 생성 (리사이클러뷰 어댑터 상속)
    private Context context;
    // 컨텍스트 객체 생성
    private List<String> imageList;
    // 이미지 리스트 생성
    public ImagePagerAdapter(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList != null ? imageList : new ArrayList<>();
        // 컨텍스트랑 리스트 초기화. 리스트가 null이면 빈 리스트로 설정
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 리사이클러뷰에서 새로운 뷰홀더를 생성하는 메서드
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        // 레이아웃 인플레이터로 image_item 레이아웃을 view 객체에 적용
        return new ImageViewHolder(view);
        // 위에서 만든 view를 새로운 ImageViewHolder에 적용해서 반환
    }
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // 뷰홀더에 데이터를 바인딩하는 메서드
        String imageUrl = imageList.get(position);
        // 현재 위치의 이미지 URL 가져오기
        Log.d("ImagePagerAdapter", "Image URL: " + imageUrl);
        // 로그 찍어서 이미지 URL 확인
        Glide.with(context)
                // Glide를 사용해서 이미지를 로드
                .load(imageUrl)
                // 가져온 URL로 이미지를 로드
                .placeholder(R.drawable.loding)
                //로딩이미지설정
                .error(R.drawable.error)
                //에러이미지 설정
                .into(holder.imageView);
        // 가져온 이미지를 ImageView에 넣기
    }
    @Override
    public int getItemCount() {
        // 아이템 개수 메서드
        return imageList.size();
        // 이미지 리스트의 크기를 반환
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        // 이미지 뷰홀더 클래스 생성 (리사이클러뷰.뷰홀더 상속)
        ImageView imageView;
        // 이미지를 표시할 ImageView 생성
        FrameLayout frameLayout;
        // 프레임레이아웃 객체생성
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            // 부모 클래스의 생성자 호출
            imageView = itemView.findViewById(R.id.image_View);
            // itemView에서 image_View 아이디를 가진 ImageView와 연결
            frameLayout = (FrameLayout) itemView;
            // 아이템뷰를 프레임레아이웃에 연결
        }
    }


}
