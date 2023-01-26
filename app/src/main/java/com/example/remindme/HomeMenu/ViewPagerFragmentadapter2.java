package com.example.remindme.HomeMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerFragmentadapter2 extends FragmentStateAdapter {

    private final String[] titles= new String[]{"Updates", "Persons", "Settings"};

    public ViewPagerFragmentadapter2(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 2:
                return new Settings_menu();
            case 1:
                return new persones();
            case 0:
            default:
                return new Updates();
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
