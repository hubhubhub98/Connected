package com.changsdev.whoaressuproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.changsdev.whoaressuproject.fragment.ChatListFragment;
import com.changsdev.whoaressuproject.fragment.OrgListFragment;
import com.changsdev.whoaressuproject.fragment.PlaceFragment;
import com.changsdev.whoaressuproject.fragment.SettingFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import eu.long1.spacetablayout.SpaceTabLayout;

public class MainActivity extends AppCompatActivity {

    ArrayList<Fragment> fragments;
    SpaceTabLayout spaceTabLayout;
    ViewPager viewPager;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            //로그인을 안했을땐 getCurrentUser가 null을 반환함.
            //로그인 안했을때 LoginActivity로 가줘야함.
            convertActivity(LoginActivity.class);
        }
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

    //화면전환하는 메서드.
    private void convertActivity(Class c){
        Intent intent = new Intent(MainActivity.this,c);
        startActivity(intent);
        finish();
    }
}
