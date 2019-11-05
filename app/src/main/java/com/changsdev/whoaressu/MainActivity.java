package com.changsdev.whoaressu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.changsdev.whoaressu.fragment.ChatListFragment;
import com.changsdev.whoaressu.fragment.OrgListFragment;
import com.changsdev.whoaressu.fragment.PlaceFragment;
import com.changsdev.whoaressu.fragment.SettingFragment;

import java.util.ArrayList;

import eu.long1.spacetablayout.SpaceTabLayout;

public class MainActivity extends AppCompatActivity {

    ArrayList<Fragment> fragments;
    SpaceTabLayout spaceTabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab = getSupportActionBar();
        ab.hide();
        viewPager = findViewById(R.id.vp_pager);
        spaceTabLayout = findViewById(R.id.tl_space_tabs);
        fragments = new ArrayList<>();
        fragments.add(new OrgListFragment());
        fragments.add(new ChatListFragment());
        fragments.add(new PlaceFragment());
        fragments.add(new SettingFragment());


        spaceTabLayout.initialize(viewPager,getSupportFragmentManager(),fragments,savedInstanceState);

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        spaceTabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }
}
