package com.changsdev.whoaressuproject.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.UserManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.changsdev.whoaressuproject.LoginActivity;
import com.changsdev.whoaressuproject.MainActivity;
import com.changsdev.whoaressuproject.PwResetActivity;
import com.changsdev.whoaressuproject.R;
import com.changsdev.whoaressuproject.model.UserVO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements MainActivity.OnBackPressedListener{

    TextView nameView;
    EditText nameEdit;
    ImageButton resetName;
    Button emailBtn;
    Button pwBtn;
    Button logoutBtn;
    String check = "Edit";

    Button n;
    Button y;
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        nameView = (TextView)v.findViewById(R.id.name_view);
        nameEdit = (EditText)v.findViewById(R.id.name_edit);
        resetName = (ImageButton)v.findViewById(R.id.reset_btn);
        emailBtn = (Button)v.findViewById(R.id.email_setting);
        pwBtn = (Button)v.findViewById(R.id.pw_setting);
        logoutBtn = (Button)v.findViewById(R.id.logout_button);

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserVO userModel = dataSnapshot.getValue(UserVO.class);
                        nameView.setText(userModel.getUserName());
                        nameEdit.setText(userModel.getUserName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.reset_btn:
                        // EditText로 전환
                        if(check.equals("Edit")){
                            nameView.setVisibility(View.GONE);
                            nameEdit.setVisibility(View.VISIBLE);
                            nameEdit.setText(nameView.getText().toString());
                            check = "View";
                        }
                        // TextView로 전환
                        else if(check.equals("View")){
                            final String inputNameText = nameEdit.getText().toString().trim();

                            FirebaseDatabase.getInstance().getReference().child("users/"+uid+"/userName")
                            .setValue(inputNameText)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Write was successful!
                                        nameEdit.setVisibility(View.GONE);
                                        nameView.setVisibility(View.VISIBLE);
                                        nameView.setText(inputNameText);
                                        check = "Edit";
                                        Toast.makeText(getContext(),"이름 수정에 성공했습니다.",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                    .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Write failed
                                                // ...
                                                Toast.makeText(getContext(),"이름 수정에 실패했습니다.",Toast.LENGTH_SHORT).show();
                                            }
                                        });

                        }
                        break;
                    case R.id.email_setting:
                        Toast.makeText(getContext(),FirebaseAuth.getInstance().getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.pw_setting:
                        Intent intent2 = new Intent(getContext(), PwResetActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.logout_button:
                        final Dialog dialog = new Dialog(getContext(),
                                android.R.style.Theme_Translucent_NoTitleBar);

                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.setting_dialog);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.gravity = Gravity.CENTER;
                        dialog.getWindow().setAttributes(lp);

                        n = (Button)dialog.findViewById(R.id.no);
                        y = (Button)dialog.findViewById(R.id.yes);

                        n.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });
                        y.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                FirebaseAuth.getInstance().signOut(); //로그아웃
                                SharedPreferences sp = getActivity().getSharedPreferences("login_info", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("autoLogin",false);
                                editor.commit();
                                Intent intent = new Intent(getActivity(), LoginActivity.class); //로그인액티비티로 가기
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
                        dialog.show();
                        break;
                }
            }
        };
        resetName.setOnClickListener(myListener);
        emailBtn.setOnClickListener(myListener);
        pwBtn.setOnClickListener(myListener);
        logoutBtn.setOnClickListener(myListener);
        return v;
    }

    ////////// back 버튼 2번 클릭 시 앱 종료 //////////
    @Override
    public void onBack() {
        // 리스너를 설정하기 위해 Activity 를 받아옴
        MainActivity activity = (MainActivity)getActivity();
        // 한번 뒤로가기 버튼을 눌렀다면 Listener 를 null 로 해제
        activity.setOnBackPressedListener(null);
    }

    // Fragment 호출 시 반드시 호출되는 오버라이드 메소드
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressedListener(this);
    }
}

