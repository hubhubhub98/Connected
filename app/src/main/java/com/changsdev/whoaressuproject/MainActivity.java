package com.changsdev.whoaressuproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.changsdev.whoaressuproject.function.Makechatroom;
import com.changsdev.whoaressuproject.function.Sendmessage;
import com.changsdev.whoaressuproject.fragment.ChatListFragment;
import com.changsdev.whoaressuproject.fragment.OrgListFragment;
import com.changsdev.whoaressuproject.fragment.PlaceFragment;
import com.changsdev.whoaressuproject.fragment.SettingFragment;
import com.changsdev.whoaressuproject.model.UserVO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        new Sendmessage("김창호","IcnUXtGEfFdefWvZepalny04Nd52","ㅁ젇랴뭊대랴ㅜㅈ매ㅑ둘매ㅑ줃래ㅑ뭊대ㅑ룸재ㅑ두랴ㅐㅁ주대ㅑ루매ㅑ줄대ㅑ무쟈대ㅜㄹ매ㅑ줃래ㅜㅈ먀ㅐㄷ루먀잳래ㅑㅁ주대ㅑ룾매ㅑ둘매ㅑㅈ두래ㅑ뭊ㄷㄹ","-LuIzNbWL7VMTtqXmfxy");

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
