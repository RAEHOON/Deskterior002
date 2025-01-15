package com.example.desk0018.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.desk0018.Fragment.FavoriteFeedFragment;
import com.example.desk0018.Fragment.MyFeedFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new MyFeedFragment(); // "내 피드" 프래그먼트
        } else {
            return new FavoriteFeedFragment(); // "즐겨찾는 피드" 프래그먼트
        }
    }

    @Override
    public int getItemCount() {
        return 2; // 탭 개수
    }
}
