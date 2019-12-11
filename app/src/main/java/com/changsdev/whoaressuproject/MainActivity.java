package com.changsdev.whoaressuproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.changsdev.whoaressuproject.fragment.ChatListFragment;
import com.changsdev.whoaressuproject.fragment.ChatListFragment1;
import com.changsdev.whoaressuproject.fragment.OrgListFragment;
import com.changsdev.whoaressuproject.fragment.PlaceFragment;
import com.changsdev.whoaressuproject.fragment.SettingFragment;
import com.changsdev.whoaressuproject.model.loading_activity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import eu.long1.spacetablayout.SpaceTabLayout;

public class MainActivity extends AppCompatActivity {

    ArrayList<Fragment> fragments;
    SpaceTabLayout spaceTabLayout;
    ViewPager viewPager;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }

        }
        return super.dispatchTouchEvent(event);
    }

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, loading_activity.class);
        startActivity(intent);

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
        fragments.add(new ChatListFragment1());
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

    ////////// back 버튼 2번 클릭 시 앱 종료 //////////
    // 뒤로가기 버튼 입력시간이 담길 long 객체
    private long pressedTime = 0;

    // 리스너 생성
    public interface OnBackPressedListener {
        public void onBack();
    }

    // 리스너 객체 생성
    private OnBackPressedListener mBackListener;

    // 리스너 설정 메소드
    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mBackListener = listener;
    }

    // 뒤로가기 버튼을 눌렀을 때의 오버라이드 메소드
    @Override
    public void onBackPressed() {

        // 다른 Fragment 에서 리스너를 설정했을 때 처리
        if(mBackListener != null) {
            mBackListener.onBack();
            // 리스너가 설정되지 않은 상태(예를들어 메인Fragment)라면
            // 뒤로가기 버튼을 연속적으로 두번 눌렀을 때 앱이 종료
        } else {
            if ( pressedTime == 0 ) {
                Snackbar.make(findViewById(R.id.main_xml),
                        " 한 번 더 누르면 종료됩니다." , Snackbar.LENGTH_LONG).show();
                pressedTime = System.currentTimeMillis();
            }
            else {
                int seconds = (int) (System.currentTimeMillis() - pressedTime);

                if ( seconds > 2000 ) {
                    Snackbar.make(findViewById(R.id.main_xml),
                            " 한 번 더 누르면 종료됩니다." , Snackbar.LENGTH_LONG).show();
                    pressedTime = 0 ;
                }
                else {
                    super.onBackPressed();
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        }
    }

}
