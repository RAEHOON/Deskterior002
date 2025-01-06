package com.example.desk0018.Tag;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/***
 * 태그가 드래그 되게 해주는 클래스
 *
 */

public class TagDragListener implements View.OnTouchListener {
    //태그 드래그해서 움직이게 해주는 클래스, 뷰.온터치리스너 인터페이스 씀

    private float dX, dY;
    // 드래그 시작할 때 위치 변수

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 뷰를 터치할 때 메서드, 뷰 v랑 모션이벤트 event
        View parent = (View) v.getParent();
        // 뷰의 부모 = 뷰의 부모 뷰 가져오기
        if (parent instanceof ViewGroup) {
            // 부모 뷰그룹이 ViewGroup에 속한다면
            ViewGroup parentGroup = (ViewGroup) parent;
            // 부모 뷰를 ViewGroup으로 묶음

            switch (event.getAction()) {
                //겟엑션 이벤트들의 종류 나누기
                case MotionEvent.ACTION_DOWN:
                    //태그를 터치했을 때
                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    //터치한 뷰의 X, Y 위치와 터치 지점의 차이를 계산해 저장
                    parentGroup.requestDisallowInterceptTouchEvent(true);
                    //부모뷰가 터치안되게하기
                    return true;
                //트루 반환
                case MotionEvent.ACTION_MOVE:
                    //태그를 드래그 중일 때
                    v.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    //뷰 움직이는 애니메이션 시작
                    return true;
                //트루반환
                case MotionEvent.ACTION_UP:
                    //손을 떼면
                    parentGroup.requestDisallowInterceptTouchEvent(false);
                    //부모뷰도 다시 터치 될 수 있게
                    return true;
                //트루반환
                case MotionEvent.ACTION_CANCEL:
                    // 사용자가 태그를 놓거나 터치 이벤트가 취소되었을 때
                    parentGroup.requestDisallowInterceptTouchEvent(false);
                    // 부모 뷰가 다시 터치 이벤트를 받을 수 있도록 설정
                    return true;
                //트루반환

                default:
                    return false;
                //나머지는 거짓 반환
            }
        }
        return false;
        // 부모 뷰가 ViewGroup이 아닐 경우 false 반환
    }
}