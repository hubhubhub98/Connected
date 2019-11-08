package com.changsdev.whoaressu;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdittext;
    private EditText pwEdittext;
    private EditText pwCheckEdittext;
    private EditText nameEdittext;
    private TextView pwResetText;
    private Button registerBtn;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        registerBtn = (Button)findViewById(R.id.register_btn);
        loginBtn = (Button)findViewById(R.id.login_btn);
        emailEdittext = (EditText)findViewById(R.id.email_edittext);
        pwEdittext = (EditText)findViewById(R.id.pw_edittext);
        pwCheckEdittext = (EditText)findViewById(R.id.pwCheck_edittext);
        nameEdittext = (EditText)findViewById(R.id.name_edittext);
        pwResetText = (TextView)findViewById(R.id.pw_reset_btn);
        registerBtn.setEnabled(false); //기본적으로 회원가입버튼은 일단 비활성화
        registerBtn.setBackgroundResource(R.drawable.register_button_not_enable_style);

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
                        Intent intent = new Intent(LoginActivity.this,PwResetActivity.class);
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
        String emailText = emailEdittext.getText().toString();
        String pwText = pwEdittext.getText().toString();
        if(emailText == null || emailText.equals("") || emailText.trim().equals("") ||
            pwText == null || pwText.equals("") || pwText.trim().equals("")){
            //이메일이나 비번을 입력하지않고 로그인버튼을 눌렀을때
            showToast("이메일과 비밀번호를 입력해주세요.");
            return ;
        }

        //로그인 처리
    }

    private void register(){ //회원가입 처리메서드
        String emailText = emailEdittext.getText().toString();
        String pwText = pwEdittext.getText().toString();
        String pwCheckText = pwCheckEdittext.getText().toString();
        String nameText = nameEdittext.getText().toString();
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


    }
    private void showToast(String msg){ //msg를 화면에 출력한다.
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }
}
