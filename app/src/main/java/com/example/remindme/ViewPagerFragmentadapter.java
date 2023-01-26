package com.example.remindme;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerFragmentadapter extends FragmentStateAdapter {

    private final String[] titles= new String[]{"Login","Register"};

    public ViewPagerFragmentadapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new RegisterTabFragment();
            case 0:
            default:
                return new LoginTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
