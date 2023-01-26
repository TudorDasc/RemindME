package com.example.remindme.HomeMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.remindme.R;
import com.example.remindme.ViewPagerFragmentadapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeScreen extends AppCompatActivity {

    ViewPagerFragmentadapter2 viewPagerFragmentadapter2;
    TabLayout tabLayout2;
    ViewPager2 viewPager3;
    private final String[] titles= new String[]{"Updates","Persons", "Settings"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_menu_2);
        getSupportActionBar().hide();

        viewPager3 = findViewById(R.id.view_pager2);
        tabLayout2 = findViewById(R.id.tab_layout2);
        viewPagerFragmentadapter2 = new ViewPagerFragmentadapter2(this);

        viewPager3.setAdapter(viewPagerFragmentadapter2);

        new TabLayoutMediator(tabLayout2,viewPager3,((tab, position) -> tab.setText(titles[position]))).attach();

    }
}