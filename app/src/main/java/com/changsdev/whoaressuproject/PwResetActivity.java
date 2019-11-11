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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

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
                String email = emailEditText.getText().toString();
                if(email == null || email.equals("") || email.trim().equals("")){
                    showToast("이메일을 입력하세요");
                    return ;
                }
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
            }
        });

    }

    private void showToast(String msg){ //msg를 화면에 출력한다.
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }
}
