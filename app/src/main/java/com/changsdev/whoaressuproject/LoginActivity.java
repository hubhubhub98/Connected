package com.changsdev.whoaressuproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.changsdev.whoaressuproject.model.UserVO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdittext;
    private EditText pwEdittext;
    private EditText pwCheckEdittext;
    private EditText nameEdittext;
    private TextView pwResetText;
    private Button registerBtn;
    private Button loginBtn;

    private CheckBox autoLoginCheckBox;
    private FirebaseAuth mAuth;
    private static final String TAG = "LOGINACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        SharedPreferences sp = getSharedPreferences("login_info", Activity.MODE_PRIVATE); //로그인 정보파일을 연다.
        mAuth = FirebaseAuth.getInstance();

        if(!sp.getBoolean("autoLogin",false)){ //autoLogin이라는 키의 값이 true면 자롱로그인 설정을 이미했다는것
            //autoLogin값이 false라면 자동로그인 설정을 이전에 하지 않았다는것
            mAuth.signOut();
        }
        if(mAuth.getCurrentUser() != null){ //이미 로그인을 한상태라면
            convertActivity(MainActivity.class);
        }

        registerBtn = (Button)findViewById(R.id.register_btn);
        loginBtn = (Button)findViewById(R.id.login_btn);
        emailEdittext = (EditText)findViewById(R.id.email_edittext);
        pwEdittext = (EditText)findViewById(R.id.pw_edittext);
        pwCheckEdittext = (EditText)findViewById(R.id.pwCheck_edittext);
        nameEdittext = (EditText)findViewById(R.id.name_edittext);
        pwResetText = (TextView)findViewById(R.id.pw_reset_btn);
        registerBtn.setEnabled(false); //기본적으로 회원가입버튼은 일단 비활성화
        registerBtn.setBackgroundResource(R.drawable.register_button_not_enable_style);
        autoLoginCheckBox = (CheckBox)findViewById(R.id.auto_login_checkbox);

        pwCheckEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) { // 해당 에딧텍스트 값이 변경될떄마다 호출
                String result = s.toString(); //에딧텍스트의 입력한 값을 얻어온다.
                if(result == null || result.equals("") || result.trim().equals("")){
                    //에딧텍스트의 값이 비어있을때
                    registerBtn.setEnabled(false);
                    registerBtn.setBackgroundResource(R.drawable.register_button_not_enable_style);
                    return ;
                }

                //비어있지 않을때
                registerBtn.setEnabled(true);
                registerBtn.setBackgroundResource(R.drawable.login_button_style);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        View.OnClickListener myClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.pw_reset_btn:
                        Intent intent = new Intent(LoginActivity.this, PwResetActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.login_btn:
                        login();
                        break;
                    case R.id.register_btn:
                        register();
                        break;
                }
            }
        };

        pwResetText.setOnClickListener(myClickListener);
        loginBtn.setOnClickListener(myClickListener);
        registerBtn.setOnClickListener(myClickListener);
    }

    private void login(){ //로그인 처리 메서드
        String emailText = emailEdittext.getText().toString().trim();
        String pwText = pwEdittext.getText().toString().trim();
        if(emailText == null || emailText.equals("") || emailText.trim().equals("") ||
            pwText == null || pwText.equals("") || pwText.trim().equals("")) {
            //이메일이나 비번을 입력하지않고 로그인버튼을 눌렀을때
            showToast("이메일과 비밀번호를 입력해주세요.");
            return;
        }

        final SharedPreferences sp = getSharedPreferences("login_info", Activity.MODE_PRIVATE); //로그인 정보파일을 연다.
        //로그인 처리
        mAuth.signInWithEmailAndPassword(emailText, pwText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            showToast("로그인에 성공하셨습니다.");
                            SharedPreferences.Editor editor = sp.edit();
                            if(autoLoginCheckBox.isChecked()){
                                //자동로그인 체크박스가 체크되어있으면
                                editor.putBoolean("autoLogin",true); //자동로그인설정을 했다고 기록한다.
                            }else{
                                //자동로그인 체크박스가 체크되어있지않다면
                                editor.putBoolean("autoLogin",false); //자동로그인설정을 하지 않았다고 기록한다.
                            }
                            editor.commit();
                            convertActivity(MainActivity.class);
                        } else {
                            // If sign in fails, display a Message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            showToast("로그인에 실패했습니다.");
                        }
                    }
                });
    }

    private void register(){ //회원가입 처리메서드
        final String emailText = emailEdittext.getText().toString().trim();
        String pwText = pwEdittext.getText().toString().trim();
        String pwCheckText = pwCheckEdittext.getText().toString().trim();
        final String nameText = nameEdittext.getText().toString().trim();
        if(emailText == null || emailText.equals("") || emailText.trim().equals("") ||
                pwText == null || pwText.equals("") || pwText.trim().equals("") ||
                pwCheckText == null || pwCheckText.equals("") || pwCheckText.trim().equals("") ||
                nameText == null || nameText.equals("") || nameText.trim().equals("")){
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
                            UserVO user = new UserVO();
                            String uid = task.getResult().getUser().getUid();
                            user.setUid(uid);
                            user.setUserName(nameText);
                            user.setUserEmail(emailText);
                            FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                                    .setValue(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Write was successful!
                                            // ...
                                            showToast("회원가입성공");
                                            convertActivity(MainActivity.class);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Write failed
                                            // ...
                                            showToast("회원가입실패");
                                        }
                                    });



                        } else {
                            // If sign in fails, display a Message to the user.
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

    //화면전환하는 메서드.
    private void convertActivity(Class c){
        Intent intent = new Intent(LoginActivity.this,c);
        startActivity(intent);
        finish();
    }
}
