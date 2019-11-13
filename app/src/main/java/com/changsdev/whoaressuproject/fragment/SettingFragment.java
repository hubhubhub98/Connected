package com.changsdev.whoaressuproject.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.changsdev.whoaressuproject.LoginActivity;
import com.changsdev.whoaressuproject.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    Button logoutBtn;
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        logoutBtn = (Button)v.findViewById(R.id.logout_button);

        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.logout_button:
                        FirebaseAuth.getInstance().signOut(); //로그아웃
                        SharedPreferences sp = getActivity().getSharedPreferences("login_info", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("autoLogin",false);
                        editor.commit();
                        Intent intent = new Intent(getActivity(), LoginActivity.class); //로그인액티비티로 가기
                        startActivity(intent);
                        getActivity().finish();
                }
            }
        };

        logoutBtn.setOnClickListener(myListener);
        return v;
    }

}
