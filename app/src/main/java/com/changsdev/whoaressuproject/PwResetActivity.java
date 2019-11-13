package com.changsdev.whoaressuproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.changsdev.whoaressuproject.model.UserVO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PwResetActivity extends AppCompatActivity {
    private EditText emailEditText;
    private Button emailSendBtn;
    private static final String TAG = "PWRESETACTIVITY";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_reset);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        emailEditText = (EditText)findViewById(R.id.email_edittext);
        emailSendBtn = (Button)findViewById(R.id.email_send_btn);

        mAuth = FirebaseAuth.getInstance();

        emailSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEditText.getText().toString().trim();
                if(email == null || email.equals("") || email.trim().equals("")){
                    showToast("이메일을 입력하세요");
                    return ;
                }

                FirebaseDatabase.getInstance().getReference().child("users")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<UserVO> users = new ArrayList<>();
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    UserVO user = snapshot.getValue(UserVO.class);
                                    users.add(user);
                                }

                                boolean isEmailExist = false; //이메일이 존재하는지 확인
                                for(int a=0; a<users.size(); a++){
                                    if(email.equals(users.get(a).getUserEmail())){
                                        isEmailExist = true; //입력해준 이메일이 데이터베이스에 존재한다.
                                        break;
                                    }
                                }
                                if(isEmailExist){
                                    mAuth.sendPasswordResetEmail(email)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "Email sent.");
                                                        showToast("해당 이메일로 비밀번호 초기화 설정을 보냈습니다.");
                                                    }
                                                }
                                            });
                                }else{
                                    showToast("등록되지 않은 이메일입니다.");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });

    }

    private void showToast(String msg){ //msg를 화면에 출력한다.
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }
}
