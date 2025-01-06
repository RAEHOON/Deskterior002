package com.example.desk0018.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.desk0018.R;
import com.example.desk0018.Topmenu.Buttontop;
import com.example.desk0018.Topmenu.Case;
import com.example.desk0018.Topmenu.Keyboard;
import com.example.desk0018.Topmenu.Lighting;
import com.example.desk0018.Topmenu.Monitor;
import com.example.desk0018.Topmenu.Mouse;
import com.example.desk0018.Topmenu.Multitap;
import com.example.desk0018.Topmenu.Speaker;
import com.example.desk0018.Topmenu.Table;

import java.util.List;

/***
 *홈프레그먼트 상단부분 버튼 어뎁터
 * 데이터를 뷰 형식으로 바꿔서 버튼탑리스트에 넣어줌
 *
 */

public class BtnAdaptertop extends RecyclerView.Adapter<BtnAdaptertop.ItemViewHolder> {

    static String TAG = "BtnAdaptertop"; // 로그 태그
    private final Context context;
    //컨텍스트 객체생성
    private final List<Buttontop> btnList1;
    //리스트 생성 버튼탑 으로 구성된 리스트

    public BtnAdaptertop(Context context, List<Buttontop> btnList1) {
        Log.d(TAG, "BtnAdaptertop 어뎁터 초기화 , context, btnList1 ");
        this.context = context;
        this.btnList1 = btnList1;
        //컨텍스트랑 리스트를 어뎁터에 넣고 객체 초기화. this 붙은게 외부것들
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //리사이클러뷰에 대한 새로운 뷰 홀더 생성 메서드
        Log.d(TAG, "onCreateViewHolder 메서드 시작 viewType");
        View view = LayoutInflater.from(context).inflate(R.layout.buttontop, parent, false);
        //레이아운인플레이터로 레이아웃 buttontop 을 view 객제체 적용
        return new ItemViewHolder(view);
        // 위에 만든 view를 새로운 아이템뷰홀더에 적용
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //온바인드 뷰홀더 메서드
        Log.d(TAG, "onBindViewHolder 메서드 시작 ");
        Buttontop buttontop = btnList1.get(position);
        //버튼탑 객채 생성하고, 리스트에서 position을 가져옴.
        Log.d(TAG, "onBindViewHolder에서 Buttontop 객체만듬, 이름: " + buttontop.getName());

        Glide.with(context)
                //글라이드 .with 이 작업범위 그 안에 아까 만든 컨텍스트 넣기
                .load(buttontop.getBtnImage())
                //버튼탑에 겟비티엔이미지를 글라이드에 넣기
                .circleCrop()
                //이미지를 동그랗게 잘라
                .fitCenter()
                //가운데에 맞춰
                .placeholder(R.drawable.loding)
                //로딩이미지
                .error(R.drawable.error)
                //에러이미지
                .into(holder.imageView);
        //위에서 넣은 이미지를 넣을 이미지뷰 위치 지정
        Log.d(TAG, "onBindViewHolder: 이미지가 imageView에 로드됨");

        holder.textView_image.setText(buttontop.getName());
        // 텍스트뷰이미지에다가 버튼탑에서 겟네임 해서 텍스트 설정?
        Log.d(TAG, "onBindViewHolder: TextView에 이름설정 - " + buttontop.getName());

        holder.itemView.setOnClickListener(v -> {
            // 아이템뷰를 클릭하면 메서드 실행하기
            Log.d(TAG, "onBindViewHolder: 아이템 클릭했고,  position값 = " + position);
            String buttonName = buttontop.getName();
            // 이름 확인하기 버튼탑에서 겟네임해서
            Log.d(TAG, "onBindViewHolder: 버튼 이름 = " + buttonName);

            Intent intent = null;
            // 인텐트를 생성해서 null로 설정해놓고
            switch (buttonName) {
                //버튼이름 하나씩 바꿔서 넣기
                case "키보드":
                    intent = new Intent(context, Keyboard.class);
                    // 키보드 일 경우 키보드 클래스로 인텐트
                    break;
                case "모니터":
                    intent = new Intent(context, Monitor.class);
                    // 모니터 일 경우 모니터 클래스로 인텐트
                    break;
                case "마우스, 패드":
                    intent = new Intent(context, Mouse.class);
                    // 마우스, 패드 일경우 인텐트
                    break;
                case "스피커":
                    intent = new Intent(context, Speaker.class);
                    // 스피커일 경우 인텐트
                    break;
                case "조명":
                    intent = new Intent(context, Lighting.class);
                    // 조명일 경우 인텐트
                    break;
                case "케이스":
                    intent = new Intent(context, Case.class);
                    //케이스일 경우 인텐트
                    break;
                case "테이블":
                    intent = new Intent(context, Table.class);
                    //테이블일 경우 인텐트
                    break;
                case "선 정리":
                    intent = new Intent(context, Multitap.class);
                    // 선정리 일 경우 인텐트
                    break;
            }

            if (intent != null) {
                // 인텐트가 null 이 아니라면
                Log.d(TAG, "onBindViewHolder: " + buttonName + " 액티비티 실행");
                context.startActivity(intent);
                //엑티비티를 실행
            }
        });
    }

    @Override
    public int getItemCount() {
        //크기를 가져온다
        int size = btnList1.size();
        Log.d(TAG, "getItemCount: 리스트 크기 = " + size);
        return size;
        //리스트의 크기를 가져온다
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        // 아이템뷰홀더 클래스 생성 리사이클러뷰.뷰홀더 상속 (위에서 써먹을 수 있게)

        ImageView imageView;
        //이미지뷰생성
        TextView textView_image;
        //텍스트뷰 생성

        public ItemViewHolder(View itemView) {
            //아이템뷰홀더 메서드 생성 View itemView를 매개변수
            super(itemView);
            //상속받은 부모클래스 리사이클러뷰.뷰홀더부르기, itemView를 넣기
            Log.d(TAG, "ItemViewHolder: ViewHolder 초기화됨");
            imageView = itemView.findViewById(R.id.btn_image1);
            //아이템뷰에 이미지 지정
            textView_image = itemView.findViewById(R.id.textview_image1);
            //텍스트뷰이미지에 이미지 지정
            Log.d(TAG, "ItemViewHolder: ImageView와 TextView 초기화됨");
        }
    }
}
