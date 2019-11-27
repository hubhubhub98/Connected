package com.changsdev.whoaressuproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.changsdev.whoaressuproject.model.UserVO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ManagerRegisterActivity extends AppCompatActivity {

    private EditText emailEdittext;
    private EditText pwEdittext;
    private EditText pwCheckEdittext;
    private EditText nameEdittext;
    private EditText phoneNumber;
    private Button registerBtn;

    private static final String TAG = "MANAGERREGISTERACTIVITY";

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();

        registerBtn = (Button)findViewById(R.id.register_btn);
        emailEdittext = (EditText)findViewById(R.id.email_edittext);
        pwEdittext = (EditText)findViewById(R.id.pw_edittext);
        pwCheckEdittext = (EditText)findViewById(R.id.pwCheck_edittext);
        nameEdittext = (EditText)findViewById(R.id.name_edittext);
        phoneNumber = (EditText)findViewById(R.id.phonenum_edittext);


        View.OnClickListener myClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.register_btn:
                        registerBtn.setEnabled(false);
                        register();
                        registerBtn.setEnabled(true);
                        break;
                }
            }
        };

        registerBtn.setOnClickListener(myClickListener);


    }

    private void register(){ //회원가입 처리메서드
        final String emailText = emailEdittext.getText().toString().trim();
        String pwText = pwEdittext.getText().toString().trim();
        String pwCheckText = pwCheckEdittext.getText().toString().trim();
        final String nameText = nameEdittext.getText().toString().trim();
        final String phoneText = phoneNumber.getText().toString().trim();
        if(emailText == null || emailText.equals("") || emailText.trim().equals("") ||
                pwText == null || pwText.equals("") || pwText.trim().equals("") ||
                pwCheckText == null || pwCheckText.equals("") || pwCheckText.trim().equals("") ||
                nameText == null || nameText.equals("") || nameText.trim().equals("") ||
                phoneText == null || phoneText.equals("") || phoneText.trim().equals("")){
            showToast("전부 입력해주시기 바랍니다.");
            return ;
        }

        if(!pwText.equals(pwCheckText)){
            // 비밀번호와 비밀번호확인의 값이 다르다면
            showToast("비밀번호와 비밀번호확인이 다릅니다.");
            return ;
        }

        //회원가입 처리
        mAuth.createUserWithEmailAndPassword(emailText, pwText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final UserVO user = new UserVO();
                            final String uid = task.getResult().getUser().getUid();
                            user.setUid(uid);
                            user.setUserName(nameText);
                            user.setUserEmail(emailText);
                            user.setOrg(true);
                            user.setUserPhoneNumber(phoneText);
                            UserVO friend = new UserVO();
                            friend.setOrg(true);
                            FirebaseDatabase.getInstance().getReference().child("friends").child(uid)
                                    .setValue(friend)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Write was successful!
                                            // ...
                                            FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                                                    .setValue(user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Write was successful!
                                                            // ...
                                                            showToast("회원가입에 성공했습니다.");
                                                            nameEdittext.setText("");
                                                            pwCheckEdittext.setText("");
                                                            pwEdittext.setText("");
                                                            phoneNumber.setText("");
                                                            emailEdittext.setText("");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Write failed
                                                            // ...
                                                            showToast("회원가입에 실패했습니다.");
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Write failed
                                            // ...
                                            showToast("회원가입에 실패했습니다.");
                                        }
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            showToast("회원가입에 실패했습니다.");
                        }

                        // ...
                    }
                });


    }

    private void showToast(String msg){ //msg를 화면에 출력한다.
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }
}
